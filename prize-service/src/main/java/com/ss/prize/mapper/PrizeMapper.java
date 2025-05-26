package com.ss.prize.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ss.prize.entity.Prize;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PrizeMapper extends BaseMapper<Prize> {
    List<Prize> selectAvailablePrizes();

    @Update("UPDATE prize SET remaining = remaining - 1 WHERE id = #{id} AND remaining > 0")
    int decreaseRemaining(@Param("id") Long id);
}
