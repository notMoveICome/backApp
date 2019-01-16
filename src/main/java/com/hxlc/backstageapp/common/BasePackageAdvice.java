package com.hxlc.backstageapp.common;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.sql.Date;
import java.text.SimpleDateFormat;

@ControllerAdvice("com.hxlc.backstageapp.controller")
public class BasePackageAdvice {

    /**
     * 处理时间参数为空时的异常
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
