package com.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


@TableName("bike")
@Data
public class Bike {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String x;
    private String y;
    private String status;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String owner;
    private String type;
}
