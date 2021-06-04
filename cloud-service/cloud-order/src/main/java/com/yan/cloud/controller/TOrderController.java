package com.yan.cloud.controller;

import com.yan.cloud.pojo.Order;
import com.yan.cloud.pojo.TOrder;
import com.yan.cloud.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/create/order")
@RestController
public class TOrderController {
    @Autowired
    private TOrderService orderService;

    @PostMapping("/insert")
    public Map<String, Object> insert(@RequestBody Order order) {
        Map<String, Object> rest = new HashMap<>(4);

        int i = orderService.insertOrder(order);

        rest.put("code", 200);
        rest.put("data", i);
        return rest;
    }

    @PostMapping("/select/{id}")
    public Map<String, Object> select(@PathVariable("id") long id) {
        Map<String, Object> rest = new HashMap<>(4);

        Order orderInfo = orderService.getOrderInfo(id);

        rest.put("code", 200);
        rest.put("data", orderInfo);
        return rest;
    }
}
