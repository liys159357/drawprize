package com.ss.lottery.controller;


import com.ss.lottery.entity.LotteryResult;
import com.ss.lottery.entity.vo.UserLotteryRecordVO;
import com.ss.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;
    /**
     * 抽奖
     *
     * @param userId 用户ID
     * @return 抽奖结果
     */
    @GetMapping("/draw/{userId}")
    public LotteryResult drawLottery(@PathVariable String userId) {
        return lotteryService.drawLottery(userId);
    }

    /**
     *
     * 获取用户抽奖记录
     * @param
     * @return
     */
    //
    @GetMapping("/records")
    public List<UserLotteryRecordVO> getUserRecords(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println("token:"+token);
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Authorization token is required");
        }
        return lotteryService.getUserRecords(token);
    }
}
