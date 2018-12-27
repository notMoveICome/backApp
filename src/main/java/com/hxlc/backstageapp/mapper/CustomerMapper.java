package com.hxlc.backstageapp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hxlc.backstageapp.pojo.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 查找所用客户的详细信息
     */
    List<Customer> findAllCustomerInfo();
}
