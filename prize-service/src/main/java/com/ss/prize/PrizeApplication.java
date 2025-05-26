package com.ss.prize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableFeignClients(basePackages = "com.hmall.api.client",defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("com.ss.prize.mapper")
@SpringBootApplication
public class PrizeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrizeApplication.class, args);
    }

}