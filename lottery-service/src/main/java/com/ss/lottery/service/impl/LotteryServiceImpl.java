package com.ss.lottery.service.impl;

import com.ss.lottery.entity.LotteryResult;
import com.ss.lottery.entity.Prize;
import com.ss.lottery.entity.UserLotteryRecord;
import com.ss.lottery.feign.PrizeFeignClient;
import com.ss.lottery.mapper.UserLotteryRecordMapper;
import com.ss.lottery.service.LotteryService;
import com.ss.user.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 抽奖服务实现类、处理用户抽奖核心逻辑
 * 包含奖品选择，库存扣减，结果记录和消息通知
 */
@Service
public class LotteryServiceImpl implements LotteryService {

    private final SecureRandom random = new SecureRandom();
    private final PrizeFeignClient prizeFeignClient;  //奖品服务Feign客户端
    private final UserLotteryRecordMapper recordMapper; //抽奖记录数据访问

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;//消息交换器的名称
    private final String routingKey; //消息路由键

    @Autowired
    public LotteryServiceImpl(
            PrizeFeignClient prizeFeignClient,
            UserLotteryRecordMapper recordMapper,
            RabbitTemplate rabbitTemplate,
            @Value("${lottery.mq.exchange}") String exchange,
            @Value("${lottery.mq.routingKey}") String routingKey
    ) {
        this.prizeFeignClient = prizeFeignClient;
        this.recordMapper = recordMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    /**
     * 用户抽奖主方法
     * @param userId
     * @return
     */

    @Override
    @Transactional //保证数据库操作原子性
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 200))
    public LotteryResult drawLottery(String userId) {
        //1.获取所有可用奖品(库存大于0)
        List<Prize> availablePrizes = prizeFeignClient.getAvailablePrizes();
        //2.无可用奖品时直接返回未中奖结果
        if (availablePrizes.isEmpty()) {
            return sendResult(userId, null, false);
        }

        //3.基于库存权重随机选择奖品
        Prize selectedPrize = selectPrizeByWeight(availablePrizes);
        boolean stockDeducted = false;

        //4.尝试扣减选中奖品的库存
        if (selectedPrize != null) {
            ResponseEntity<Boolean> response = prizeFeignClient.decreaseStock(selectedPrize.getId());
            // 检查库存扣减是否成功(HTTP 200且返回true)
            stockDeducted = response.getStatusCode().is2xxSuccessful()
                    && Boolean.TRUE.equals(response.getBody());
        }

        //5.记录抽奖结果（无论是否中奖）
        UserLotteryRecord record = buildRecord(userId, selectedPrize, stockDeducted);
        recordMapper.insert(record);

        //6.发送结果到消息队列并返回
        return sendResult(userId, selectedPrize, stockDeducted);
    }

    /**
     * 基于库存权重的奖品随机选择算法
     * @param prizes
     * @return
     */
    private Prize selectPrizeByWeight(List<Prize> prizes) {
        //计算总库存为总权重
        int totalWeight = prizes.stream().mapToInt(Prize::getRemaining).sum();
        if (totalWeight <= 0) return null;

        //生成[0,totalWeight)范围内的随机数
        int randomValue = random.nextInt(totalWeight);
        AtomicInteger currentWeight = new AtomicInteger();

        //按奖品库存权重分配随机值，实现按库存比例抽奖
        return prizes.stream()
                .filter(prize -> {
                    currentWeight.addAndGet(prize.getRemaining());
                    return randomValue < currentWeight.get();
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * 构建用户抽奖记录实体
     * @param userId 用户ID
     * @param prize 选中的奖品
     * @param success 是否中奖(库存扣减成功)
     * @return 抽奖记录实体
     */
    private UserLotteryRecord buildRecord(String userId, Prize prize, boolean success) {
        UserLotteryRecord record = new UserLotteryRecord();
        record.setUserId(userId);
        record.setPrizeId(success ? prize.getId() : null);
        record.setResult(success);
        record.setCreateTime(new Date());
        return record;
    }

    /**
     * 发送抽奖结果到消息队列并返回结果
     * @param userId 用户ID
     * @param prize 奖品信息
     * @param success 是否中奖
     * @return 抽奖结果对象
     */
    private LotteryResult sendResult(String userId, Prize prize, boolean success) {
        LotteryResult result = new LotteryResult(
                userId,
                success ? prize.getId() : null,
                success ? prize.getName() : "未中奖",
                success,
                System.currentTimeMillis()
        );

        //发送结果到RabbitMq队列，前端可监听此队列获取实时结果

        rabbitTemplate.convertAndSend(exchange, routingKey, result);
        return result;
    }
}
