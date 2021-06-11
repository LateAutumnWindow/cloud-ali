package com.yan.cloud.service.impl;

import com.yan.cloud.CommonResult;
import com.yan.cloud.dao.StorageMapper;
import com.yan.cloud.pojo.Storage;
import com.yan.cloud.service.StorageService;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageMapper storageMapper;

    @Override
    public CommonResult getGoodsPrice(String commodityCode, Integer count) {
        System.out.println(RootContext.getXID() + " ==  ===========================================");
        Storage goods = storageMapper.getGoodsUnitPrice(commodityCode);
        int price = goods.getUnitPrice() * count;
        storageMapper.dwindleNumbers(commodityCode, (goods.getCount() - count));
        return CommonResult.success("计算成功", price);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStorage(String code, Integer count) {
        int i = storageMapper.upStorage(code, count);
        int j = 10 / count;
        return i;
    }
}
