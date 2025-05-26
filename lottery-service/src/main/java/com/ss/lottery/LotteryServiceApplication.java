package com.ss.lottery;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ss.lottery.feign")
@EnableRabbit
@MapperScan("com.ss.lottery.mapper")
public class LotteryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LotteryServiceApplication.class, args);
    }
}