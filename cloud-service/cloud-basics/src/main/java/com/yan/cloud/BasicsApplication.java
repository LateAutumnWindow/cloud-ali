package com.yan.cloud;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.yan.cloud.dao"})
@SpringBootApplication(exclude = {
        MybatisPlusAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class
})
public class BasicsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicsApplication.class, args);
    }
}