package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;

import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Security;


@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/userLogin")
    public SysObject login(String username, String password, HttpSession session, HttpServletResponse response) {
        User user = userService.findUser(username, password);
        if (user != null) {
           if((user.getRoleId()==2||user.getRoleId()==4)){
               if (("正常").equals(user.getState())){
                   user.setPassword(null);
                   session.setAttribute("user",user);
                   Cookie cookie1 = new Cookie("iframe_user",user.getName());
                   Cookie cookie2 = new Cookie("iframe_userRole",user.getRoleId().toString());
                   cookie1.setPath("/backApp");
                   cookie1.setMaxAge(60*60*24*7);
                   cookie2.setPath("/backApp");
                   cookie2.setMaxAge(60*60*24*7);
                   response.addCookie(cookie1);
                   response.addCookie(cookie2);
                   return new SysObject(200, "登录成功", user);
               }else {
                   return new SysObject(201, "此用户已停用!", null);
               }
           }else{
               return new SysObject(201, "此用户不存在!", null);
           }
        }
        return new SysObject(201, "账号或密码错误", null);
    }

    @RequestMapping("/signOut")
    public void singOut(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie1 = new Cookie("iframe_user","");
        Cookie cookie2 = new Cookie("iframe_userRole","");
        cookie1.setPath("/backApp");
        cookie1.setMaxAge(0);
        cookie2.setPath("/backApp");
        cookie2.setMaxAge(0);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
    };
}
