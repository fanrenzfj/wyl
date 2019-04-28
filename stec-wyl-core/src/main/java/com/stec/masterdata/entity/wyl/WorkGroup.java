package com.stec.masterdata.entity.wyl;


import com.stec.masterdata.entity.common.MasterEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/20
 * Time: 19:28
 */
@Entity
@Table(name = "work_group")
public class WorkGroup extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411253L;
    private String type;
    private Long orgId;
    private String orgName;
    private Long leaderId;
    private String leaderName;
    private String mobile;
    private Long projectId;
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }


}