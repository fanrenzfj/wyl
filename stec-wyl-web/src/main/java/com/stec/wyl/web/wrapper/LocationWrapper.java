package com.stec.wyl.web.wrapper;

import stec.framework.excel.annotation.ExcelField;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhuangrui
 * Date: 2018/12/5
 * Time: 18:48
 */
public class LocationWrapper {
    @ExcelField(title = "FID")
    private Long id;

    @ExcelField(title = "road")
    private String road;
    @ExcelField(title = "mileage")
    private String mileage;
    @ExcelField(title = "POINT_X")
    private Double POINT_X;
    @ExcelField(title = "POINT_Y")
    private Double POINT_Y;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Double getPOINT_X() {
        return POINT_X;
    }

    public void setPOINT_X(Double POINT_X) {
        this.POINT_X = POINT_X;
    }

    public Double getPOINT_Y() {
        return POINT_Y;
    }

    public void setPOINT_Y(Double POINT_Y) {
        this.POINT_Y = POINT_Y;
    }
}
