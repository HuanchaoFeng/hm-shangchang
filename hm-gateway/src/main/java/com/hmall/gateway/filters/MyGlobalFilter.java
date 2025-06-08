package com.hmall.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyGlobalFilter implements GlobalFilter , Ordered {

    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //登录校验

        //获取request
        ServerHttpRequest request = exchange.getRequest();

        //判断是否拦截
        if(isExclude(request.getPath().toString())){
            //放行
            return chain.filter(exchange);
        }

        //获取token
        String token=null;
        List<String> authorization = request.getHeaders().get("authorization");
        if(authorization != null && !authorization.isEmpty()){
            token = authorization.get(0);
        }

        //检验token
        Long userId=null;
        try {
            userId = jwtTool.parseToken(token);
        }catch (Exception e){
            //拦截，设置响应状态码为401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //传递用户信息
        System.out.println("userID:"+userId);
        String userInfo = userId.toString();
        ServerWebExchange newExchange = exchange.mutate().request(
                b -> b.header("user-info", userInfo)
        ).build();

        //放行
        return chain.filter(exchange);

    }


    //顺序
    @Override
    public int getOrder() {
        //值越小，优先级越高
        return 0;
    }

    public boolean isExclude(String path){
        for (String pathPattern : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern, path)){
                return true;
            }
        }
        return false;
    }


}
