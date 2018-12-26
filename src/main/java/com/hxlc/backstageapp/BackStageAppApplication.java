package com.hxlc.backstageapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hxlc.backstageapp.mapper")	//扫描Mybatis接口文件
public class BackStageAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackStageAppApplication.class, args);
	}

}

