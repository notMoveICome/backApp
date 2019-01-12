package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.AppNews;
import com.hxlc.backstageapp.service.AppNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private AppNewsService appNewsService;

    @RequestMapping("/newsList")
    public SysObject newsList(){
        List<AppNews> list = appNewsService.findNewsList();
        return SysObject.ok(list);
    }

    @RequestMapping("/queryNewsByTitle")
    public SysObject queryNewsByTitle(String title){
        List<AppNews> list = appNewsService.queryNewsByTitle(title);
        return SysObject.ok(list);
    }

    /**
     * 添加头条
     * @param appNews
     * @return
     */
    @RequestMapping(value = "/addNews",method = RequestMethod.POST)
    public SysObject addNews(AppNews appNews,@RequestParam(value = "pictureFile") MultipartFile pictureFile){
        try {
            Integer row = appNewsService.addNews(appNews,pictureFile);
            if (row > 0){
                return SysObject.build(200,"添加成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(201,"服务器异常!");
    }
}
