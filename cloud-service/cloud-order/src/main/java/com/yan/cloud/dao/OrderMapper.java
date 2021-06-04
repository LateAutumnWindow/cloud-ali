package com.yan.cloud.dao;

import com.yan.cloud.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper {
    public int createOrder(Order order);
    public Order getOrderInfo(Long orderId);
}
