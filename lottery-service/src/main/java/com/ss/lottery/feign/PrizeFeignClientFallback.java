package com.ss.lottery.feign;

import com.ss.lottery.entity.Prize;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PrizeFeignClientFallback implements PrizeFeignClient {

    @Override
    public List<Prize> getAvailablePrizes() {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity<Boolean> decreaseStock(Long id) {
        return ResponseEntity.ok(false);
    }
}
