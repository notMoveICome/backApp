package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.Media;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaMapper extends BaseMapper<Media> {
    @Insert("insert into media_info(name,format,remark,project_id,type,url) " +
            "values(#{media.name},#{media.format},#{media.remark},#{media.projectId},#{media.type},#{media.url})")
    @Options(useGeneratedKeys = true, keyProperty = "media.gid", keyColumn = "gid")
    void addMedia(@Param("media") Media media);
}
