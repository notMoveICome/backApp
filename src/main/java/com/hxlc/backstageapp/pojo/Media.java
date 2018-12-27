package com.hxlc.backstageapp.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("media_info")
public class Media {
    @TableId(value="gid",type= IdType.AUTO)
    private Integer gid;
    private String name;
    private String format;
    private String remark;
}
