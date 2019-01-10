package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.AppFace;
import com.hxlc.backstageapp.pojo.DistributorInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorMapper extends BaseMapper<DistributorInfo> {
    @Select("SELECT * FROM distributor_info WHERE dis_id = #{saleId}")
    DistributorInfo queryDisByDisId(Integer disId);
}
