package com.ss.prize.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @GetMapping("/list")
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
     * 根据名称查询奖品
     *  使用mybatis-plus模糊查询
     */
    @GetMapping("/name")
    public List<Prize> getPrizesByName(@RequestParam("name") String name) {
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return prizeService.list(queryWrapper);
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
    @PutMapping("/updatePrize")
    public Prize updatePrize(@RequestBody Prize prize) {
        prize.setId(prize.getId());
        prizeService.updateById(prize);
        return prize;
    }

    /**
     * 删除奖品
     */
    @DeleteMapping("/{id}")
    public String deletePrize(@PathVariable Long id) {
        boolean b = prizeService.removeById(id);
        if (b) {
            return "删除成功";
        }
        throw new RuntimeException("删除失败");
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
    /**
     * 根据ID获取奖品名称
     * @param id 奖品ID
     * @return 奖品名称
     */
    @GetMapping("/{id}/name")
    public String getPrizeNameById(@PathVariable("id") Long id) {
        Prize prize = prizeService.getById(id);
        return prize != null ? prize.getName() : "";
    }
}
