package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.wyl.OrderItemDevice;
import com.stec.masterdata.service.project.DeviceService;
import com.stec.masterdata.service.wyl.OrderItemDeviceService;
import com.stec.utils.CollectionUtils;
import com.stec.utils.MapUtils;
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
 * Time: 10:06
 */
@Service
public class OrderItemDeviceServiceImpl extends AdvSqlDaoImpl<OrderItemDevice, String> implements OrderItemDeviceService {

    @Autowired
    private DeviceService deviceService;

    @Override
    public void saveDevices(Long orderItemId, List<Long> deviceIds) throws DataServiceException {
        OrderItemDevice param = new OrderItemDevice();
        param.setOrderItemId(orderItemId);
        delete(param);

        if (CollectionUtils.isNotEmpty(deviceIds)) {
            List<OrderItemDevice> saveList = new ArrayList<>(deviceIds.size());
            for (Long deviceId : deviceIds) {
                OrderItemDevice orderItemDevice = new OrderItemDevice();
                orderItemDevice.setOrderItemId(orderItemId);
                orderItemDevice.setDeviceId(deviceId);
                saveList.add(orderItemDevice);
            }
            save(saveList);
        }
    }

    @Override
    public List<Device> selectDevices(Long orderItemId) throws DataServiceException {
        List<Device> deviceList = new ArrayList<>();
        if (ObjectUtils.isNotNull(orderItemId)) {
            List<OrderItemDevice> list = selectEntities(MapUtils.newHashMap("orderItemId", orderItemId));
            if (CollectionUtils.isNotEmpty(list)) {
                List<Long> deviceIds = new ArrayList<>(list.size());
                for (OrderItemDevice orderItemDevice : list) {
                    deviceIds.add(orderItemDevice.getDeviceId());
                }
                deviceList = deviceService.selectBatchByIds(deviceIds);
            }
        }
        return deviceList;
    }
}
