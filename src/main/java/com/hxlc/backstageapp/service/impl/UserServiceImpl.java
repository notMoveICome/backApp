package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.UserMapper;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

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
    public Integer addUser(String username, String password, String tel,String role) {
        Date date = new Date(new java.util.Date().getTime());
        if("管理员".equals(role)){
            User user = new User(null,username,password,tel,3,"正常",date,null);
            return userMapper.insert(user);
        }
        if("分销商".equals(role)){
            User user = new User(null,username,password,tel,2,"正常",date,null);
            return userMapper.insert(user);
        }
        return null;
    }

    @Override
    public Integer changeState(Integer gid, String state) {
        User user = new User();
        user.setGID(gid);
        user.setState("正常".equals(state)?"非正常":"正常");
        return userMapper.updateById(user);
    }

    @Override
    public Integer updateUser(Integer gid, String username, String password, String tel) {
        User user = new User();
        user.setGID(gid);
        user.setName(username);
        user.setPassword(password);
        user.setTel(tel);
        return userMapper.updateById(user);
    }

    @Override
    public List<User> findUserByCondition(Map map) throws ParseException {
        //拼接查询条件，如果只有起始时间则条件为大于起始时间的所有用户（终止时间则，小于所有的终止时间），
        String username = map.get("username").toString();
        String usertel = map.get("usertel").toString();
        String starttime = map.get("starttime").toString();
        String endtime = map.get("endtime").toString();
        String role = map.get("role").toString();
        int type=2;
        if("管理员".equals(role)){
            type=3;
        }
        java.util.Date utilDate=null;
        java.util.Date utilDate1=null;
        java.util.Date date =null;
        java.util.Date date1 =null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<User> u = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(starttime)&&StringUtils.isNotEmpty(endtime)){
            utilDate = sdf.parse(starttime);
            utilDate1 = sdf.parse(endtime);
            date = new java.sql.Date(utilDate.getTime());
            date1 = new java.sql.Date(utilDate1.getTime());
            int i = Integer.parseInt(starttime.replace("-", ""));
            int i1 = Integer.parseInt(endtime.replace("-", ""));
            if(i1<1){
                u.eq("role_id",type).like("name",username).like("tel",usertel).between("create_time",date1,date);
                return userMapper.selectList(u);
            }else{
                u.eq("role_id",type).like("name",username).like("tel",usertel).between("create_time",date,date1);
                return userMapper.selectList(u);
            }
        }
        if(StringUtils.isNotEmpty(starttime)&&StringUtils.isEmpty(endtime)){
            utilDate = sdf.parse(starttime);
            date = new java.sql.Date(utilDate.getTime());
            u.eq("role_id",type).like("name",username).like("tel",usertel).ge("create_time",date);
            return userMapper.selectList(u);
        }
        if(StringUtils.isNotEmpty(endtime)&&StringUtils.isEmpty(starttime)){
            utilDate1 = sdf.parse(endtime);
            date1 = new java.sql.Date(utilDate1.getTime());
            u.eq("role_id",type).like("name",username).like("tel",usertel).le("create_time",date1);
            return userMapper.selectList(u);
        }
        if(StringUtils.isEmpty(starttime)&&StringUtils.isEmpty(endtime)){
            u.eq("role_id",type).like("name",username).like("tel",usertel);
            return userMapper.selectList(u);
        }
        return userMapper.selectList(new EntityWrapper<User>().eq("role_id",type));
    }

    @Override
    public User findSaleByTelAndPwd(String tel, String pwd) {
        User user = new User();
        user.setTel(tel);
        user.setPassword(pwd);
        return userMapper.selectOne(user);
    }

    @Override
    public User findUserByUsername(String username) {
        User user = new User();
        user.setName(username);
        return userMapper.selectOne(user);
    }

    @Override
    public User findUserByTel(String tel) {
        User user = new User();
        user.setTel(tel);
        return userMapper.selectOne(user);
    }

    @Override
    public Integer delUser(Integer gid) {
        return userMapper.deleteById(gid);
    }

    @Override
    public User findUser(String username, String password) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }

    @Override
    public Object getCustomerInfoBySale(String customerName, Integer saleId) {
        return customerMapper.findCustomerInfoBySale(saleId);
    }


}
