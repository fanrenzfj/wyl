package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.OrderItemRecord;
import com.stec.masterdata.handler.wyl.OrderItemRecordHandler;
import com.stec.masterdata.service.wyl.OrderItemRecordService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:08
 */
@Service
@Component
public class OrderItemRecordHandlerImpl extends AdvMySqlHandlerService<OrderItemRecordService, OrderItemRecord, Long> implements OrderItemRecordHandler {
}
