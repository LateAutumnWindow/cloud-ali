package com.yan.cloud.service;


import com.yan.cloud.pojo.TOrder;

public interface TOrderService {

    public int insertOrder(TOrder order);

    public TOrder getOrderInfo(long id);

}
