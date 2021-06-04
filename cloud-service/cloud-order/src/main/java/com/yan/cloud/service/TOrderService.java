package com.yan.cloud.service;


import com.yan.cloud.pojo.Order;
import com.yan.cloud.pojo.TOrder;

public interface TOrderService {

    public int insertOrder(Order order);

    public Order getOrderInfo(long id);

}
