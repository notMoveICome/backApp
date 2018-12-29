package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMapper extends BaseMapper<Project> {
    /**
     * 根据项目ID查出楼盘解说与销售问答的文档
     * @param projectId
     * @return
     */
    List<Media> findCapAndQueByProId(Integer projectId);
}
