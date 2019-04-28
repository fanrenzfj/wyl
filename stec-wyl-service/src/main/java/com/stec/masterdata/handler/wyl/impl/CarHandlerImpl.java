package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.handler.wyl.CarHandler;
import com.stec.masterdata.service.wyl.CarService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/22
 * Time: 17:56
 */
@Service
@Component
public class CarHandlerImpl extends AdvMySqlHandlerService<CarService, Car, Long> implements CarHandler {
    @Override
    public boolean deleteEntity(Long id) {
        return this.privateService.deleteEntity(id);
    }

    @Override
    public Car saveEntity(Car entity, String[] carUsage, String[] carCategory) {
        return this.privateService.saveEntity(entity,carUsage,carCategory);
    }
}
