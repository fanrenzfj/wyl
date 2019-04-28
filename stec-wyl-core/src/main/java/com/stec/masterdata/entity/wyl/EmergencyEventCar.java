package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 8:56
 */
@Entity
@Table(name ="emergency_event_car")
public class EmergencyEventCar extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411250L;
    private Long emergencyEventId;
    private Long carId;

    public Long getEmergencyEventId() {
        return emergencyEventId;
    }

    public void setEmergencyEventId(Long emergencyEventId) {
        this.emergencyEventId = emergencyEventId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}