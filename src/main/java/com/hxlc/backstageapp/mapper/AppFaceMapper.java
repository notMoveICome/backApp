package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.AppFace;
import com.hxlc.backstageapp.pojo.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppFaceMapper extends BaseMapper<AppFace> {

    @Update("UPDATE app_face SET index = index - 1 WHERE index > #{index}")
    void updateIndexUp(Integer index);

    @Update("UPDATE app_face SET index = index + 1 WHERE index > 0 AND index < #{index}")
    void updateIndexDown(Integer index);

    @Update("UPDATE app_face SET ad_index = ad_index - 1 WHERE ad_index > #{adIndex}")
    Integer updateAdIndexUp(Integer adIndex);

    @Update("UPDATE app_face SET ad_index = #{val} WHERE gid = #{gid}")
    Integer updateAdIndex(@Param("val") Integer val,@Param("gid") Integer gid);

    @Select("SELECT COUNT(*) FROM app_face where ad_index IS NOT NULL")
    Integer selectRecomAD();
}
