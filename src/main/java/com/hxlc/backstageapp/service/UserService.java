package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface UserService {

    Object getUserByRole(String role);



    List<Customer> getCustomerInfoBySale(Integer saleId);
    User findUser(String username,String password);
    Object getCustomerInfoBySale(String customerName, Integer saleId);

    Integer addUser(String username, String password, String tel);

    User findUserByUsername(String username);

    User findUserByTel(String tel);

    Integer delUser(Integer gid);


    Integer changeState(Integer gid, String state);


    Integer updateUser(Integer gid, String username, String password, String tel);
}
