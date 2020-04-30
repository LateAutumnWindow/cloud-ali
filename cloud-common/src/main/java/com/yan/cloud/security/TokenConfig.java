package com.yan.cloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 令牌管理:内存模式
 */
@Configuration
public class TokenConfig {

    private String SIGNING_KEY = "AAW";

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtToken = new JwtAccessTokenConverter();
        // 对称密钥，资源服务器使用该密钥来验证
        jwtToken.setSigningKey(SIGNING_KEY);
        return jwtToken;
    }

}
