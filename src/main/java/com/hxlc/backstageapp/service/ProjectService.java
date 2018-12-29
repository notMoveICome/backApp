package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    List<Project> findProjectList();

    List<Project> findProjectByName(String name);

    List<Media> getCapAndQueByProId(Integer projectId);

    List<Media> getProMediaByProId(Integer projectId);

    List<Map> getRecommendPro();
}
