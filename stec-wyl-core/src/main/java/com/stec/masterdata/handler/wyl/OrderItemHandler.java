package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderItemRecord;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 9:56
 */
public interface OrderItemHandler extends IAdvMySqlHandlerService<OrderItem, Long> {

    OrderItemRecord record(OrderItem entity, OrderItemRecord record, List<Long> deviceList) throws DataServiceException;

}
