package com.ss.prize.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ss.prize.entity.Prize;
import com.ss.prize.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prizes")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    /**
     * 查询所有可用奖品
     */
    @GetMapping("/available")
    public List<Prize> getAvailablePrizes() {
        return prizeService.getAvailablePrizes();
    }

    /**
     * 分页查询奖品列表
     */
    @GetMapping
    public IPage<Prize> getPrizes(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return prizeService.page(new Page<>(page, size));
    }

    /**
     * 根据ID查询奖品
     */
    @GetMapping("/{id}")
    public Prize getPrizeById(@PathVariable Long id) {
        return prizeService.getById(id);
    }

    /**
     * 添加奖品
     */
    @PostMapping
    public Prize addPrize(@RequestBody Prize prize) {
        prizeService.save(prize);
        return prize;
    }

    /**
     * 更新奖品信息
     */
    @PutMapping("/{id}")
    public Prize updatePrize(@PathVariable Long id, @RequestBody Prize prize) {
        prize.setId(id);
        prizeService.updateById(prize);
        return prize;
    }

    /**
     * 删除奖品
     */
    @DeleteMapping("/{id}")
    public void deletePrize(@PathVariable Long id) {
        prizeService.removeById(id);
    }

    /**
     *扣减库存
     * @param id
     * @return
     */
    @PostMapping("/{id}/decrease")
    public ResponseEntity<Boolean> decreaseStock(@PathVariable Long id) {
        boolean success = prizeService.decreaseRemaining(id);
        return ResponseEntity.ok(success);
    }
}
