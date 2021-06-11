package com.yan.cloud.service.impl;

import com.yan.cloud.SnowflakeIdWorker;
import com.yan.cloud.api.StorageServerApi;
import com.yan.cloud.CommonResult;
import com.yan.cloud.api.AccountApi;
import com.yan.cloud.constant.MsgCode;
import com.yan.cloud.dao.OrderMapper;
import com.yan.cloud.exception.OrderExeption;
import com.yan.cloud.pojo.Order;
import com.yan.cloud.redisson.RedissonLockUtil;
import com.yan.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    SnowflakeIdWorker idUtil;
    @Autowired
    private OrderMapper orderMapper;
    @Resource
    private StorageServerApi storageServerApi;
    @Resource
    private AccountApi accountApi;

    @Override
    @Transactional
    @GlobalTransactional(timeoutMills = 300000000, name = "create-order")
    public CommonResult createOrder(String userId, String commodityCode, int count) {
        System.out.println(RootContext.getXID() + " ==  ===========================================");

        // Redisson联锁同时锁住用户ID,和物品Code
        RLock multiLock = RedissonLockUtil.getMultiLock(userId, commodityCode);
        try{
            multiLock.lock(30, TimeUnit.SECONDS);
            // 计算价格，扣除库存
            CommonResult goodsPrice = storageServerApi.getGoodsPrice(commodityCode, count);
            Integer countMoney = (Integer) goodsPrice.getData();
            Order build = Order.builder()
                    .orderId(idUtil.nextId())
                    .commodityCode(commodityCode)
                    .userId(Integer.parseInt(userId))
                    .count(count)
                    .money(countMoney)
                    .build();
            // 创建运单
            orderMapper.createOrder(build);
            // 账户扣钱
            accountApi.deductMoney(userId, countMoney);
            if (count > 99) {
                int i = 10 / 0;
            }
        } catch (Exception e) {
            log.info("创建订单失败", e);
            throw new OrderExeption(MsgCode.ADD_ORDER,"创建订单失败");
        } finally {
            multiLock.unlock();
        }
        return CommonResult.success("订单创建成功");
    }

    @Override
    public Order getOrderInfo(long id) {
        return orderMapper.getOrderInfo(id);
    }
}
