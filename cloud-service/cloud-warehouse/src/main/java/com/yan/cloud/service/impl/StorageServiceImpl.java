package com.yan.cloud.service.impl;

import com.yan.cloud.CommonResult;
import com.yan.cloud.dao.StorageMapper;
import com.yan.cloud.service.StorageService;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageMapper storageMapper;

    @Override
    public CommonResult getGoodsPrice(String commodityCode, Integer count) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(RootContext.getXID() + " ==  ===========================================");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        Integer goodsPrice = storageMapper.getGoodsUnitPrice(commodityCode) * count;
        storageMapper.dwindleNumbers(commodityCode, count);
        return new CommonResult<>(200, "计算成功", goodsPrice);
    }
}
