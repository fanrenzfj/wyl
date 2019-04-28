package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderItemRecord;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:04
 */
public interface OrderItemService extends IAdvMySqlService<OrderItem, Long> {

    void save(OrderItem entity, List<Long> deviceIds);

    OrderItemRecord record(OrderItem entity, OrderItemRecord record, List<Long> deviceList) throws DataServiceException;
}
