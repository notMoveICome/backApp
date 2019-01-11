package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.Media;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaMapper extends BaseMapper<Media> {
    @Insert("INSERT INTO media_info(name,format,remark,project_id,type,url) " +
            "VALUES(#{media.name},#{media.format},#{media.remark},#{media.projectId},#{media.type},#{media.url})")
    @Options(useGeneratedKeys = true, keyProperty = "media.gid", keyColumn = "gid")
    void addMedia(@Param("media") Media media);

    @Select("SELECT url FROM media_info WHERE project_id = #{proId} ORDER BY gid LIMIT 1")
    String selectUrlByProId(Integer proId);
}
