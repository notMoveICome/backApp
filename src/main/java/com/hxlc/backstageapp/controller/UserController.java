package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserByRole")
    public SysObject getUserByRole(String role) {
        return new SysObject(userService.getUserByRole(role));
    }

    @RequestMapping("/getCustomerInfoBySale")
    public  SysObject getCustomerInfoBySale(Integer saleId){
        return new SysObject(userService.getCustomerInfoBySale(saleId));
    }

    @RequestMapping("/addUser")
    public SysObject addUser(String username, String password, String tel) {
        //判断用户名和手机号是否存在
        User user = userService.findUserByUsername(username);
        User user1 = userService.findUserByTel(tel);
        if (user != null) {
            return new SysObject(201, "用户名已存在", null);
        }
        if(user1!=null){
            return new SysObject(201, "该手机号已存在", null);
        }
        Integer result = userService.addUser(username, password, tel);
        System.out.println(result);
        if (result > 0) {
            return new SysObject(200, "添加成功", null);
        }
        return new SysObject(201, "添加失败", null);
    }

    @RequestMapping("/delUser")
    public SysObject delUser(Integer gid){
        Integer result = userService.delUser(gid);
        if(result>0){
            return new SysObject(200, "删除用户成功", null);
        }
        return new SysObject(201, "删除失败", null);
    }

    @RequestMapping("/changeState")
    public SysObject changeState(Integer gid,String state){
        Integer result = userService.changeState(gid, state);
        if(result>0){
            return new SysObject(200, "修改状态成功", null);
        }
        return new SysObject(201, "修改状态失败", null);
    }
}
