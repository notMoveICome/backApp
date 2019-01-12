package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectMapper extends BaseMapper<Project> {
    /**
     * 根据项目ID查出楼盘解说与销售问答的文档
     * @param projectId
     * @return
     */
    List<Media> findCapAndQueByProId(Integer projectId);

    /**
     * 获取首页的推荐项目
     * @return
     */
    List<Map> findRecommendPro();

    Integer addProject(@Param("project") Project project);

    List<Project> getAllProject();

    List<Project> findProjectByProjectName(@Param("projectName") String projectName);

    Project selectProById(@Param("proId") Integer proId);
}
