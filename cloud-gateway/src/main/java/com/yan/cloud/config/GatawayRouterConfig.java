package com.yan.cloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置路径映射
 * @author Yan
 */
@Configuration
public class GatawayRouterConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        /*
        参数：
        id：随便起不重复
        patterns: 访问路径
        uri:
            lb://cloud-order-service 是按服务轮询访问，
            也可以是这种格式 http://news.baidu.com
        */
        return builder.routes()
                .route("cloud-order",
                        f -> f.path("/order/**").uri("lb://cloud-order-service"))
                .build();
    }
}
