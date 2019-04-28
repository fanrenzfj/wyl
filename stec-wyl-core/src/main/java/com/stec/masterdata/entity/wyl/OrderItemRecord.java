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
 * Time: 8:40
 */
@Entity
@Table(name = "order_item_record")
public class OrderItemRecord extends MasterEntity<String> {
    private static final long serialVersionUID = 3458482255363496821L;

    private Long orderItemId;

    private Long recordUserId;

    private String recordUser;

    private Date recordDate;

    private Long docId;

    private String remark;

    private Boolean relatedDevice;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getRecordUserId() {
        return recordUserId;
    }

    public void setRecordUserId(Long recordUserId) {
        this.recordUserId = recordUserId;
    }

    public String getRecordUser() {
        return recordUser;
    }

    public void setRecordUser(String recordUser) {
        this.recordUser = recordUser;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
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

    public Boolean getRelatedDevice() {
        return relatedDevice;
    }

    public void setRelatedDevice(Boolean relatedDevice) {
        this.relatedDevice = relatedDevice;
    }
}