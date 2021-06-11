package com.yan.cloud.controller;

import com.yan.cloud.CommonResult;
import com.yan.cloud.pojo.Order;
import com.yan.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@RestController
@RefreshScope
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create/{userId}/{commodityCode}/{count}")
    public CommonResult createOrder(@PathVariable("userId") String userId,
                                    @PathVariable("commodityCode") String commodityCode,
                                    @PathVariable("count") Integer count) {
        return orderService.createOrder(userId, commodityCode, count);
    }

    @GetMapping("/select/{id}")
    public CommonResult select(@PathVariable("id") long id) {
        Order orderInfo = orderService.getOrderInfo(id);
        return CommonResult.success(orderInfo);
    }
}
