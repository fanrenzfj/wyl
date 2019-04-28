package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 8:36
 */
@Entity
@Table(name = "plan_item_device")
public class PlanItemDevice extends MasterEntity<String> {
    private static final long serialVersionUID = 929314603063922639L;

    private Long planItemId;

    private Long deviceId;

    public Long getPlanItemId() {
        return planItemId;
    }

    public void setPlanItemId(Long planItemId) {
        this.planItemId = planItemId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}