package com.yan.cloud.service.impl;

import com.yan.cloud.api.StorageServerApi;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.yan.cloud.CommonResult;
import com.yan.cloud.api.AccountApi;
import com.yan.cloud.dao.OrderMapper;
import com.yan.cloud.pojo.Order;
import com.yan.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Resource
    private StorageServerApi storageServerApi;
    @Resource
    private AccountApi accountApi;

    @Override
    @GlobalTransactional
    public CommonResult createOrder(String userId, String commodityCode, int count) {
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
        int i = 10 / count;
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        // 计算价格，扣除库存
        CommonResult goodsPrice = storageServerApi.getGoodsPrice(commodityCode, count);
        Integer countMoney = (Integer) goodsPrice.getData();
        Order build = Order.builder()
                .commodityCode(snowflake.nextIdStr())
                .userId(userId)
                .count(count)
                .money(countMoney)
                .build();
        // 创建运单
        int order = orderMapper.createOrder(build);
        // 账户扣钱
        accountApi.deductMoney(userId, countMoney);
        return new CommonResult<>(200, "订单创建成功", order);
    }
}
