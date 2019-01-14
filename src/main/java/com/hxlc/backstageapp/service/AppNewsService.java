package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.AppNews;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppNewsService {

    List<AppNews> findNewsList();

    List<AppNews> queryNewsByTitle(String title);

    Integer addNews(AppNews appNews,MultipartFile pictureFile);

    Integer deleteNewsById(Integer newsId);

    Integer editNews(AppNews appNews, MultipartFile pictureFile);

    List<AppNews> getRecentNews();
}
