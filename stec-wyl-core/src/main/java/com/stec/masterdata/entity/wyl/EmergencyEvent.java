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
 * Time: 15:14
 */
@Entity
@Table(name ="emergency_event")
public class EmergencyEvent extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411249L;
    public static final String EMERGENCY_EVENT_STATUS_CONFIRM = "0";
    public static final String EMERGENCY_EVENT_STATUS_HANDLE = "10";
    public static final String EMERGENCY_EVENT_STATUS_ASSESSMENT = "20";
    public static final String EMERGENCY_EVENT_STATUS_COMPLETE = "30";
    private Date discoveryTime;
    private String eventType;
    private String source;
    private String level;
    private String position;
    private String detailPosition;
    private Long userId;
    private String mobile;
    private String remark;
    private String status;
    private String userName;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    private Long docId;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Date getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(Date discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDetailPosition() {
        return detailPosition;
    }

    public void setDetailPosition(String detailPosition) {
        this.detailPosition = detailPosition;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}