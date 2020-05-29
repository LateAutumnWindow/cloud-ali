package com.yan.cloud.dao;

import com.yan.cloud.pojo.TOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface TOrderDAO {
    int deleteByPrimaryKey(Long id);

    int insert(TOrder record);

    int insertSelective(TOrder record);

    TOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TOrder record);

    int updateByPrimaryKey(TOrder record);
}