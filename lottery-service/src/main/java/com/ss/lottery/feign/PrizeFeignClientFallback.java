package com.ss.lottery.feign;

import com.ss.lottery.entity.Prize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
@Slf4j
@Component
public class PrizeFeignClientFallback implements PrizeFeignClient {

    @Override
    public List<Prize> getAvailablePrizes() {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity<Boolean> decreaseStock(Long id) {
        return ResponseEntity.ok(false);
    }

    @Override
    public String getPrizeNameById(Long id) {
        log.info("获取奖品名称降级处理，奖品ID: {}", id);
        return "奖品名称获取失败";
    }
}
