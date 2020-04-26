package com.yan.cloud.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "cloud-order-service")
public interface OrderAPI {

}
