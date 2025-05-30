package com.ss.lottery.service;


import com.ss.lottery.entity.LotteryResult;
import com.ss.lottery.entity.vo.UserLotteryRecordVO;

import java.util.List;

public interface LotteryService {
    /**
     * 抽奖
     * @param userId 用户ID
     * @return 抽奖结果
     */
    LotteryResult drawLottery(String userId);

    /**
     * 获取用户抽奖记录
     * @param token 用户token
     * @return 用户抽奖记录
     */
    List<UserLotteryRecordVO> getUserRecords(String token);
}
