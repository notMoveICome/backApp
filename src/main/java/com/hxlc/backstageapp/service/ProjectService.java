package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    List<Project> findProjectList();

    List<Project> findProjectByName(String name);

    List<Media> getCapAndQueByProId(Integer projectId);

    List<Media> getProMediaByProId(Integer projectId);

    List<Map> getRecommendPro();

    List<Project> findProjectByProjectName(String projectName);

    Integer deleteProById(Integer proId);

    List<Project> queryRecommendPro();

    Integer addProjectRecomm(Integer proId);

    Integer editProjectInfo(Project project);

    void addProject(Project project, MultipartFile spjs, MultipartFile xswd, MultipartFile hxt, MultipartFile hxt1, MultipartFile other);
}
