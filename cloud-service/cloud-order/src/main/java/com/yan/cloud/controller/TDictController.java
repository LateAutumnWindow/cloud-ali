package com.yan.cloud.controller;

import com.yan.cloud.pojo.TDict;
import com.yan.cloud.service.TDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/dict")
@RestController
public class TDictController {

    @Autowired
    private TDictService dictService;

    @PostMapping("/insert")
    public Map<String, Object> insertDict(@RequestBody TDict dict) {
        Map<String, Object> rest = new HashMap<>(4);

        Integer integer = dictService.insertDict(dict);

        rest.put("code", 200);
        rest.put("data", integer);
        return rest;
    }
}
