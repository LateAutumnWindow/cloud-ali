package com.yan.cloud.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StorageMapper {

    Integer getGoodsUnitPrice(String commodityCode);

    int dwindleNumbers(@Param("code") String code, @Param("count") Integer count);

    int upStorage(@Param("code") String code, @Param("count") Integer count);
}
