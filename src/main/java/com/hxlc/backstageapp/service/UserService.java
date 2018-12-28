package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.User;

import java.util.List;

public interface UserService {

    Object getUserByRole(String role);

    User findUser(String username,String password);
    Object getCustomerInfoBySale(String customerName, Integer saleId);
}
