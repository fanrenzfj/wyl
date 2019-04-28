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
@Table(name ="emergency_event_work_group")
public class EmergencyEventWorkGroup extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411252L;
    private Long emergencyEventId;
    private Long workGroupId;

    public Long getEmergencyEventId() {
        return emergencyEventId;
    }

    public void setEmergencyEventId(Long emergencyEventId) {
        this.emergencyEventId = emergencyEventId;
    }

    public Long getWorkGroupId() {
        return workGroupId;
    }

    public void setWorkGroupId(Long workGroupId) {
        this.workGroupId = workGroupId;
    }
}