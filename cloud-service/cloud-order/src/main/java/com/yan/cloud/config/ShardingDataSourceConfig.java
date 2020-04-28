package com.yan.cloud.config;

import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingDataSourceConfig {

    @Value("${spring.shardingsphere.datasource.names}")
    public String datasourceName;

    @Bean
    @Primary
    public DataSource getShardingDataSource() throws SQLException {
        return ShardingDataSourceFactory.createDataSource(
                    createDataSourceMap(),
                    createShardingRule(),
                new Properties());
    }

    public ShardingRuleConfiguration createShardingRule() {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        String[] names = datasourceName.split(",");
        shardingRuleConfig.setDefaultDataSourceName(names[0]);

        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
//        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
//        shardingRuleConfig.getBindingTableGroups().add("order");
//        shardingRuleConfig.getBroadcastTables().add("t_config");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("user_id",
                        "order-${user_id % 2}"));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(
                new StandardShardingStrategyConfiguration("commodity_id",
                        new ModuloShardingTableAlgorithm()));
        return shardingRuleConfig;
    }


    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "commodity_id");
        return result;
    }

    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("order", "order-${0..1}.order_${0..1}");
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        return result;
    }

//    TableRuleConfiguration getOrderItemTableRuleConfiguration() {
//        TableRuleConfiguration result = new TableRuleConfiguration("order", "ds${0..1}.t_order_item${0..1}");
//        return result;
//    }

    public Map<String, DataSource> createDataSourceMap() {
        Map<String,DataSource> map = new HashMap<>();
        String[] names = datasourceName.split(",");
        for (String name : names) {
            map.put(name, DataSourceUtil.createDataSource(name));
        }
        return map;
    }

}
