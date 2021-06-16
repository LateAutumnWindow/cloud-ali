package com.yan.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acc")
public class TestAccountController {


    @GetMapping("/get")
    public String jj() {
        return "Account--jjjj";
    }

}
