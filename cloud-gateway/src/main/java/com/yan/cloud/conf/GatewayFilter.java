package com.yan.cloud.conf;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器
 * @author Yan
 */
@Component
public class GatewayFilter implements GlobalFilter, Ordered {
    /**
     * 过滤内容
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 可以通过 exchange 获取请求的信息用来判断
        // 过滤通过去下一个过滤器
        return chain.filter(exchange);
    }
    /**
     *  设置过滤器执行的顺序，越小越在前
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
