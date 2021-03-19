package com.yan.cloud.service;

import com.yan.cloud.CommonResult;

import java.sql.SQLException;

public interface OrderService {

    CommonResult createOrder(String userId, String commodityCode, int orderCount);

}
