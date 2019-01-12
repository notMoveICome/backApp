package com.hxlc.backstageapp;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.github.pagehelper.PageRowBounds;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.util.WordUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.templateparser.reader.PrototypeOnlyCommentMarkupReader;

import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackStageAppApplicationTests {

	@Autowired
	private MediaMapper mediaMapper;
	@Autowired
	private ProjectMapper projectMapper;

	@Value("${fileDir.projectFile}")
	private String projectFile;
	@Value("${fileDir.disLicense}")
	private String disLicense;

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
		String path = "E:\\ifamily\\prodata\\联投半岛\\项目介绍";
		path = path.replaceAll("\\\\","/");
		String proName = path.substring(path.indexOf("p"));
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0;i < files.length;i++){
			String fileName = files[i].getName();
			fileName = fileName.replaceAll("\\\\","/");
			// doc转pdf
			if (fileName.endsWith(".doc") || fileName.endsWith("docx")){
				WordUtil.wordSaveAs(path + File.separator + fileName,path + File.separator + fileName.substring(0,fileName.lastIndexOf(".")) + ".pdf");
				String name = fileName.substring(0,fileName.lastIndexOf("."));
				String format = "pdf";
//				String type = "沙盘说辞";
//				String type = "销售问答";
				String type = "其他文件";
				String url = "/" + proName + "/" + fileName.substring(0,fileName.lastIndexOf(".")) + ".pdf";
				Media media = new Media();
				media.setName(name);
				media.setFormat(format);
				media.setProjectId(118);
				media.setType(type);
				media.setUrl(url);
				mediaMapper.addMedia(media);
			}
			String name = fileName.substring(0,fileName.lastIndexOf("."));
			String format = fileName.substring(fileName.lastIndexOf(".") + 1);
//			String type = "户型图";
//			String type = "效果图";
//			String type = "图标";
//			String type = "沙盘说辞";
//			String type = "销售问答";
			String type = "其他文件";
			String url = "/" + proName + "/" + fileName;
			Media media = new Media();
			media.setName(name);
			media.setFormat(format);
			media.setProjectId(118);
			media.setType(type);
			media.setUrl(url);
			mediaMapper.addMedia(media);
		}

//		Project project = new Project();
//		project.setGid(103);
//		project.setDescPic();
	}

	@Test
	public void test3(){
		try {
			List<Project> list = projectMapper.selectList(new EntityWrapper<Project>());
			for (int i = 0;i < list.size();i++){
                Media media = new Media();
                media.setProjectId(list.get(i).getGid());
                media.setType("图标");
                Media one = mediaMapper.selectOne(media);
//                File file = new File(projectFile.substring(0,projectFile.lastIndexOf("/")) + one.getUrl());
                FileInputStream fis = new FileInputStream(projectFile.substring(0,projectFile.lastIndexOf("/")) + one.getUrl());
                int n = fis.available(); // 得到文件大小
                byte[] data = new byte[n];
                fis.read(data); //读数据
                fis.close();

                Project project = new Project();
                project.setGid(list.get(i).getGid());
                project.setDescPic(data);
				projectMapper.updateById(project);
            }
		} catch (IOException e) {
			e.printStackTrace();
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

