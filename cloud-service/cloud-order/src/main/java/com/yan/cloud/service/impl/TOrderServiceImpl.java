package com.yan.cloud.service.impl;

import com.yan.cloud.dao.OrderMapper;
import com.yan.cloud.dao.TOrderDAO;
import com.yan.cloud.pojo.Order;
import com.yan.cloud.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TOrderServiceImpl implements TOrderService {

    @Autowired
    private TOrderDAO orderDAO;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertOrder(Order order) {
        orderMapper.createOrder(order);
        return 0;
    }

    @Override
    public Order getOrderInfo(long id) {
        Order orderInfo = orderMapper.getOrderInfo(id);
        return orderInfo;
    }
}
