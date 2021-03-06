package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("app_face")
public class AppFace implements Serializable {
    @TableId(value = "gid", type = IdType.AUTO)
    private Integer gid;
    private Integer projectId;
    private Integer index;
    private String price;
    private Integer adIndex;
    private String publishTime;
    private String remark;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAdIndex() {
        return adIndex;
    }

    public void setAdIndex(Integer adIndex) {
        this.adIndex = adIndex;
    }
}
