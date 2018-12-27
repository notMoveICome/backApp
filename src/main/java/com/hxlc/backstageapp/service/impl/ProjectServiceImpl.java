package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<Project> findProjectList() {
        return projectMapper.selectList(new EntityWrapper<Project>());
    }

    @Override
    public List<Project> findProjectByName(String name) {
        return projectMapper.selectList(new EntityWrapper<Project>().like("name",name));
    }
}
