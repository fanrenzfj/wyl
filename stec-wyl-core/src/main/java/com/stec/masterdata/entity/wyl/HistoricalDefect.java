package com.stec.masterdata.entity.wyl;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/10/12
 * Time: 10:39
 */
@Entity
@Table(name = "historical_defect")
public class HistoricalDefect extends MasterEntity<String> {
    public static final String DEVICE = "device";
    public static final String STRUCTURE = "structure";
    private static final long serialVersionUID = 1791313258999604461L;
    //问题发现时间
    private Date discoveryDate;
    //专业  机电 土建
    private String major;

    //严重程度
    private String severity;
    //缺陷类型id  关联  defectType表
    private Long defectTypeId;
    //病害类型
    private String diseaseType;
    //问题描述
    private String problemRemark;
    //巡检人
    private Long inspectUserId;
    //上报情况  eg:报业主
    private String reportSituation;
    //上报节点
    private String reportNode;
    private Long reportDocId;
    private String dealStatus;
    //是否维修占道
    private Boolean blockRoad;
    //处理说明
    private String dealRemark;
    private Long dealDocId;
    private String deviceOrStructure;
    
    //关联项目id
    private Long projectId;

    private Long structureId;

    private Long deviceId;

    @Deprecated
    private Long deviceOrStructureId;

    //以下为了导入数据临时新建的参数
    //图片的地址  网络环境或者是本地
    private String pic_url;
    //缺陷巡检人员
    private String inspectUser;
    //结构/设备
    private String eStructureDevice;
    //结构/设备名称
    private String eStructureDeviceName;
    private String location;

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getInspectUser() {
        return inspectUser;
    }

    public void setInspectUser(String inspectUser) {
        this.inspectUser = inspectUser;
    }

    public String geteStructureDevice() {
        return eStructureDevice;
    }

    public void seteStructureDevice(String eStructureDevice) {
        this.eStructureDevice = eStructureDevice;
    }

    public String geteStructureDeviceName() {
        return eStructureDeviceName;
    }

    public void seteStructureDeviceName(String eStructureDeviceName) {
        this.eStructureDeviceName = eStructureDeviceName;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Transient
    @TableField(exist = false)
    private String deviceName;

    @Transient
    @TableField(exist = false)
    private String structureName;

    public Date getDiscoveryDate() {
        return discoveryDate;
    }

    public void setDiscoveryDate(Date discoveryDate) {
        this.discoveryDate = discoveryDate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Long getDefectTypeId() {
        return defectTypeId;
    }

    public void setDefectTypeId(Long defectTypeId) {
        this.defectTypeId = defectTypeId;
    }

    public String getProblemRemark() {
        return problemRemark;
    }

    public void setProblemRemark(String problemRemark) {
        this.problemRemark = problemRemark;
    }

    public Long getInspectUserId() {
        return inspectUserId;
    }

    public void setInspectUserId(Long inspectUserId) {
        this.inspectUserId = inspectUserId;
    }

    public String getReportSituation() {
        return reportSituation;
    }

    public void setReportSituation(String reportSituation) {
        this.reportSituation = reportSituation;
    }

    public String getReportNode() {
        return reportNode;
    }

    public void setReportNode(String reportNode) {
        this.reportNode = reportNode;
    }

    public Long getReportDocId() {
        return reportDocId;
    }

    public void setReportDocId(Long reportDocId) {
        this.reportDocId = reportDocId;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public Boolean getBlockRoad() {
        return blockRoad;
    }

    public void setBlockRoad(Boolean blockRoad) {
        this.blockRoad = blockRoad;
    }

    public String getDealRemark() {
        return dealRemark;
    }

    public void setDealRemark(String dealRemark) {
        this.dealRemark = dealRemark;
    }

    public Long getDealDocId() {
        return dealDocId;
    }

    public void setDealDocId(Long dealDocId) {
        this.dealDocId = dealDocId;
    }

    public String getDeviceOrStructure() {
        return deviceOrStructure;
    }

    public void setDeviceOrStructure(String deviceOrStructure) {
        this.deviceOrStructure = deviceOrStructure;
    }

    public Long getDeviceOrStructureId() {
        return deviceOrStructureId;
    }

    public void setDeviceOrStructureId(Long deviceOrStructureId) {
        this.deviceOrStructureId = deviceOrStructureId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {
        this.structureId = structureId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}