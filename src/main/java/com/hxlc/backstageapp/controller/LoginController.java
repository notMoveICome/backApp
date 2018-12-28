package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;

import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Security;


@RestController
@RequestMapping()
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/userLogin")
    public SysObject login(String username, String password, HttpSession session) {
        User user = userService.findUser(username, password);
        if (user != null) {
           if((user.getRoleId()==2||user.getRoleId()==4)&&("正常").equals(user.getState())){
               session.setAttribute("user",user);
               return new SysObject(200, "登录成功", user);
           }else{
               return new SysObject(201, "账户权限不足", null);
           }
        }
        return new SysObject(201, "账号或密码错误", null);
    }

    @RequestMapping("/signOut")
    public void singOut(HttpSession session) {
        session.invalidate();
    };
}
