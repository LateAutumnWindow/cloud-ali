package com.yan.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 认证授权服务
 */
@EnableAuthorizationServer
@SpringBootApplication
public class SecurityAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthApplication.class, args);
    }

}
