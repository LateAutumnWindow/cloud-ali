package com.yan.cloud.dao;

import com.yan.cloud.pojo.TDict;
import org.springframework.stereotype.Repository;

@Repository
public interface TDictDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TDict record);

    int insertSelective(TDict record);

    TDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDict record);

    int updateByPrimaryKey(TDict record);
}