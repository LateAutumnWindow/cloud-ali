package com.yan.cloud.service;

import com.yan.cloud.CommonResult;

public interface StorageService {

    CommonResult getGoodsPrice(String commodityCode, Integer count);

    int updateStorage(String code, Integer count);
}
