package com.ss.prize.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ss.prize.entity.Prize;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PrizeMapper extends BaseMapper<Prize> {

    /**
     * 查询可用奖品
     * @return
     */
    List<Prize> selectAvailablePrizes();

    /**
     * 使用乐观锁扣减库存
     *
     * @param id
     * @return
     */
    @Update("UPDATE prize SET remaining = remaining - 1 WHERE id = #{id} AND remaining > 0")
    boolean decreaseRemaining(@Param("id") Long id);

}
