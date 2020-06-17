package com.yan.cloud.config;

import com.alibaba.fastjson.JSON;
import com.yan.cloud.CommonResult;
import com.yan.cloud.ResponseCodeEnum;
import com.yan.cloud.exception.TokenAuthenticationException;
import com.yan.cloud.security.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器
 * @author Yan
 */
@Slf4j
@Component
@RefreshScope
public class GatewayAfterFilter implements GlobalFilter, Ordered {

    @Value("${secretKey}")
    private String secretKey;

    /**
     * 过滤内容
     */
     @Override
     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
         ServerHttpRequest serverHttpRequest = exchange.getRequest();
         ServerHttpResponse serverHttpResponse = exchange.getResponse();
         String uri = serverHttpRequest.getURI().getPath();

         //  检查白名单（配置）
         if (uri.indexOf("/login") >= 0) {
             return chain.filter(exchange);
         }

         String token = serverHttpRequest.getHeaders().getFirst("token");
         if (StringUtils.isBlank(token)) {
             serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
             return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_MISSION);
         }

         //todo 检查Redis中是否有此Token
         System.out.println(" = = ==== = = == = = == = = " + secretKey);
         try {
             JWTUtil.verifyToken(token, secretKey);
         } catch (TokenAuthenticationException ex) {
             return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
         } catch (Exception ex) {
             return getVoidMono(serverHttpResponse, ResponseCodeEnum.UNKNOWN_ERROR);
         }

         String userId = JWTUtil.getUserInfo(token);

         ServerHttpRequest mutableReq = serverHttpRequest.mutate().header("userId", userId).build();
         ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

         return chain.filter(mutableExchange);
     }

     private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ResponseCodeEnum responseCodeEnum) {
         serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
         CommonResult responseResult = CommonResult.error(responseCodeEnum.getCode(), responseCodeEnum.getMessage());
         DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes());
         return serverHttpResponse.writeWith(Flux.just(dataBuffer));
     }
    /**
     *  设置过滤器执行的顺序，越小越在前
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
