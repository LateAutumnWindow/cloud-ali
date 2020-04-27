package com.yan.cloud.controller;

import com.yan.cloud.CommonResult;
import com.yan.cloud.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/goods/price/{code}/{count}")
    public CommonResult getGoodsPrice(@PathVariable("code") String code, @PathVariable("count") Integer count) {
        return storageService.getGoodsPrice(code, count);
    }

}
