package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.entity.wyl.OrderCar;
import com.stec.masterdata.service.wyl.CarService;
import com.stec.masterdata.service.wyl.OrderCarService;
import com.stec.utils.CollectionUtils;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 19:46
 */
@Service
public class OrderCarServiceImpl extends AdvSqlDaoImpl<OrderCar, String> implements OrderCarService {

    @Autowired
    private CarService carService;

    @Override
    public void saveCars(Long orderId, List<Long> carIds) throws DataServiceException {
        OrderCar param = new OrderCar();
        param.setOrderId(orderId);
        delete(param);
        if(CollectionUtils.isNotEmpty(carIds)) {
            List<OrderCar> saveList = new ArrayList<>(carIds.size());
            for (Long carId : carIds) {
                OrderCar orderCar = new OrderCar();
                orderCar.setOrderId(orderId);
                orderCar.setCarId(carId);
                saveList.add(orderCar);
            }
            save(saveList);
        }
    }

    @Override
    public List<Car> selectCars(Long orderId) throws DataServiceException {
        List<Car> carList = new ArrayList<>();
        if(ObjectUtils.isNotNull(orderId)) {
            List<OrderCar> list = selectEntities(MapUtils.newHashMap("orderId", orderId));
            if(CollectionUtils.isNotEmpty(list)) {
                List<Long> carIds = new ArrayList<>(list.size());
                for (OrderCar orderCar : list) {
                    carIds.add(orderCar.getCarId());
                }
                carList = carService.selectBatchByIds(carIds);
            }
        }
        return carList;
    }
}
