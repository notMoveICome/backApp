package com.hxlc.backstageapp;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.pojo.Media;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Test
	public void unRarFile(){
		String srcRarPath = "E:/Chrome-DownLoad/纯js分页插件.rar";
		String dstDirectoryPath = "E:/Chrome-DownLoad/d";
		if (!srcRarPath.toLowerCase().endsWith(".rar")) {
			System.out.println("非rar文件！");
			return;
		}
		File dstDiretory = new File(dstDirectoryPath);
		if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
			dstDiretory.mkdirs();
		}
		Archive a = null;
		try {
			a = new Archive(new File(srcRarPath));
			if (a != null) {
				a.getMainHeader().print(); // 打印文件信息.
				FileHeader fh = a.nextFileHeader();
				while (fh != null) {
					// 判断是否有中文
					String path = fh.getFileNameW().trim();
					if (!existZH(path)){
						path = fh.getFileNameW();
					}
					if (fh.isDirectory()) { // 文件夹
						File fol = new File(dstDirectoryPath + File.separator
								+ path);
						fol.mkdirs();
					} else { // 文件
						File out = new File(dstDirectoryPath + File.separator
								+ path);
						try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
							if (!out.exists()) {
								if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
									out.getParentFile().mkdirs();
								}
								out.createNewFile();
							}
							FileOutputStream os = new FileOutputStream(out);
							a.extractFile(fh, os);
							os.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					fh = a.nextFileHeader();
				}
				a.close();
				System.out.println("rar压缩文件解压成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean existZH(String str) {
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			return true;
		}
		return false;
	}
}

