package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.wyl.OrderItemDevice;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:06
 */
public interface OrderItemDeviceService extends IAdvMySqlService<OrderItemDevice, Long> {

    void saveDevices(Long orderItemId, List<Long> deviceIds) throws DataServiceException;

    List<Device> selectDevices(Long orderItemId) throws DataServiceException;
}
