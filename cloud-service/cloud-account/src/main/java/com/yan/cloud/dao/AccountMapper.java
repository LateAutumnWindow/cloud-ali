package com.yan.cloud.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    int deductMoney(@Param("userId") String userId,@Param("money") Integer money);

    Integer getUserInfo(@Param("userId") String userId);
}
