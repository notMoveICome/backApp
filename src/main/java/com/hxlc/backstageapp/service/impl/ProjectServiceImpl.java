package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.AppFaceMapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.AppFace;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private AppFaceMapper appFaceMapper;

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

    @Override
    public List<Project> queryRecommendPro() {
        List<AppFace> faces = appFaceMapper.selectList(new EntityWrapper<AppFace>());
        List<Project> projects = projectMapper.selectList(new EntityWrapper<Project>());
        for (int i = 0;i < faces.size();i++){
            for (int j = 0;j < projects.size();j++){
                if (faces.get(i).getProjectId() == projects.get(j).getGid()){
                    projects.remove(j);
                }
            }
        }
        return projects;
    }

    @Override
    public Integer addProjectRecomm(Integer proId) {
        Project project = new Project();
        project.setGid(proId);
        Project pro = projectMapper.selectOne(project);
        Integer count = appFaceMapper.selectCount(new EntityWrapper<AppFace>());
        AppFace appFace = new AppFace();
        appFace.setProjectId(proId);
        appFace.setIndex(count + 1);
        appFace.setPrice(pro.getPrice() / 1000); // 待定
        appFace.setPublishTime("6月-12月");// 待定
        Integer row = appFaceMapper.insert(appFace);
        return row;
    }
}
















