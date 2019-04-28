package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.OrderItemRecordDevice;
import com.stec.masterdata.handler.wyl.OrderItemRecordDeviceHandler;
import com.stec.masterdata.service.wyl.OrderItemRecordDeviceService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:09
 */
@Service
@Component
public class OrderItemRecordDeviceHandlerImpl extends AdvMySqlHandlerService<OrderItemRecordDeviceService, OrderItemRecordDevice, Long> implements OrderItemRecordDeviceHandler {
}
