package com.hxlc.backstageapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.hxlc.backstageapp.mapper")	//扫描Mybatis接口文件
public class BackStageAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackStageAppApplication.class, args);
	}

}

