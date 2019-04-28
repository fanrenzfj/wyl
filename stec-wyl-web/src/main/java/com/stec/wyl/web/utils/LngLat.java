package com.stec.wyl.web.utils;

/**
 * Created with IntelliJ IDEA.
 * 经纬度点封装
 * @author zhuangrui
 * Date: 2018/12/5
 * Time: 16:42
 */
public class LngLat {
    private double longitude;//经度
    private double lantitude;//维度

    public LngLat() {
    }

    public LngLat(double longitude, double lantitude) {
        this.longitude = longitude;
        this.lantitude = lantitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLantitude() {
        return lantitude;
    }

    public void setLantitude(double lantitude) {
        this.lantitude = lantitude;
    }

    @Override
    public String toString() {
        return "LngLat{" +
                "longitude=" + longitude +
                ", lantitude=" + lantitude +
                '}';
    }
}
