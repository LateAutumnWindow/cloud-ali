package com.yan.cloud.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StorageMapper {

    Integer getGoodsUnitPrice(String commodityCode);

    int dwindleNumbers(@Param("code") String code, @Param("count") Integer count);
}
