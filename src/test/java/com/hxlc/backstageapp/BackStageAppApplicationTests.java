package com.hxlc.backstageapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackStageAppApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void string1() {
		String s = "aaaa" + "\r\n" + "bbb";
		System.out.println(s);
	}

}

