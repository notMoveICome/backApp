package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.sql.Date;
import java.util.Arrays;

@TableName("project_info")
public class Project {
    @TableId(value = "gid", type = IdType.AUTO)
    private Integer gid;
    private String name;
    private Integer disnum;
    @TableField(exist = false)
    private Integer reportNum;
    private String description;
    private String develop;
    private String keyword;
    private String type;
    private byte[] descPic;
    private Float price;
    private String address;
    private Float commission;
    private String header;
    private String tel;
    private Date backTime;
    private String state;
    private Integer reportLimit;
    private Integer captionId;
    private Integer questionId;
    private Date biddingBegin;
    private Date biddingEnd;
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

    public Integer getDisnum() {
        return disnum;
    }

    public void setDisnum(Integer disnum) {
        this.disnum = disnum;
    }

    public Integer getReportNum() {
        return reportNum;
    }

    public void setReportNum(Integer reportNum) {
        this.reportNum = reportNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getDescPic() {
        return descPic;
    }

    public void setDescPic(byte[] descPic) {
        this.descPic = descPic;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getCommission() {
        return commission;
    }

    public void setCommission(Float commission) {
        this.commission = commission;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getReportLimit() {
        return reportLimit;
    }

    public void setReportLimit(Integer reportLimit) {
        this.reportLimit = reportLimit;
    }

    public Integer getCaptionId() {
        return captionId;
    }

    public void setCaptionId(Integer captionId) {
        this.captionId = captionId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Date getBiddingBegin() {
        return biddingBegin;
    }

    public void setBiddingBegin(Date biddingBegin) {
        this.biddingBegin = biddingBegin;
    }

    public Date getBiddingEnd() {
        return biddingEnd;
    }

    public void setBiddingEnd(Date biddingEnd) {
        this.biddingEnd = biddingEnd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Project{" +
                "gid=" + gid +
                ", name='" + name + '\'' +
                ", disnum=" + disnum +
                ", reportNum=" + reportNum +
                ", description='" + description + '\'' +
                ", develop='" + develop + '\'' +
                ", keyword='" + keyword + '\'' +
                ", type='" + type + '\'' +
                ", descPic=" + Arrays.toString(descPic) +
                ", price=" + price +
                ", address='" + address + '\'' +
                ", commission=" + commission +
                ", header='" + header + '\'' +
                ", tel='" + tel + '\'' +
                ", backTime=" + backTime +
                ", state='" + state + '\'' +
                ", reportLimit=" + reportLimit +
                ", captionId=" + captionId +
                ", questionId=" + questionId +
                ", biddingBegin=" + biddingBegin +
                ", biddingEnd=" + biddingEnd +
                ", remark='" + remark + '\'' +
                '}';
    }
}
