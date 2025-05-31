package com.ss.gateway.interceptor;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class UserInfoInterceptor implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //1.获取登录用户信息
        String userInfo = exchange.getRequest().getHeaders().getFirst("user-info");
        //2.判断是否获取了用户，如果有，存入ThreadLocal
        if (StrUtil.isNotBlank(userInfo)){
            UserContext.setUser(Long.valueOf(userInfo));
        }
        //3.放行
        return chain.filter(exchange)
                .doFinally(signalType -> UserContext.removeUser());
    }
}
