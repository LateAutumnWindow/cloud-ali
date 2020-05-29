package com.yan.cloud.service.impl;

import com.yan.cloud.api.StorageServerApi;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.yan.cloud.CommonResult;
import com.yan.cloud.api.AccountApi;
import com.yan.cloud.dao.OrderMapper;
import com.yan.cloud.pojo.Order;
import com.yan.cloud.redisson.RedissonLockUtil;
import com.yan.cloud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Resource
    private StorageServerApi storageServerApi;
    @Resource
    private AccountApi accountApi;

    @Override
    public CommonResult createOrder(String userId, String commodityCode, int count) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        // Redisson联锁同时锁住用户ID,和物品Code
        RLock multiLock = RedissonLockUtil.getMultiLock(userId, commodityCode);
        try{
            multiLock.lock(2, TimeUnit.SECONDS);
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
            orderMapper.createOrder(build);
            // 账户扣钱
            accountApi.deductMoney(userId, countMoney);
            int i = 10 / count;
        } catch (Exception e) {
            log.info("创建订单失败", e);
        } finally {
            multiLock.unlock();
        }
        return new CommonResult<>(200, "订单创建成功");
    }
}
