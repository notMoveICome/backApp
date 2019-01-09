package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.DistributorMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.mapper.UserMapper;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.DistributorInfo;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DistributorMapper distributorMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProjectMapper projectMapper;

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
                List<DistributorInfo> users = userMapper.selectAllUser();
                return users;
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
            User user = new User();
            user.setName(username);
            user.setPassword(password);
            user.setRoleId(3);
            user.setTel(tel);
            user.setState("正常");
            user.setCreateTime(date);
            return userMapper.insert(user);
        }
        if ("分销商".equals(role)) {
            User user = new User();
            user.setName(username);
            user.setPassword(password);
            user.setRoleId(2);
            user.setTel(tel);
            user.setState("正常");
            user.setCreateTime(date);
            userMapper.addUser(user);

            DistributorInfo dis = new DistributorInfo();
            dis.setDisId(user.getGid());
//            dis.setChannelComm("王五");
            dis.setCheckState("未过审");
            return distributorMapper.insert(dis);
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
    public Object findUserByCondition(Map map) throws ParseException {
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
//        User user = new User();
//        user.setTel(tel);
//        user.setPassword(pwd);
        return userMapper.selectUser(tel,pwd);
//        return userMapper.selectOne(user);
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
    public Map<String,Object> batchExportCus(String dis, MultipartFile cusExcel) {
        User user = new User();
        user.setName(dis);
        User disUser = userMapper.selectOne(user);
        Map<String,Object> map = parseExcel(disUser.getGid(), cusExcel);
        return map;
    }

    @Override
    public Integer reportCustomer(Customer customer) {
        // 根据项目名查找项目
        String projectName = customer.getProjectName();
        Project project = new Project();
        project.setName(projectName);
        Project pro = projectMapper.selectOne(project);
        // 查该项目下的该用户报备次数
        Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("tel", customer.getTel()).and().eq("project_id", pro.getGid()));
        // 超过报备次数限制的用户不予报备
        if (count >= pro.getReportLimit()){
            return -1;
        }else {
            customer.setProjectId(pro.getGid());
            customer.setState("正常");
            java.util.Date date = new java.util.Date();
            customer.setBackTime(new Date(date.getTime()));
            customer.setExpireTime(new Date(date.getTime() + 3600*24*7*1000));
            return customerMapper.insert(customer);
        }
    }

    @Override
    public boolean checkDistributorState(Integer saleId) {
        String state = userMapper.queryDisStateById(saleId);
        return state == "已过审" ? true : false;
    }

    private Map<String,Object> parseExcel(Integer disId, MultipartFile cusExcel) {
        //读取Excel里面客户的信息
        List<Customer> customerList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        //初始化输入流
        InputStream is = null;
        Workbook wb = null;
        try {
            is = cusExcel.getInputStream();
            // .xls与.xlsx都支持
            wb = WorkbookFactory.create(is);
            /** 得到第一个shell */
            Sheet sheet = wb.getSheetAt(0);
            /** 得到Excel的行数 */
            int totalRows = sheet.getPhysicalNumberOfRows();
            /** 得到Excel的列数 */
            int totalCells = 0;
            if (totalRows >= 1 && sheet.getRow(0) != null) {
                totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            }
            /** 循环Excel的行 */
            int m = 0;
            for (int r = 1; r < totalRows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                /** 循环Excel的列 */
                List<String> list = getRowList(row, totalCells);
                boolean flag = true;
                for (String s : list) {
                    if (org.apache.commons.lang3.StringUtils.isBlank(s)) {
                        flag = false;
                        break;
                    }
                }
                // 根据项目名查项目--
                Project project = new Project();
                project.setName(list.get(2));
                Project proRes = projectMapper.selectOne(project);
                if (flag && proRes != null) {
                    // 查该项目下的该用户报备次数
                    Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("tel", list.get(1)).and().eq("project_id", proRes.getGid()));
                    // 超过报备次数限制的用户不予报备
                    if (count >= proRes.getReportLimit()) {
                        Customer c = new Customer();
                        c.setName(list.get(0));
                        c.setTel(list.get(1));
                        c.setProjectName(list.get(2));
                        customerList.add(c);
                        continue;
                    }
                    java.util.Date date = new java.util.Date();
                    Customer customer = new Customer();
                    customer.setName(list.get(0));
                    customer.setTel(list.get(1));
                    customer.setSaleId(disId);
                    customer.setProjectId(proRes.getGid());
                    customer.setState("正常");
                    customer.setBackTime(new Date(date.getTime()));
                    customer.setExpireTime(new Date(date.getTime() + 3600*24*7*1000));// 七天时间
                    customerMapper.insert(customer);
                    m++;
                }else {
                    Customer c = new Customer();
                    c.setName(list.get(0));
                    c.setTel(list.get(1));
                    c.setProjectName(list.get(2));
                    customerList.add(c);
                }
            }
            map.put("success",m);
            map.put("fail",customerList);
            is.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<String> getRowList(Row row, int totalCells) {
        List<String> list = new ArrayList();
        for (int c = 0; c < totalCells; c++) {
            Cell cell = row.getCell(c);
            if (null != cell && cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                list.add(cell.getStringCellValue());
            } else if (null != cell && cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                list.add(String.valueOf(cell.getNumericCellValue()));
            } else {
                list.add(null);
            }
        }
        return list;
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
        Date date = new Date(System.currentTimeMillis());
        user.setCreateTime(date);
        userMapper.addUser(user);// 返回自增逐渐ID

        DistributorInfo dis = new DistributorInfo();
        dis.setDisId(user.getGid());
        dis.setChannelComm(attache);
        dis.setCheckState("未过审");
        dis.setSize(size);
        return distributorMapper.insert(dis);
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
