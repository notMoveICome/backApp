package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.DistributorInfo;
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
        return new SysObject(list);
    }

    @RequestMapping("/findCustomer")
    public SysObject findCustomer(@RequestParam Map map) throws ParseException {
        List<Customer> list = userService.findCustomerByCondition(map);
        if (list == null || list.size() == 0) {
            return new SysObject(201, "查询失败", null);
        }
        return new SysObject(list);
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
     * 验证用户名是否可用
     * @param name
     * @return
     */
    @RequestMapping("/validateUserName")
    public SysObject validateUserName(String name){
        Integer rows = userService.validateUserName(name);
        if (rows == 0) {
            return new SysObject(200, "该公司名称可以使用!", null);
        } else {
            return new SysObject(201, "该公司名称已被使用!", null);
        }
    }

    /**
     * 分销商注册
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public SysObject registerUser(@RequestParam Map map) {
        try {
            Integer row = userService.registerUser(map);
            if (row > 0){
                return new SysObject(200, "注册成功!", null);
            }
//            Integer saleId = userService.registerUser(map); // 返回注册成功的分销商ID
//            DistributorInfo dis = userService.selectDisByDIsID(saleId);
//            dis.setPassword(null);
//            return new SysObject(200, "注册成功!", dis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "注册失败!", null);
    }

    /**
     * 批量导入用户
     *
     * @param disId      分销商ID
     * @param cusExcel Excel文件
     * @return
     */
    @RequestMapping(value = "/batchExportCus", method = RequestMethod.POST)
    public SysObject batchExportCus(Integer disId, @RequestParam(value = "cusExcel") MultipartFile cusExcel) {
        try {
            Map<String, Object> map = userService.batchExportCus(disId, cusExcel);
            if (map != null) {
                return new SysObject(200, "报备成功!", map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "报备异常,请检查Excel文件!", null);
    }

    /**
     * 验证分销商是否已审核营业执照
     *
     * @param disId 分销商ID
     * @return
     */
    @RequestMapping("/validateDisState")
    public SysObject validateDisState(Integer disId) {
        try {
            DistributorInfo distributorInfo = userService.checkDistributorState(disId);
            return SysObject.ok(distributorInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "服务器异常!", null);
    }

    /**
     * 上传营业执照
     *
     *  licensePic 照片
     *  disId      分销商ID
     *  disCompany    公司名称   ??
     *  disLinkman    联系人    ??
     *  disLinktel    联系电话  ??
     * @return
     */
    @RequestMapping(value = "/uploadLicense", method = RequestMethod.POST)
    public SysObject uploadLicense(@RequestParam(value = "licensePic") MultipartFile licensePic, DistributorInfo distributorInfo) {
        try {
            Integer row = userService.saveDisLicense(licensePic,distributorInfo);
            if (row > 0){
                DistributorInfo dis = userService.selectDisByDIsID(distributorInfo.getDisId());
                return new SysObject(200, "提交成功,审核结果会在一个工作日内发送到至您的手机!", dis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "提交失败!", null);
    }

    /**
     * 分销商报备客户
     *
     *  saleId      分销商ID
     *  name        客户名字
     *  tel         客户电话
     *  projectName 项目名称
     *  cusArea     客户区域
     *  acreage     意向面积
     *  money       投资额
     *  remark      客户备注
     * @return
     */
    @RequestMapping(value = "/reportCustomer", method = RequestMethod.POST)
    public SysObject reportCustomer(Customer customer) {
        try {
            DistributorInfo distributorInfo = userService.checkDistributorState(customer.getSaleId());
            if (!"已过审".equals(distributorInfo.getCheckState())) {
                return new SysObject(201, "权限不足,请先完成公司营业执照审核!", null);
            }
            Integer row = userService.reportCustomer(customer);
            if (row > 0){
                return new SysObject(200, "报备成功!", null);
            }
            if (row == -1) {
                return new SysObject(201, "该客户报备该项目已超过报备次数!", null);
            }
            if (row == -2){
                return new SysObject(201, "您之前给该客户报备过相同的项目,不能重复上报!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "报备异常!", null);
    }

    /**
     * 修改客户状态--到访
     * @param disId
     * @param cusTel
     * @return
     */
    @RequestMapping("/changeCusVisit")
    public SysObject changeCusVisit(Integer disId, Integer proId, String cusTel){
        try {
            Integer row = userService.changeCusVisit(disId, proId, cusTel);
            if (row > 0){
                return new SysObject(200,"到访成功!",null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new SysObject(201,"到访失败!",null);
    }

    /**
     * 修改客户状态--成交
     * @param disId
     * @param cusTel
     * @return
     */
    @RequestMapping("/changeCusDeal")
    public SysObject changeCusDeal(Integer disId,Integer proId,String cusTel){
        try {
            Integer row = userService.changeCusDeal(disId, proId, cusTel);
            if (row > 0){
                return new SysObject(200,"成交成功!",null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new SysObject(201,"成交失败!",null);
    }

    /**
     * 统计该分销商的业绩
     * @param disId
     * @return
     */
    @RequestMapping("/statTurnover")
    public SysObject statTurnover(Integer disId){
        try {
            Map<String,Integer> map = userService.statTurnover(disId);
            return SysObject.ok(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new SysObject(201,"服务器异常!",null);
    }

    /**
     * 修改分销商审核状态
     * @param value
     * @return
     */
    @RequestMapping("/changeDisCkState")
    public SysObject changeDisCkState(Integer disId,String value){
        try {
            Integer row = userService.changeDisCkState(disId,value);
            if (row > 0){
                return SysObject.build(200,"修改成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(201,"修改异常!");
    }

    /**
     * 查找收藏客户的信息
     * @param disId
     * @param ids
     * @return
     */
    @RequestMapping("/getCusByCusIDs")
    public SysObject getCusByCusIDs(Integer disId,Integer[] ids){
        try {
            List<Customer> list = userService.getCusByCusIDs(disId,ids);
            return SysObject.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(500,"服务器内部错误!");
    }
}
