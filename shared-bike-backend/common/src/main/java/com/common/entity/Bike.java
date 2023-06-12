package com.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("bike")
@Data
public class Bike {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String x;
    private String y;
    private String status;
    private String owner;
    private String type;
}
