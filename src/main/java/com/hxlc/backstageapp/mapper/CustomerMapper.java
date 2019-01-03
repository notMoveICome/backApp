package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.Customer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 查找所用客户的详细信息
     * @return
     */
    List<Customer> findAllCustomerInfo();

    /**
     * 根据客户名字与业务员ID查找客户的相关信息
     * @param saleId
     * @return
     */
    List<Customer> findCustomerInfoBySale(@Param("saleId") Integer saleId);


    List<Customer> findCustomerByCondition(@Param("username")String username,
                                           @Param("proname")String proname,
                                           @Param("usertel") String usertel,
                                           @Param("beginDate") Date beginDate,
                                           @Param("endDate") Date endDate);
}
