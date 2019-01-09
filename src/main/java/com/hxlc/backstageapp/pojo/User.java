package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.sql.Date;

@TableName("user_info")
public class User implements Serializable {
    @TableId(value = "gid", type = IdType.AUTO)
    private Integer gid;
    private String name;
    private String password;
    private String tel;
    private Integer roleId;
    private String state;
//    private String checkState;
    private Date createTime;
//    private String channelComm;
//    private String size;
    private String remark;
    @TableField(exist = false)
    private Integer count;// 分销商报备总数统计

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

//    public String getChannelComm() {
//        return channelComm;
//    }
    public Integer getCount() {
        return count;
    }

//    public void setChannelComm(String channelComm) {
//        this.channelComm = channelComm;
//    }

//    public String getCheckState() {
//        return checkState;
//    }

//    public void setCheckState(String checkState) {
//        this.checkState = checkState;
//    }
    public void setCount(Integer count) {
        this.count = count;
    }

//    public String getSize() {
//        return size;
//    }

//    public void setSize(String size) {
//        this.size = size;
//    }
}
