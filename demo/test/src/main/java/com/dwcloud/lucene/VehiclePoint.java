package com.dwcloud.lucene;

import lombok.Data;

/**
 * 车辆坐标点
 */
@Data
public class VehiclePoint {
    //车牌号，需要索引
    private String plateNumber;
    //坐标点（经度,纬度），需要索引
    private String point;
    //时间
    private String time;
    //速度
    private String speed;
    //其它
    private String other;
    //评分
    private String score;
}