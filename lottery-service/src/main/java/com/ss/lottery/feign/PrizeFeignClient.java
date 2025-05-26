package com.ss.lottery.feign;

import com.ss.lottery.entity.Prize;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "prize-service", fallback = PrizeFeignClientFallback.class)
public interface PrizeFeignClient {

    @GetMapping("/api/prizes/available")
    List<Prize> getAvailablePrizes();

    @PostMapping("/api/prizes/{id}/decrease")
    ResponseEntity<Boolean> decreaseStock(@PathVariable("id") Long id);
}
