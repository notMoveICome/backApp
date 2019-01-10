package com.hxlc.backstageapp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.UserMapper;
import com.hxlc.backstageapp.pojo.User;
import com.hxlc.backstageapp.service.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public void load(String role, String data, HttpServletResponse response) throws IOException {
        //获取用户选择的信息进行生成excel导出
        String decode = URLDecoder.decode(data,"UTF-8");
        JSONArray jsonArray = JSONArray.parseArray(decode);
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook();
        OutputStream out = null;
        String filename = "";
        if ("管理员".equals(role)) {
            filename = "管理员用户信息.xls";
            Sheet sheet = workbook.createSheet("管理员信息清单");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("姓名");
            row.createCell(1).setCellValue("密码");
            row.createCell(2).setCellValue("手机号码");
            row.createCell(3).setCellValue("状态");
            row.createCell(4).setCellValue("创建时间");
            row.createCell(5).setCellValue("备注");
            for (int i = 0; i < jsonArray.size(); i++) {
                Row rows = sheet.createRow(i + 1);
                Map<String, String> map = (Map) jsonArray.get(i);
                String name = map.get("name");
                String password = map.get("password");
                String tel = map.get("tel");
                String state = map.get("state");
                String createTime = map.get("createTime");
                String remark = "无";
                if (map.get("remark") != null) {
                    remark = map.get("remark");
                }
                rows.createCell(0).setCellValue(name);
                rows.createCell(1).setCellValue(password);
                rows.createCell(2).setCellValue(tel);
                rows.createCell(3).setCellValue(state);
                rows.createCell(4).setCellValue(createTime);
                rows.createCell(5).setCellValue(remark);
            }
        }
        if ("分销商".equals(role)) {
            filename = "分销商用户信息.xls";
            Sheet sheet = workbook.createSheet("分销商用户");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("姓名");
            row.createCell(1).setCellValue("密码");
            row.createCell(2).setCellValue("手机号码");
            row.createCell(3).setCellValue("状态");
            row.createCell(4).setCellValue("审核");
            row.createCell(5).setCellValue("创建时间");
            row.createCell(6).setCellValue("渠道专员");
            row.createCell(7).setCellValue("备注");
            row.createCell(8).setCellValue("报备数量");
            for (int i = 0; i < jsonArray.size(); i++) {
                Row rows = sheet.createRow(i + 1);
                Map<String, Object> map = (Map) jsonArray.get(i);
                String name = map.get("name").toString();
                String password = map.get("password").toString();
                String tel = map.get("tel").toString();
                String state = map.get("state").toString();
                String checkState = map.get("checkState").toString();
                String createTime = map.get("createTime").toString();
                String channelComm = "";
                if (map.get("channelComm") != null) {
                    channelComm = map.get("channelComm").toString();
                }
                String remark = "无";
                if (map.get("remark") != null) {
                    remark = map.get("remark").toString();
                }
                String count = "0";
                if (map.get("count") != null) {
                    count = map.get("count").toString();
                }
                rows.createCell(0).setCellValue(name);
                rows.createCell(1).setCellValue(password);
                rows.createCell(2).setCellValue(tel);
                rows.createCell(3).setCellValue(state);
                rows.createCell(4).setCellValue(checkState);
                rows.createCell(5).setCellValue(createTime);
                rows.createCell(6).setCellValue(channelComm);
                rows.createCell(7).setCellValue(remark);
                rows.createCell(8).setCellValue(count);
            }
        }
        if ("客户".equals(role)) {
            filename = "客户信息.xls";
            Sheet sheet = workbook.createSheet("客户信息清单");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("姓名");
            row.createCell(1).setCellValue("电话");
            row.createCell(2).setCellValue("项目");
//            row.createCell(3).setCellValue("分销商");
            row.createCell(3).setCellValue("状态");
            row.createCell(4).setCellValue("顾客区域");
            row.createCell(5).setCellValue("意向面积");
            row.createCell(6).setCellValue("投资金额");
            row.createCell(7).setCellValue("报备时间");
            row.createCell(8).setCellValue("过期时间");
            row.createCell(9).setCellValue("到访时间");
            row.createCell(10).setCellValue("成交时间");

            for (int i = 0; i < jsonArray.size(); i++) {
                Row rows = sheet.createRow(i + 1);
                Map<String, Object> map = (Map) jsonArray.get(i);
                String name = map.get("name").toString();
                String tel = map.get("tel").toString();
                String projectName = map.get("projectName").toString();
//                String distritionName = map.get("distritionName").toString();
//                String saleName = map.get("saleName").toString();
                String state = map.get("state").toString();
                String backTime = map.get("backTime").toString();
                String expireTime = map.get("expireTime").toString();
//                String remark = "无";
//                if (map.get("remark") != null) {
//                    remark = map.get("remark").toString();
//                }
                String cusArea = "-";
                if (map.get("cusArea") != null) {
                    cusArea = map.get("cusArea").toString();
                }
                String acreage = "-";
                if (map.get("acreage") != null) {
                    acreage = map.get("acreage").toString();
                }
                String money = "-";
                if (map.get("money") != null) {
                    money = map.get("money").toString();
                }
                String visitTime = "-";
                if (map.get("visitTime") != null) {
                    visitTime = map.get("visitTime").toString();
                }
                String dealTime = "-";
                if (map.get("dealTime") != null) {
                    dealTime = map.get("dealTime").toString();
                }
                rows.createCell(0).setCellValue(name);
                rows.createCell(1).setCellValue(tel);
                rows.createCell(2).setCellValue(projectName);
                rows.createCell(3).setCellValue(state);
                rows.createCell(4).setCellValue(cusArea);
                rows.createCell(5).setCellValue(acreage);
                rows.createCell(6).setCellValue(money);
                rows.createCell(7).setCellValue(backTime);
                rows.createCell(8).setCellValue(expireTime);
                rows.createCell(9).setCellValue(visitTime);
                rows.createCell(10).setCellValue(dealTime);
            }
        }
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename="
                + new String(filename.getBytes(), "iso-8859-1"));
        workbook.write(output);
        output.close();
    }
}
