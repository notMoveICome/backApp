package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.DistributorInfo;
import com.hxlc.backstageapp.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DistributorController {
    @Autowired
    private DistributorService distributorService;
    @RequestMapping("/findPicUrl")
    public SysObject findPicUrl(Integer disId){
        String result = distributorService.findUrlById(disId);
        if(result!=null){
             return new SysObject(200,result,null);
        }
        return new SysObject(201,"未找到审核的图片",null);
    }
}
