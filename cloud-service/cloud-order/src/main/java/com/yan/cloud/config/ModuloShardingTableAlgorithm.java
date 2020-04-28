package com.yan.cloud.config;

import com.alibaba.fastjson.JSON;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class ModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        System.out.println("collection: " + JSON.toJSONString(collection) );
        System.out.println("preciseShardingValue: " + JSON.toJSONString(preciseShardingValue) );
        // collection:["db0","db1","db2"]
        // preciseShardingValue:{"columnName":"order_id","logicTableName":"t_order_items","value":100}
        for (String dbname : collection) {
            if (preciseShardingValue.getValue() % 2 == 0) {
                return dbname;
            }
        }
        return null;
    }
}
