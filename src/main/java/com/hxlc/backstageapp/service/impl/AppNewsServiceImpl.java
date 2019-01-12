package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.AppNewsMapper;
import com.hxlc.backstageapp.pojo.AppNews;
import com.hxlc.backstageapp.service.AppNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class AppNewsServiceImpl implements AppNewsService{

    @Autowired
    private AppNewsMapper appNewsMapper;

    @Value("${fileDir.appNews}")
    private String appNews;

    @Override
    public List<AppNews> findNewsList() {
        return appNewsMapper.selectList(new EntityWrapper<AppNews>());
    }

    @Override
    public List<AppNews> queryNewsByTitle(String title) {
        return appNewsMapper.selectList(new EntityWrapper<AppNews>().like("title",title));
    }

    @Override
    public Integer addNews(AppNews news,MultipartFile pictureFile) {
        try {
            String picName = pictureFile.getOriginalFilename();
            InputStream is = pictureFile.getInputStream();
            String fileName = System.currentTimeMillis() + picName.substring(picName.lastIndexOf("."));
            FileOutputStream fos = new FileOutputStream(appNews + "/" + fileName);
            int length;
            byte[] b = new byte[1024];
            while ((length = is.read(b)) > -1){
                fos.write(b,0,length);
            }
            fos.close();
            is.close();
            news.setPicture(appNews.substring(appNews.lastIndexOf("/"),appNews.length()) + "/" + fileName);
            Integer row = appNewsMapper.insert(news);
            return row;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
