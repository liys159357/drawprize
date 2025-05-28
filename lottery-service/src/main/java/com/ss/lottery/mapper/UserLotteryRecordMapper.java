package com.ss.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ss.lottery.entity.UserLotteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLotteryRecordMapper extends BaseMapper<UserLotteryRecord> {


    /**
     * 统计用户已抽奖次数 todo
     * @param userId
     * @return
     */

    int countByUserId(String userId);
}
