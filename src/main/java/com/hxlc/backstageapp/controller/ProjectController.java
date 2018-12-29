package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/getAllProject")
    public SysObject getAllProject(){
        List<Project> list = projectService.findProjectList();
        return new SysObject(list);
    }

    @RequestMapping("/getProjectByName")
    public SysObject getProjectByName(String name){
        List<Project> list = projectService.findProjectByName(name);
        return new SysObject(list);
    }

    /**
     * 根据项目ID得到楼盘解说与销售问答PDF文件
     * @param projectId
     * @return
     */
    @RequestMapping("/getCapAndQueByProId")
    public SysObject getCapAndQueByProId(Integer projectId){
        List<Media> list = projectService.getCapAndQueByProId(projectId);
        return SysObject.ok(list);
    }

    /**
     * 根据项目ID得到所有与项目相关的资料
     * @param projectId
     * @return
     */
    @RequestMapping("/getProMediaByProId")
    public SysObject getProMediaByProId(Integer projectId){
        List<Media> list = projectService.getProMediaByProId(projectId);
        return SysObject.ok(list);
    }

    /**
     * 首页推荐
     * @return
     */
    @RequestMapping("/getRecommendPro")
    public SysObject  getRecommendPro(){
        return SysObject.ok(projectService.getRecommendPro());
    }
}
