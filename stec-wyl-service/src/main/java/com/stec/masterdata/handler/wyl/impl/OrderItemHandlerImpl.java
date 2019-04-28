package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderItemRecord;
import com.stec.masterdata.handler.wyl.OrderItemHandler;
import com.stec.masterdata.service.wyl.OrderItemService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:05
 */
@Service
@Component
public class OrderItemHandlerImpl extends AdvMySqlHandlerService<OrderItemService, OrderItem, Long> implements OrderItemHandler {
    @Override
    public OrderItemRecord record(OrderItem entity, OrderItemRecord record, List<Long> deviceList) throws DataServiceException {
        return this.privateService.record(entity, record, deviceList);
    }
}
