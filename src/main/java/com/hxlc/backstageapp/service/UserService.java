package com.hxlc.backstageapp.service;

import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.DistributorInfo;
import com.hxlc.backstageapp.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserService {

    Object getUserByRole(String role);

    List<Customer> getCustomerInfoBySale(Integer saleId);

    User findUser(String username, String password);

    Object getCustomerInfoBySale(String customerName, Integer saleId);

    Integer addUser(String username, String password, String tel, String role);

    User findUserByUsername(String username);

    User findUserByTel(String tel);

    Integer delUser(Integer gid);

    Integer changeState(Integer gid, String state);

    Integer updateUser(Integer gid, String username, String password, String tel);

    Object findUserByCondition(Map map) throws ParseException;

    User findSaleByTelAndPwd(String tel, String pwd);

    Integer validateTel(String tel);

    /**
     * 分销商注册
     *
     * @param map
     * @return
     */
    Integer registerUser(Map map);

    List<Customer> findCustomerByCondition(Map map) throws ParseException;

    Map<String, Object> batchExportCus(String dis, MultipartFile cusExcel);

    Integer reportCustomer(Customer customer);

    boolean checkDistributorState(Integer saleId);

    Integer  saveDisLicense(MultipartFile licensePic, DistributorInfo distributorInfo);

    Integer changeCusVisit(Integer disId, Integer proId, String cusTel);

    Integer changeCusDeal(Integer disId, Integer proId, String cusTel);

    DistributorInfo selectDisByDIsID(Integer disId);
}
