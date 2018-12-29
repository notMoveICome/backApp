package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.User;

import java.util.List;

public interface UserService {

    Object getUserByRole(String role);

    User findUser(String username, String password);

    List<Customer> getCustomerInfoBySale(String customerName, Integer saleId);
}
