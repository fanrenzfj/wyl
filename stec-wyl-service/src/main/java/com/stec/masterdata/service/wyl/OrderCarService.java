package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.entity.wyl.OrderCar;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 19:45
 */
public interface OrderCarService extends IAdvMySqlService<OrderCar, Long> {

    void saveCars(Long orderId, List<Long> carIds) throws DataServiceException;

    List<Car> selectCars(Long orderId) throws DataServiceException;
}
