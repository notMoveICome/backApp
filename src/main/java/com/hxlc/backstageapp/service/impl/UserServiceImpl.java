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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    @Value("${fileDir.disLicense}")
    private String disLicense;

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
            return userMapper.selectList(new EntityWrapper<User>().eq("role_id", roleId).orderBy("gid"));
        }
    }

    @Override
    public List<Customer> getCustomerInfoBySale(Integer saleId) {
        return customerMapper.findCustomerInfoBySale(saleId);
    }

    @Transactional
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
            dis.setCheckState("未提交");
            return distributorMapper.insert(dis);
        }
        return null;
    }

    @Transactional
    @Override
    public Integer changeState(Integer gid, String state) {
        User user = new User();
        user.setGid(gid);
        user.setState("正常".equals(state) ? "不正常" : "正常");
        return userMapper.updateById(user);
    }

    @Transactional
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
        return userMapper.selectUser(tel, pwd);
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

    @Transactional
    @Override
    public Map<String, Object> batchExportCus(Integer disId, MultipartFile cusExcel) {
        Map<String, Object> map = parseExcel(disId, cusExcel);
        return map;
    }

    @Transactional
    @Override
    public Integer reportCustomer(Customer customer) {
        // 根据项目名查找项目
        String projectName = customer.getProjectName();
        Project project = new Project();
        project.setName(projectName);
        Project pro = projectMapper.selectOne(project);
        // 查看是否之前给该客户报备过相同的项目
        List<Customer> selectList = customerMapper.selectList(new EntityWrapper<Customer>().eq("tel", customer.getTel()).eq("sale_id", customer.getSaleId()).eq("project_id", pro.getGid()));
        // 查该项目下的该用户报备次数
        Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("tel", customer.getTel()).eq("project_id", pro.getGid()));
        if (selectList != null && selectList.size() > 0){
            return -2;
        }else if (count >= pro.getReportLimit()) {  // 超过报备次数限制的用户不予报备
            return -1;
        } else {
            customer.setProjectId(pro.getGid());
            customer.setState("正常");
            java.util.Date date = new java.util.Date();
            customer.setBackTime(new Date(date.getTime()));
            customer.setExpireTime(new Date(date.getTime() + 3600 * 24 * 7 * 1000));
            return customerMapper.insert(customer);
        }
    }

    @Override
    public DistributorInfo checkDistributorState(Integer saleId) {
//        String state = userMapper.queryDisStateById(saleId);
//        return "已过审".equals(state) ? true : false;
        return selectDisByDIsID(saleId);
    }

    @Transactional
    @Override
    public Integer saveDisLicense(MultipartFile licensePic,DistributorInfo distributorInfo) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            String filename = licensePic.getOriginalFilename();
            File parentFile = new File(disLicense);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            // 存入本地
            File file = new File(parentFile.getAbsolutePath() + File.separator + filename);
            is = licensePic.getInputStream();
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            // 开始入库
            distributorInfo.setCheckState("审核中");
            distributorInfo.setLicense(disLicense.substring(disLicense.lastIndexOf("/"),disLicense.length()) + "/" + filename);
            Integer row = distributorMapper.update(distributorInfo, new EntityWrapper<DistributorInfo>().eq("dis_id", distributorInfo.getDisId()));
            fos.close();
            is.close();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Transactional
    @Override
    public Integer changeCusVisit(Integer disId, Integer proId, String cusTel) {
        Customer customer = new Customer();
//        customer.setTel(cusTel);
//        customer.setSaleId(disId);
        customer.setState("到访");
        customer.setVisitTime(new Date(new java.util.Date().getTime()));
        Integer row = customerMapper.update(customer, new EntityWrapper<Customer>().eq("tel", cusTel).eq("sale_id", disId).eq("project_id",proId));
        return row;
    }

    @Transactional
    @Override
    public Integer changeCusDeal(Integer disId, Integer proId, String cusTel) {
        Customer customer = new Customer();
//        customer.setTel(cusTel);
//        customer.setSaleId(disId);
        customer.setState("成交");
        customer.setDealTime(new Date(new java.util.Date().getTime()));
        return customerMapper.update(customer,new EntityWrapper<Customer>().eq("tel",cusTel).eq("sale_id",disId).eq("project_id",proId));
    }

    @Override
    public DistributorInfo selectDisByDIsID(Integer disId) {
        DistributorInfo dis = distributorMapper.queryDisByDisId(disId);
        String chanTel = distributorMapper.getChTelById(disId);
        dis.setChannelCommTel(chanTel);
        return dis;
    }

    @Override
    public Map<String, Integer> statTurnover(Integer disId) {
        Integer cj = customerMapper.selectCount(new EntityWrapper<Customer>().eq("sale_id", disId).eq("state", "成交"));
        Integer dcj = customerMapper.selectCount(new EntityWrapper<Customer>().eq("sale_id", disId).in("state",new String[]{"正常","到访"}));
        Map<String,Integer> map = new HashMap<>();
        map.put("success",cj);
        map.put("unsuccess",dcj);
        return map;
    }

    @Transactional
    @Override
    public Integer changeDisCkState(Integer disId,String value) {
        String v = "";
        if ("pass".equals(value)){
            v = "已过审";
            distributorMapper.updateDisCkState(disId,v);
            User user = new User();
            user.setGid(disId);
            user.setState("正常");
            Integer row = userMapper.updateById(user);  // 改变用户的状态
            return row;
        }else {
            v = "未过审";
            return distributorMapper.updateDisCkState(disId,v);
        }
    }

    @Override
    public Integer validateUserName(String name) {
        return userMapper.selectCount(new EntityWrapper<User>().eq("name", name));
    }

    @Override
    public String getChTelById(Integer gid) {
        String tel = distributorMapper.getChTelById(gid);
        return tel;
    }

    @Override
    public List<Customer> getCusByCusIDs(Integer disId, String ids) {
        String[] idArr = ids.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0;i < idArr.length;i++){
            list.add(Integer.valueOf(idArr[i]));
        }
        List<Customer> cusList = customerMapper.getCusByCusIDs(disId,list);
        return cusList;
    }

    private Map<String, Object> parseExcel(Integer disId, MultipartFile cusExcel) {
        //读取Excel里面客户的信息
        List<Customer> customerList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
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
                // excel表各中每列不能有空,备注除外
                boolean flag = true;
                for (int i = 0;i < list.size() - 1;i++) {
                    if (org.apache.commons.lang3.StringUtils.isBlank(list.get(i))) {
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
                    Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("tel", list.get(1)).eq("project_id", proRes.getGid()));
                    // 查是否之前已经报备过(该分销商+该客户+该项目)
                    List<Customer> selectList = customerMapper.selectList(new EntityWrapper<Customer>().eq("tel", list.get(1)).eq("sale_id", disId).eq("project_id", proRes.getGid()));
                    // 超过报备次数限制的用户不予报备
                    Integer limit = proRes.getReportLimit() == null ? 0 : proRes.getReportLimit();
                    if ((selectList != null && selectList.size() > 0) || count >= limit) {
                        Customer c = new Customer();
                        c.setName(list.get(0));
                        c.setTel(list.get(1));
                        c.setProjectName(list.get(2));
                        c.setCusArea(list.get(3));
                        c.setAcreage(list.get(4));
                        c.setMoney(new Float(list.get(5)));
                        c.setRemark(list.get(6));
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
                    customer.setExpireTime(new Date(date.getTime() + 3600 * 24 * 7 * 1000));// 七天时间
                    customer.setCusArea(list.get(3));
                    customer.setAcreage(list.get(4));
                    customer.setMoney(new Float(list.get(5)));
                    customer.setRemark(list.get(6));
                    customerMapper.insert(customer);
                    m++;
                } else {
                    Customer c = new Customer();
                    c.setName(list.get(0));
                    c.setTel(list.get(1));
                    c.setProjectName(list.get(2));
                    c.setCusArea(list.get(3));
                    c.setAcreage(list.get(4));
                    c.setMoney(new Float(list.get(5)));
                    c.setRemark(list.get(6));
                    customerList.add(c);
                }
            }
            map.put("success", m);
            map.put("fail", customerList);
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
        return userMapper.selectCount(new EntityWrapper<User>().eq("tel", tel));
    }

    @Transactional
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
        user.setState("不正常");
        Date date = new Date(System.currentTimeMillis());
        user.setCreateTime(date);
        userMapper.addUser(user);// 返回自增逐渐ID

        DistributorInfo dis = new DistributorInfo();
        dis.setDisId(user.getGid());
        dis.setChannelComm(Integer.valueOf(attache));
        dis.setCheckState("未提交");
        dis.setSize(size);
        return distributorMapper.insert(dis);
//        return user.getGid();
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

    @Transactional
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
