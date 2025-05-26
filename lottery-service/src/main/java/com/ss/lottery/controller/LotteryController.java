package com.ss.lottery.controller;


import com.ss.lottery.entity.LotteryResult;
import com.ss.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;

    @GetMapping("/draw/{userId}")
    public LotteryResult drawLottery(@PathVariable String userId) {
        return lotteryService.drawLottery(userId);
    }
}
