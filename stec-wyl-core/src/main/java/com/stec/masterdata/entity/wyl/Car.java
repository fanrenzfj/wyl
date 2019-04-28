package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/22
 * Time: 16:17
 */
@Entity
@Table(name ="car")
public class Car extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411248L;
    private String type;
    private Long orgId;
    private String orgName;
    private String useStatus;
    private String simNo;
    private boolean vehicularVideo;
    private Long docId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo;
    }

    public boolean isVehicularVideo() {
        return vehicularVideo;
    }

    public void setVehicularVideo(boolean vehicularVideo) {
        this.vehicularVideo = vehicularVideo;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}