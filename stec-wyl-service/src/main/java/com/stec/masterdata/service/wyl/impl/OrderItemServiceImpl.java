package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderItemDevice;
import com.stec.masterdata.entity.wyl.OrderItemRecord;
import com.stec.masterdata.entity.wyl.OrderItemRecordDevice;
import com.stec.masterdata.service.wyl.OrderItemDeviceService;
import com.stec.masterdata.service.wyl.OrderItemRecordDeviceService;
import com.stec.masterdata.service.wyl.OrderItemRecordService;
import com.stec.masterdata.service.wyl.OrderItemService;
import com.stec.utils.CollectionUtils;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:04
 */
@Service
public class OrderItemServiceImpl extends AdvSqlDaoImpl<OrderItem, String> implements OrderItemService {

    @Autowired
    private OrderItemDeviceService orderItemDeviceService;

    @Autowired
    private OrderItemRecordService orderItemRecordService;

    @Autowired
    private OrderItemRecordDeviceService orderItemRecordDeviceService;

    @Override
    public void save(OrderItem entity, List<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            entity.setRelatedDevice(false);
        } else {
            entity.setRelatedDevice(true);
        }
        if (ObjectUtils.isNull(entity.getAction())) {
            entity.setAction(false);
        }
        if (ObjectUtils.isNull(entity.getId())) {
            entity = save(entity);
        } else {
            entity = save(entity);
            OrderItemDevice delParam = new OrderItemDevice();
            delParam.setOrderItemId(entity.getId());
            orderItemDeviceService.delete(delParam);
        }
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            List<OrderItemDevice> saveList = new ArrayList<>();
            for (Long deviceId : deviceIds) {
                OrderItemDevice item = new OrderItemDevice();
                item.setOrderItemId(entity.getId());
                item.setDeviceId(deviceId);
                saveList.add(item);
            }
            orderItemDeviceService.save(saveList);
        }
    }

    @Override
    public OrderItemRecord record(OrderItem entity, OrderItemRecord record, List<Long> deviceList) throws DataServiceException {
        if (ObjectUtils.isNotNull(record.getId())) {
            OrderItemRecordDevice param = new OrderItemRecordDevice();
            param.setItemRecordId(record.getId());
            orderItemRecordDeviceService.delete(param);
        }
        record = orderItemRecordService.save(record);
        if (record.getRelatedDevice()) {
            List<OrderItemRecordDevice> saveList = new ArrayList<>(deviceList.size());
            for (Long deviceId : deviceList) {
                OrderItemRecordDevice device = new OrderItemRecordDevice();
                device.setItemRecordId(record.getId());
                device.setDeviceId(deviceId);
                saveList.add(device);
            }
            orderItemRecordDeviceService.save(saveList);
        }
        entity.setAction(true);
        this.save(entity);

        return record;
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        OrderItemDevice delParam = new OrderItemDevice();
        delParam.setOrderItemId(id);
        orderItemDeviceService.delete(delParam);
        return super.deleteByPrimaryKey(id);
    }
}
