package com.ss.gateway.filters;


import com.hmall.common.exception.UnauthorizedException;
import com.ss.gateway.config.AuthProperties;
import com.ss.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth.filter")
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private List<String> excludePaths;
    
    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request
        ServerHttpRequest request = exchange.getRequest();
        //2.判断是否需要做登录拦截
        if (isExclude(request.getPath().toString())){
            //放行
            return chain.filter(exchange);
        }

        System.out.println("拦截中.......");

        //3.获取token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        List<String> headers2 = request.getHeaders().get("token");

        System.out.println(headers + " 666");
        System.out.println(headers + " 777");

        if (headers != null && !headers.isEmpty()){
            token = headers.get(0);
        }

        System.out.println("token: " + token);
        //4.校验并解析token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);

            System.out.println("userId: " + userId);

        }catch (UnauthorizedException e){
            //拦截：设置响应状态码为401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //  5.传递用户信息
        String userInfo = userId.toString();  //mutate()方法用于构建一个新的ServerWebExchange实例，允许对请求或响应进行更改
        ServerWebExchange swe = exchange.mutate() //在请求头中添加一个名为"user-info"的Header，并将其值设置为userInfo
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        //6.放行

        System.out.println("fx.......");
        return chain.filter(swe);
    }

    private boolean isExclude(String path) {
        for (String pathPattern  : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern,path)){  //使用antPathMatcher对当前请求路径与排除路径进行匹配
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }
}
