package com.ss.lottery.feign;

import com.ss.user.domain.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);

    /**
     * 根据token获取用户ID
     * @param token
     * @return
     */
    @GetMapping("/users/token/{token}")
    Long getUserIdByToken(@PathVariable String token);
}
