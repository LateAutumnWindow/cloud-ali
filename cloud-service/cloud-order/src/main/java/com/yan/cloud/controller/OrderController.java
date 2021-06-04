package com.yan.cloud.controller;

import com.yan.cloud.CommonResult;
import com.yan.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create/order/{userId}/{commodityCode}/{count}")
    public CommonResult createOrder(@PathVariable("userId") String userId,
                                    @PathVariable("commodityCode") String commodityCode,
                                    @PathVariable("count") Integer count) {
        return orderService.createOrder(userId, commodityCode, count);
    }
}
