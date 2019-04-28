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
 * Time: 8:16
 */
@Entity
@Table(name = "work_plan")
public class WorkPlan extends MasterEntity<String> {

    private static final long serialVersionUID = -20533987343051796L;

    public static final String PLAN_STATUS_CONFIRM = "1";

    public static final String PLAN_STATUS_CREATE = "0";

    private Long projectId;

    private String type;

    private Date startDate;

    private Date endDate;

    private String frequency;

    private Integer hours;

    private Long createUserId;

    private Date createDate;

    private Long updateUserId;

    private Date updateDate;

    private String remark;

    private String assetNum;

    private String assetUnit;

    private Long confirmUserId;

    private Date confirmDate;

    private String confirm;

    private String status;

    private Long docId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getAssetUnit() {
        return assetUnit;
    }

    public void setAssetUnit(String assetUnit) {
        this.assetUnit = assetUnit;
    }

    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}