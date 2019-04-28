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
@Table(name ="emergency_event_material")
public class EmergencyEventMaterial extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411251L;
    private Long emergencyEventId;
    private Long materialId;
    private String materialName;
    private Double amount;
    private String unit;


    public Long getEmergencyEventId() {
        return emergencyEventId;
    }

    public void setEmergencyEventId(Long emergencyEventId) {
        this.emergencyEventId = emergencyEventId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}