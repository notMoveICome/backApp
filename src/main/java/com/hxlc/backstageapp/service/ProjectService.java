package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findProjectList();

    List<Project> findProjectByName(String name);
}
