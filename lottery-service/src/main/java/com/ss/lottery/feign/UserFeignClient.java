package com.ss.lottery.feign;

import com.ss.user.domain.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//todo
@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
}
