package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hxlc.backstageapp.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMapper<User>{


    List<Map> selectAllUser();

    List<User> findUserByCondition(@Param("username") String username,@Param("usertel") String usertel,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);


    User selectUser(@Param("tel")String tel,@Param("pwd") String pwd);

    @Insert("INSERT INTO user_info(name,password,tel,role_id,state,create_time) " +
            "VALUES(#{user.name},#{user.password},#{user.tel},#{user.roleId},#{user.state},#{user.createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "user.gid",keyColumn = "gid")
    Integer addUser(@Param("user") User user);

}
