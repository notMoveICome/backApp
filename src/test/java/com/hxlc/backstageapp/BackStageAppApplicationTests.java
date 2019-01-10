package com.hxlc.backstageapp;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.pojo.Media;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.event.FocusEvent;
import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackStageAppApplicationTests {

	@Autowired
	private MediaMapper mediaMapper;

	@Test
	public void contextLoads() {
	}

	@Test
	public void string1() {
		String s = "aaaa" + "\r\n" + "bbb";
		System.out.println(s);
	}

	@Test
	public void updateFile() {
		String path = "E:\\其他工具\\QQFile\\759645275\\FileRecv\\楼盘信息\\长城汇项目资料";
		File file = new File(path);
		File[] files = file.listFiles();
		List<Media> mediaList = mediaMapper.selectList(new EntityWrapper<Media>().eq("project_id", 18));
		for (int m = 0;m < mediaList.size();m++){
			Media media = mediaList.get(m);
			for (int i = 0;i < files.length;i++){
				String fileName = files[i].getName();
				String strings = fileName.substring(0,fileName.lastIndexOf("."));
				if (strings.equals(media.getName())){
					Media med = new Media();
					med.setGid(media.getGid());
					med.setUrl("/resources/CCH/" + fileName);
					mediaMapper.updateById(med);
				}
			}
		}
	}

//	public void rsr
}

