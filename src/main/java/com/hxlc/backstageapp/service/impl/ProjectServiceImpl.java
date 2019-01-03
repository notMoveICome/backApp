package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Customer;
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
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Project> findProjectList() {
        List<Project> projectList = projectMapper.selectList(new EntityWrapper<Project>());
        for (int i = 0;i < projectList.size();i++){
            projectList.get(i).setDisnum(projectList.get(i).getDisnum() + 80);//分销商数技术80
            Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("project_id", projectList.get(i).getGid()));
            projectList.get(i).setReportNum(count);
        }
        return projectList;
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

    @Override
    public List<Project> findProjectByProjectName(String projectName) {
        List<Project> projectList = projectMapper.selectList(new EntityWrapper<Project>().like("name", projectName));
        for (int i = 0;i < projectList.size();i++){
            projectList.get(i).setDisnum(projectList.get(i).getDisnum() + 80);//分销商数技术80
            Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("project_id", projectList.get(i).getGid()));
            projectList.get(i).setReportNum(count);
        }
        return projectList;
    }

    @Override
    public Integer deleteProById(Integer proId) {
        return projectMapper.delete(new EntityWrapper<Project>().eq("gid",proId));
    }
}
