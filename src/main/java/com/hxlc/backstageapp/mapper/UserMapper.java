package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User>{


    List<User> selectAllUser();

    List<User> findUserByCondition(@Param("username") String username,@Param("usertel") String usertel,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);
}
