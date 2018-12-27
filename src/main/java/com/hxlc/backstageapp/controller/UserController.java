package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserByRole")
    public SysObject getUserByRole(String role){
        return new SysObject(userService.getUserByRole(role));
    }
}
