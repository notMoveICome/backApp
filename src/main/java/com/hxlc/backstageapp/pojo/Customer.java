package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.sql.Date;

@TableName("customer_info")
public class Customer {
    @TableId(value="gid",type= IdType.AUTO)
    private Integer gid;
    private String name;
    private String tel;
    private Integer projectId;
    // 表中不存在的字段
    @TableField(exist=false)
    private String  projectName;
    @TableField(exist=false)
    private Integer distritionId;
    @TableField(exist=false)
    private String  distritionName;
    private Integer saleId;
    @TableField(exist=false)
    private String  saleName;
    private String state;
    private Date backTime;
    private Date expireTime;
    private String remark;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getDistritionId() {
        return distritionId;
    }

    public void setDistritionId(Integer distritionId) {
        this.distritionId = distritionId;
    }

    public String getDistritionName() {
        return distritionName;
    }

    public void setDistritionName(String distritionName) {
        this.distritionName = distritionName;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
