package com.yan.cloud.service.impl;

import com.yan.cloud.CommonResult;
import com.yan.cloud.dao.AccountMapper;
import com.yan.cloud.service.AccountService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    @Transactional
    public CommonResult deductMoney(String userId, Integer money) {
        System.out.println(RootContext.getXID() + " ==  ===========================================");
        accountMapper.deductMoney(userId, money);
        int i = 10 / Integer.parseInt(userId);
        return new CommonResult(200, "扣钱成功");
    }
}
