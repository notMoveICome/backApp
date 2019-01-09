package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserByRole")
    public SysObject getUserByRole(String role) {
        return new SysObject(userService.getUserByRole(role));
    }

    @RequestMapping("/getCustomerInfoBySaleId")
    public SysObject getCustomerInfoBySale(Integer saleId) {
        return new SysObject(userService.getCustomerInfoBySale(saleId));
    }

    @RequestMapping("/addUser")
    public SysObject addUser(String username, String password, String tel, String role) {
        //判断用户名和手机号是否存在
        User user = userService.findUserByUsername(username);
        User user1 = userService.findUserByTel(tel);
        if (user != null) {
            return new SysObject(201, "用户名已存在", null);
        }
        if (user1 != null) {
            return new SysObject(201, "该手机号已存在", null);
        }
        Integer result = userService.addUser(username, password, tel, role);
        System.out.println(result);
        if (result > 0) {
            return new SysObject(200, "添加成功", null);
        }
        return new SysObject(201, "添加失败", null);
    }

    @RequestMapping("/delUser")
    public SysObject delUser(Integer gid) {
        Integer result = userService.delUser(gid);
        if (result > 0) {
            return new SysObject(200, "删除用户成功", null);
        }
        return new SysObject(201, "删除失败", null);
    }

    @RequestMapping("/changeState")
    public SysObject changeState(Integer gid, String state) {
        Integer result = userService.changeState(gid, state);
        if (result > 0) {
            return new SysObject(200, "修改状态成功", null);
        }
        return new SysObject(201, "修改状态失败", null);
    }

    @RequestMapping("/updateUser")
    public SysObject updateUser(Integer gid, String username, String password, String tel) {
        Integer result = userService.updateUser(gid, username, password, tel);
        if (result > 0) {
            return new SysObject(200, "修改用户成功", null);
        }
        return new SysObject(201, "修改用户失败", null);
    }

    @RequestMapping("/findUser")
    public SysObject findUser(@RequestParam Map map) throws ParseException {
        List<Object> list = (List<Object>) userService.findUserByCondition(map);
        if (list == null || list.size() == 0) {
            return new SysObject(201, "查询失败", null);
        }
        return new SysObject(userService.findUserByCondition(map));
    }

    @RequestMapping("/findCustomer")
    public SysObject findCustomer(@RequestParam Map map) throws ParseException {
        List<Customer> list = userService.findCustomerByCondition(map);
        if (list == null || list.size() == 0) {
            return new SysObject(201, "查询失败", null);
        }
        return new SysObject(userService.findCustomerByCondition(map));
    }

    @RequestMapping("/validateTel")
    public SysObject validateTel(String tel) {
        Integer rows = userService.validateTel(tel);
        if (rows == 0) {
            return new SysObject(200, "该手机号可以使用!", null);
        } else {
            return new SysObject(201, "该手机号已被使用!", null);
        }
    }

    /**
     * 分销商注册
     * @param map
     * @return
     */
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public SysObject registerUser(@RequestParam Map map) {
        Integer rows = userService.registerUser(map);
        if (rows == 1) {
            return new SysObject(200, "注册成功!", null);
        } else {
            return new SysObject(201, "注册失败!", null);
        }
    }

    /**
     * 批量导入用户
     * @param dis      分销商
     * @param cusExcel Excel文件
     * @return
     */
    @RequestMapping(value = "/batchExportCus", method = RequestMethod.POST)
    public SysObject batchExportCus(String dis, @RequestParam(value = "cusExcel") MultipartFile cusExcel) {
        try {
            Map<String, Object> map = userService.batchExportCus(dis, cusExcel);
            if (map != null) {
                return new SysObject(200, "报备成功!", map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "报备异常,请检查Excel文件!", null);
    }

    /**
     * 上传营业执照
     * @param licensePic 照片
     * @param company   公司名称
     * @param linkman   联系人
     * @param linkTel   联系电话
     * @return
     */
    @RequestMapping(value = "/uploadLicense",method = RequestMethod.POST)
    public SysObject uploadLicense(@RequestParam(value = "licensePic") MultipartFile licensePic,String company,String linkman,Integer linkTel) {
        String picName = licensePic.getOriginalFilename();
        System.out.println(picName);
        System.out.println(company);
        System.out.println(linkman);
        System.out.println(linkTel);
        return null;
    }

    /**
     * 分销商报备客户
     * @param saleId        分销商ID
     * @param name          客户名字
     * @param tel           客户电话
     * @param projectName   项目名称
     * @param cusArea       客户区域
     * @param acreage       意向面积
     * @param money         投资额
     * @param remark        客户备注
     * @return
     */
    @RequestMapping(value = "/reportCustomer",method = RequestMethod.POST)
    public SysObject reportCustomer(Customer customer){
        try {
            boolean flag = userService.checkDistributorState(customer.getSaleId());
            if (flag){
                Integer row = userService.reportCustomer(customer);
                if (row == -1){
                    return new SysObject(201,"该客户报备该项目已超过报备次数!",null);
                }else {
                    return new SysObject(200,"报备成功!",null);
                }

            }
            return new SysObject(201,"权限不足,请先完成营业许可证审核!",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201,"报备异常!",null);
    }
}
