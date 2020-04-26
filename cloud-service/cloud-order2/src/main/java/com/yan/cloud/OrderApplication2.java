package com.yan.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderApplication2 {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication2.class, args);
    }
}
