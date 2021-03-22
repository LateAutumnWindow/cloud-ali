package com.yan.cloud.service.impl;

import com.yan.cloud.CommonResult;
import com.yan.cloud.dao.StorageMapper;
import com.yan.cloud.service.StorageService;
import com.yan.cloud.util.ExcelDemo;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageMapper storageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult getGoodsPrice(String commodityCode, Integer count) {
        System.out.println(RootContext.getXID() + " ==  ===========================================");
        Integer goodsPrice = storageMapper.getGoodsUnitPrice(commodityCode) * count;
        storageMapper.dwindleNumbers(commodityCode, count);
        int j = 10 / count;
        return new CommonResult<>(200, "计算成功", goodsPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStorage(String code, Integer count) {
        int i = storageMapper.upStorage(code, count);
        int j = 10 / count;
        return i;
    }
}
