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
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Object getUserByRole(String role) {
        if ("客户".equals(role)) {
            List<Customer> customerList = customerMapper.findAllCustomerInfo();
            return customerList;
        } else {
            Integer roleId = 3;

            if ("管理员".equals(role)) {
                roleId = 3;
            } else if ("分销商".equals(role)) {
                return userMapper.selectAllUser();
            }

            return userMapper.selectList(new EntityWrapper<User>().eq("role_id", roleId));
        }
    }

    @Override
    public List<Customer> getCustomerInfoBySale(Integer saleId) {
        return customerMapper.findCustomerInfoBySale(saleId);
    }

    @Override
    public Integer addUser(String username, String password, String tel, String role) {
        Date date = new Date(new java.util.Date().getTime());
        if ("管理员".equals(role)) {
            User user = new User(null, username, password, tel, 3, "正常", null, date, null, null);
            return userMapper.insert(user);
        }
        if ("分销商".equals(role)) {
            User user = new User(null, username, password, tel, 2, "正常", "待审核", date, null, null);
            return userMapper.insert(user);
        }
        return null;
    }

    @Override
    public Integer changeState(Integer gid, String state) {
        User user = new User();
        user.setGid(gid);
        user.setState("正常".equals(state) ? "非正常" : "正常");
        return userMapper.updateById(user);
    }

    @Override
    public Integer updateUser(Integer gid, String username, String password, String tel) {
        User user = new User();
        user.setGid(gid);
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
        //如果用户没有选择查询的时间，则时间段2000-2100，以表示查询所有时间段内
        if (StringUtils.isEmpty(starttime)) {
            starttime = "2000-1-1";
        }
        if (StringUtils.isEmpty(endtime)) {
            endtime = "2100-12-30";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = new java.sql.Date(sdf.parse(starttime).getTime());
        java.util.Date endDate = new java.sql.Date(sdf.parse(endtime).getTime());
        EntityWrapper<User> u = new EntityWrapper<>();
        if ("管理员".equals(role)) {
            //管理员的role_id的type值为3
            int tpye = 3;
            return userMapper.selectList(new EntityWrapper<User>().eq("role_id", 3).like("name", username).like("tel", usertel).between("create_time", beginDate, endDate));

        } else {
            return userMapper.findUserByCondition(username, usertel, beginDate, endDate);
        }

    }

    @Override
    public User findSaleByTelAndPwd(String tel, String pwd) {
        User user = new User();
        user.setTel(tel);
        user.setPassword(pwd);// ????
        return userMapper.selectOne(user);
    }

    @Override
    public List<Customer> findCustomerByCondition(Map map) throws ParseException {
        //拼接查询条件，如果只有起始时间则条件为大于起始时间的所有用户（终止时间则，小于所有的终止时间），
        String username = map.get("username").toString();
        String proname = map.get("proname").toString();
        String usertel = map.get("usertel").toString();
        String starttime = map.get("starttime").toString();
        String endtime = map.get("endtime").toString();
        if (StringUtils.isEmpty(starttime)) {
            starttime = "2000-1-1";
        }
        if (StringUtils.isEmpty(endtime)) {
            endtime = "2100-12-30";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = new java.sql.Date(sdf.parse(starttime).getTime());
        java.util.Date endDate = new java.sql.Date(sdf.parse(endtime).getTime());

        return customerMapper.findCustomerByCondition(username, proname, usertel, beginDate, endDate);
    }

    @Override
    public Integer validateTel(String tel) {
        return userMapper.selectList(new EntityWrapper<User>().eq("tel", tel)).size();
    }

    @Override
    public Integer registerUser(Map map) {
        String tel = (String) map.get("tel");
        String pwd = (String) map.get("pwd");
        String company = (String) map.get("company");
        String size = (String) map.get("size");
        String attache = (String) map.get("attache");

        User user = new User();
        user.setName(company);
        user.setPassword(pwd);
        user.setTel(tel);
        user.setRoleId(2);
        user.setState("正常");
        user.setCheckState("未过审");
        Date date = new Date(System.currentTimeMillis());
        user.setCreateTime(date);
        user.setChannelComm(attache);
        user.setSize(size);
        Integer row = userMapper.insert(user);
        return row;
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
