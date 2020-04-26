package com.yan.cloud.dao;

import com.yan.cloud.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    public int createOrder(Order order);
}
