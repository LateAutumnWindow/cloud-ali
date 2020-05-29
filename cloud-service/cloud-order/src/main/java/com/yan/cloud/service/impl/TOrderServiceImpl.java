package com.yan.cloud.service.impl;

import com.yan.cloud.api.StorageServerApi;
import com.yan.cloud.dao.TOrderDAO;
import com.yan.cloud.pojo.TOrder;
import com.yan.cloud.service.TOrderService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TOrderServiceImpl implements TOrderService {

    @Autowired
    private TOrderDAO orderDAO;

    @Resource
    private StorageServerApi storageServerApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.BASE)
    public int insertOrder(TOrder order) {
        int i = orderDAO.insertSelective(order);
        String status = order.getStatus();
        String[] split = status.split(",");
        storageServerApi.getGoodsPrice(split[0], Integer.parseInt(split[1]));
        return i;
    }

    @Override
    public TOrder getOrderInfo(long id) {
        TOrder tOrder = orderDAO.selectByPrimaryKey(id);
        return tOrder;
    }
}
