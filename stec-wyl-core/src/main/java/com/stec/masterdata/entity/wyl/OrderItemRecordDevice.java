package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 8:41
 */
@Entity
@Table(name = "order_item_record_device")
public class OrderItemRecordDevice extends MasterEntity<String> {

    private static final long serialVersionUID = 8472984108031644476L;

    private Long itemRecordId;

    private Long deviceId;

    public Long getItemRecordId() {
        return itemRecordId;
    }

    public void setItemRecordId(Long itemRecordId) {
        this.itemRecordId = itemRecordId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}