package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {

    @RequestMapping("/newsList")
    public SysObject newsList(){
        return null;
    }
}
