package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 18:30
 */
@Entity
@Table(name ="traction")
public class Traction extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411255L;
    private Date startTime;
    private Date endTime;
    private Double money;
    private String type;
    private String position;
    private String detailPosition;
    private Long userId;
    private String userName;
    private String mobile;
    private Long carId;
    private String reason;
    private String tractionDescribe;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
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

    public String getDetailPosition() {
        return detailPosition;
    }

    public void setDetailPosition(String detailPosition) {
        this.detailPosition = detailPosition;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTractionDescribe() {
        return tractionDescribe;
    }

    public void setTractionDescribe(String tractionDescribe) {
        this.tractionDescribe = tractionDescribe;
    }
}