package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 8:39
 */
@Entity
@Table(name = "order_item")
public class OrderItem extends MasterEntity<String> {
    private static final long serialVersionUID = 4763707172411046624L;

    private Long workOrderId;

    private Date createDate;

    private Long createUserId;

    private Long structureId;

    private String structureName;

    private String remark;

    private Boolean relatedDevice;

    private Boolean action;

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }
}