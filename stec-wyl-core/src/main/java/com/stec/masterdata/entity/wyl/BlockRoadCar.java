package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 15:28
 */
@Entity
@Table(name ="blockRoad_car")
public class BlockRoadCar extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411246L;
    private Long blockRoadId;
    private Long carId;

    public Long getBlockRoadId() {
        return blockRoadId;
    }

    public void setBlockRoadId(Long blockRoadId) {
        this.blockRoadId = blockRoadId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}