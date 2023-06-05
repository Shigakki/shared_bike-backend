package com.example.bikes.common;

import com.common.entity.Bike;

public class UpdateBikeRequest {
    private Bike bike;
    private String time;
    public UpdateBikeRequest() {
        // 默认构造函数
    }

    public UpdateBikeRequest(Bike bike, String time) {
        this.bike = bike;
        this.time = time;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
