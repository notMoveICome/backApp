package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.AppFace;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.DistributorInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributorMapper extends BaseMapper<DistributorInfo> {

//    @Select("SELECT * FROM distributor_info WHERE dis_id = #{saleId}")
    @Select("SELECT di.*,ui.* " +
            "FROM distributor_info di ,user_info ui " +
            "WHERE di.dis_id = #{disId} AND ui.gid = di.dis_id")
    DistributorInfo queryDisByDisId(Integer disId);

    @Update("UPDATE distributor_info SET check_state = #{value} WHERE dis_id = #{disId}")
    Integer updateDisCkState(@Param("disId") Integer disId, @Param("value") String value);

    @Select("SELECT ui.tel FROM distributor_info di,user_info ui WHERE di.channel_comm = ui.gid AND di.dis_id=#{gid}")
    String getChTelById(Integer gid);

    List<Customer> getCusByCusIDs(@Param("disId") Integer disId, @Param("ids") Integer[] ids);
}
