package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 13:16
 */
@Entity
@Table(name ="block_road")
public class BlockRoad extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411245L;
    private Date planStartDate;
    private Date planEndDate;
    private String type;
    private String position;
    private String roadNo;
    private String roadStart;
    private String roadEnd;
    private Long leaderId;
    private String leaderName;
    private String mobile;
    private String projectId;
    private Long docId;
    private String remark;
    private Boolean handleStatus;
    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    public String getRoadStart() {
        return roadStart;
    }

    public void setRoadStart(String roadStart) {
        this.roadStart = roadStart;
    }

    public String getRoadEnd() {
        return roadEnd;
    }

    public void setRoadEnd(String roadEnd) {
        this.roadEnd = roadEnd;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Boolean handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}