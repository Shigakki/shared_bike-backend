package com.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("parking")
@Data
public class Parking {
    private Integer userId;
    private Integer bikeId;
    private String x;
    private String y;
    private String time;
    private String type;
}
