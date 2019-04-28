package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 11:07
 */
@Entity
@Table(name = "defect")
public class Defect extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411243L;

    public static final String DEFECT_STATUS_CREATE = "0";
    public static final String DEFECT_STATUS_CONFIRM = "10";
    public static final String DEFECT_STATUS_PROCESS = "20";
    public static final String DEFECT_STATUS_COMPLETE = "30";

    private Date discoveryDate;
    private Long discoveryUserId;
    private String discoveryUserName;
    private Date createDate;
    private Date createUserId;
    private String source;
    private Long projectId;
    private Long structureId;
    private String structureName;
    private Long deviceId;
    private String deviceName;
    private Long defectTypeId;
    private String defectTypeName;
    private Long defectLevelId;
    private String defectLevelCode;
    private Long fromOrderId;
    private Long workOrderId;
    private String remark;
    private Long docId;
    private String status;

    public Date getDiscoveryDate() {
        return discoveryDate;
    }

    public void setDiscoveryDate(Date discoveryDate) {
        this.discoveryDate = discoveryDate;
    }

    public Long getDiscoveryUserId() {
        return discoveryUserId;
    }

    public void setDiscoveryUserId(Long discoveryUserId) {
        this.discoveryUserId = discoveryUserId;
    }

    public String getDiscoveryUserName() {
        return discoveryUserName;
    }

    public void setDiscoveryUserName(String discoveryUserName) {
        this.discoveryUserName = discoveryUserName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Date createUserId) {
        this.createUserId = createUserId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getDefectTypeId() {
        return defectTypeId;
    }

    public void setDefectTypeId(Long defectTypeId) {
        this.defectTypeId = defectTypeId;
    }

    public String getDefectTypeName() {
        return defectTypeName;
    }

    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }

    public String getDefectLevelCode() {
        return defectLevelCode;
    }

    public void setDefectLevelCode(String defectLevelCode) {
        this.defectLevelCode = defectLevelCode;
    }

    public Long getDefectLevelId() {
        return defectLevelId;
    }

    public void setDefectLevelId(Long defectLevelId) {
        this.defectLevelId = defectLevelId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public Long getFromOrderId() {
        return fromOrderId;
    }

    public void setFromOrderId(Long fromOrderId) {
        this.fromOrderId = fromOrderId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}