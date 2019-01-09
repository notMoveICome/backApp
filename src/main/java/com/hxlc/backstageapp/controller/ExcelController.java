package com.hxlc.backstageapp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hxlc.backstageapp.common.SysObject;

import com.hxlc.backstageapp.service.ExcelService;
import org.apache.http.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequestMapping()
@RestController
public class ExcelController {
    @Autowired
    private ExcelService excelService;

    @RequestMapping(value = "/downloadExcel")
    public SysObject downLoadExcel(String role, String data, HttpServletResponse response) throws IOException {
        try {
            excelService.load(role, data, response);
            return new SysObject(200, "download success!", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SysObject(201, "download fail!", null);
    }

}