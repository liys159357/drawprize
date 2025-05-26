package com.ss.prize.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ss.prize.entity.Prize;


import java.util.List;

public interface PrizeService extends IService<Prize> {
    List<Prize> getAvailablePrizes();

    int decreaseRemaining(Long id);
}
