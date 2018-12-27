package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
