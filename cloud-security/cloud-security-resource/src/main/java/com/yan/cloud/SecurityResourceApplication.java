package com.yan.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 资源服务
 */
@EnableResourceServer
@SpringBootApplication
public class SecurityResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityResourceApplication.class, args);
    }

}
