package com.ss.prize.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ss.prize.entity.Prize;
import com.ss.prize.mapper.PrizeMapper;
import com.ss.prize.service.PrizeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize> implements PrizeService {
    private final PrizeMapper prizeMapper;

    public PrizeServiceImpl(PrizeMapper prizeMapper) {
        this.prizeMapper = prizeMapper;
    }

    @Override
    public List<Prize> getAvailablePrizes() {
        return baseMapper.selectAvailablePrizes();
    }

    @Override
    public boolean decreaseRemaining(Long id) {
        return baseMapper.decreaseRemaining(id);
    }

}
