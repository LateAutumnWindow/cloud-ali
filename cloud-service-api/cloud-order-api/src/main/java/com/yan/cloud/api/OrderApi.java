package com.yan.cloud.api;

import com.yan.cloud.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-order-service")
public interface OrderApi {

    @PostMapping("/insert/order")
    CommonResult insertOrder(@RequestParam("userId") String userId,
                               @RequestParam("commodityCode") String commodityCode,
                               @RequestParam("count") Integer count,
                               @RequestParam("countMoney") Integer countMoney);


}
