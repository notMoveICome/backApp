package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private MediaMapper mediaMapper;
    @Override
    public List<Project> findProjectList() {
        return projectMapper.selectList(new EntityWrapper<Project>());
    }

    @Override
    public List<Project> findProjectByName(String name) {
        return projectMapper.selectList(new EntityWrapper<Project>().like("name",name));
    }

    @Override
    public List<Media> getCapAndQueByProId(Integer projectId) {
        return projectMapper.findCapAndQueByProId(projectId);
    }

    @Override
    public List<Media> getProMediaByProId(Integer projectId) {
        return mediaMapper.selectList(new EntityWrapper<Media>().eq("project_id",projectId));
    }

    @Override
    public List<Map> getRecommendPro() {
        return projectMapper.findRecommendPro();
    }
}
