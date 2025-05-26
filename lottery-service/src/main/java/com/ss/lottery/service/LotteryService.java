package com.ss.lottery.service;


import com.ss.lottery.entity.LotteryResult;

public interface LotteryService {
    LotteryResult drawLottery(String userId);
}
