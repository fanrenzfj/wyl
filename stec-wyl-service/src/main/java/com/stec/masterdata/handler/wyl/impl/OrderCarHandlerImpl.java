package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.entity.wyl.OrderCar;
import com.stec.masterdata.handler.wyl.OrderCarHandler;
import com.stec.masterdata.service.wyl.OrderCarService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 19:46
 */
@Service
@Component
public class OrderCarHandlerImpl extends AdvMySqlHandlerService<OrderCarService, OrderCar, Long> implements OrderCarHandler {
    @Override
    public void saveCars(Long orderId, List<Long> carIds) throws DataServiceException {
        this.privateService.saveCars(orderId, carIds);
    }

    @Override
    public List<Car> selectCars(Long orderId) throws DataServiceException {
        return this.privateService.selectCars(orderId);
    }
}
