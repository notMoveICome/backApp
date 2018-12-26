package com.hxlc.backstageapp.common;

public class SysObject {
    // 响应业务状态
    /*
     * 200	成功
     * 201	错误
     * 400	参数错误
     */
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public static SysObject build(Integer status, String msg, Object data) {
        return new SysObject(status, msg, data);
    }

    public static SysObject ok(Object data) {
        return new SysObject(data);
    }

    public static SysObject ok() {
        return new SysObject(null);
    }

    public SysObject() {

    }

    public static SysObject build(Integer status, String msg) {
        return new SysObject(status, msg, null);
    }

    public SysObject(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public SysObject(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
