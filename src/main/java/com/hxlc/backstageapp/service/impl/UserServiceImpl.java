package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.UserMapper;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Object getUserByRole(String role) {
         if ("客户".equals(role)){
             List<Customer> customerList = customerMapper.findAllCustomerInfo();
             return customerList;
         }else {
             Integer roleId;
             if ("管理员".equals(role)){
                 roleId = 3;
             }else if ("分销商".equals(role)){
                 roleId = 2;
             }else {
                 roleId = 1;
             }
             return userMapper.selectList(new EntityWrapper<User>().eq("role_id",roleId));
         }
    }

    @Override
    public List<Customer> getCustomerInfoBySale(Integer saleId) {
        return customerMapper.findCustomerInfoBySale(saleId);
    }

    @Override
    public User findUser(String username, String password) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }


}
