package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.wyl.OrderItemDevice;
import com.stec.masterdata.handler.wyl.OrderItemDeviceHandler;
import com.stec.masterdata.service.wyl.OrderItemDeviceService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:06
 */
@Service
@Component
public class OrderItemDeviceHandlerImpl extends AdvMySqlHandlerService<OrderItemDeviceService, OrderItemDevice, Long> implements OrderItemDeviceHandler {
    @Override
    public void saveDevices(Long orderItemId, List<Long> deviceIds) throws DataServiceException {

    }

    @Override
    public List<Device> selectDevices(Long orderItemId) throws DataServiceException {
        return null;
    }
}
