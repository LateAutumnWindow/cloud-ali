package com.yan.cloud.controller;

import com.yan.cloud.CommonResult;
import com.yan.cloud.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/goods/price/{code}/{count}")
    public CommonResult getGoodsPrice(@PathVariable("code") String code, @PathVariable("count") Integer count) {
        return storageService.getGoodsPrice(code, count);
    }

    @PostMapping("/{code}/{count}")
    public Map<String, Object> update(@PathVariable("code") String code, @PathVariable("count") Integer count) {
        Map<String, Object> rest = new HashMap<>();

        int i = storageService.updateStorage(code, count);

        rest.put("code", 200);
        rest.put("data", i);
        return  rest;
    }
}
