package com.yan.cloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.yan.cloud.dao"})
public class MybatisConfig {
}
