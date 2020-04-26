package com.yan.cloud.api;

import com.yan.cloud.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-account-service")
public interface AccountApi {

     @PutMapping("/account/deduct/money")
     CommonResult deductMoney(@RequestParam("userId") String userId, @RequestParam("money") Integer money);
}



