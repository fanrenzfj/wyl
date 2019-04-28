package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 8:32
 */
@Entity
@Table(name = "plan_item")
public class PlanItem extends MasterEntity<String> {
    private static final long serialVersionUID = 104941297389090091L;

    private Long workPlanId;

    private Long structureId;

    private String structureName;

    private String remark;

    private Boolean relatedDevice;

    public Long getWorkPlanId() {
        return workPlanId;
    }

    public void setWorkPlanId(Long workPlanId) {
        this.workPlanId = workPlanId;
    }

    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {
        this.structureId = structureId;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getRelatedDevice() {
        return relatedDevice;
    }

    public void setRelatedDevice(Boolean relatedDevice) {
        this.relatedDevice = relatedDevice;
    }
}