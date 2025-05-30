package com.ss.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ss.lottery.entity.UserLotteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserLotteryRecordMapper extends BaseMapper<UserLotteryRecord> {


    /**
     * 统计用户已抽奖次数
     * @param userId
     * @return
     */

    int countByUserId(String userId);

    /**
     * 根据用户id和是否中奖的结果查询记录
     * @param userId
     * @param
     * @return
     */
    List<UserLotteryRecord> selectByUserIdAndResult(Long userId, int result);
}
