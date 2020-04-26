package com.yan.cloud.service;

import com.yan.cloud.CommonResult;
import org.apache.ibatis.annotations.Param;

public interface AccountService {
    CommonResult deductMoney(String userId, Integer money);
}
