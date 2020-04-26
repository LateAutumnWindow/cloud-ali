package com.yan.cloud.controller;

import com.yan.cloud.CommonResult;
import com.yan.cloud.service.AccountService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PutMapping("/deduct/money")
    public CommonResult deductMoney(@RequestParam("userId") String userId,@RequestParam("money") Integer money) {
        return accountService.deductMoney(userId, money);
    }

}
