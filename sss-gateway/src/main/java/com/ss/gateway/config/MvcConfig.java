package com.ss.gateway.config;

import com.ss.gateway.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class MvcConfig {
    @Bean
    public WebFilter userInfoFilter() {
        return new UserInfoInterceptor();
    }
}
