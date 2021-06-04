package com.yan.cloud.dao;

import com.yan.cloud.pojo.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Mapper
public interface StorageMapper {

    Storage getGoodsUnitPrice(String commodityCode);

    int dwindleNumbers(@Param("code") String code, @Param("count") Integer count);

    int upStorage(@Param("code") String code, @Param("count") Integer count);
}
