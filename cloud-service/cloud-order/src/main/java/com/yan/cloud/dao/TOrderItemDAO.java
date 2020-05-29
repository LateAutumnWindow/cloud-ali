package com.yan.cloud.dao;

import com.yan.cloud.pojo.TOrderItem;
import org.springframework.stereotype.Repository;

@Repository
public interface TOrderItemDAO {
    int deleteByPrimaryKey(Long id);

    int insert(TOrderItem record);

    int insertSelective(TOrderItem record);

    TOrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TOrderItem record);

    int updateByPrimaryKey(TOrderItem record);
}