package com.yan.cloud.api;

import com.yan.cloud.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "cloud-storage-service")
public interface StorageServerApi {

    @GetMapping("/storage/goods/price/{code}/{count}")
    CommonResult getGoodsPrice(@PathVariable("code") String code, @PathVariable("count") Integer count);

}
