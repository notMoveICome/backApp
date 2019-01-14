package com.hxlc.backstageapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement	// 开启事务管理
@SpringBootApplication
@MapperScan("com.hxlc.backstageapp.mapper")	//扫描Mybatis接口文件

public class BackStageAppApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BackStageAppApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(BackStageAppApplication.class, args);
	}

}

