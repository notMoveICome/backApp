package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.sql.Date;

@TableName("project_info")
public class Project {
    @TableId(value = "gid", type = IdType.AUTO)
    private Integer gid;
    private String name;
    private Integer disnum;
    private String desc;
    private String develop;
    private String keyword;
    private byte[] descPic;
    private Float price;
    private String address;
    private Float commission;
    private String header;
    private String tel;
    private Date backTime;
    private Integer state;
    private Integer captionId;
    private Integer questionId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
