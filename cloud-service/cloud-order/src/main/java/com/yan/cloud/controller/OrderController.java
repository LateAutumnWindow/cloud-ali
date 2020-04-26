package com.yan.cloud.controller;

import com.yan.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RefreshScope
public class OrderController {

    @Value("${server.port}")
    private String port;
    @Autowired
    private OrderService orderService;

    @GetMapping("/create/order/{userId}/{commodityCode}/{count}")
    public void createOrder(@PathVariable("userId") String userId,
                             @PathVariable("commodityCode") String commodityCode,
                             @PathVariable("count") Integer count) {
        orderService.createOrder(userId, commodityCode, count);
    }

}
