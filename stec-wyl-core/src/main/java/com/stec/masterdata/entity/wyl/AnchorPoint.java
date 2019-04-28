package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 19:08
 */
@Entity
@Table(name = "anchor_point")
public class AnchorPoint extends MasterEntity<String> {
    private static final long serialVersionUID = 3200664955167563810L;

    private String tag;

    private Double mileage;

    private Double longitude;

    private Double latitude;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}