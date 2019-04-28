package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/30
 * Time: 15:36
 */
@Entity
@Table(name ="car_usage")
public class CarUsage extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411266L;
    private Long carId;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}