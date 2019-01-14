package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("distributor_info")
public class DistributorInfo extends User implements Serializable {
    @TableId(value = "gid", type = IdType.AUTO)
    private Integer gid;
    private Integer disId;
    private String checkState;
    private Integer channelComm;
    private String size;
    private String license;
    private String disCompany;
    private String disLinkman;
    private String disLinktel;
//    @TableField(exist = false)
//    private String chanCommTel;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getDisId() {
        return disId;
    }

    public void setDisId(Integer disId) {
        this.disId = disId;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public Integer getChannelComm() {
        return channelComm;
    }

    public void setChannelComm(Integer channelComm) {
        this.channelComm = channelComm;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDisCompany() {
        return disCompany;
    }

    public void setDisCompany(String disCompany) {
        this.disCompany = disCompany;
    }

    public String getDisLinkman() {
        return disLinkman;
    }

    public void setDisLinkman(String disLinkman) {
        this.disLinkman = disLinkman;
    }

    public String getDisLinktel() {
        return disLinktel;
    }

    public void setDisLinktel(String disLinktel) {
        this.disLinktel = disLinktel;
    }

//    public String getChanCommTel() {
//        return chanCommTel;
//    }
//
//    public void setChanCommTel(String chanCommTel) {
//        this.chanCommTel = chanCommTel;
//    }
}
