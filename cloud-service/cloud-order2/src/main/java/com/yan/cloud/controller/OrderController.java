package com.yan.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class OrderController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/create/order")
    public void createOrder() {
        System.out.println("当前服务端口号： " + port);
    }

}
