package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.entity.wyl.CarCategory;
import com.stec.masterdata.entity.wyl.CarUsage;
import com.stec.masterdata.service.basic.DocInfoService;
import com.stec.masterdata.service.wyl.CarCategoryService;
import com.stec.masterdata.service.wyl.CarService;
import com.stec.masterdata.service.wyl.CarUsageService;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/22
 * Time: 17:56
 */
@Service
public class CarServiceImpl extends AdvSqlDaoImpl<Car, String> implements CarService {
    @Autowired
    DocInfoService docInfoService;
    @Autowired
    CarService carService;
    @Autowired
    CarUsageService carUsageService;
    @Autowired
    CarCategoryService carCategoryService;
    @Override
    public boolean deleteEntity(Long id) {
        Car car=this.selectByPrimaryKey(id);
        docInfoService.deleteByPrimaryKey(car.getDocId());
        CarUsage carUsage=new CarUsage();
        carUsage.setCarId(id);
        carUsageService.delete(carUsage);
        CarCategory carCategory=new CarCategory();
        carCategory.setCarId(id);
        carCategoryService.delete(carCategory);
        return this.deleteByPrimaryKey(id);
    }

    @Override
    public Car saveEntity(Car entity, String[] carUsage, String[] carCategory) {
        if (ObjectUtils.isNull(entity.getId())) {
            entity=carService.save(entity);
        } else {
            entity=carService.save(entity);
            CarUsage carUsage1=new CarUsage();
            carUsage1.setCarId(entity.getId());
            carUsageService.delete(carUsage1);
            CarCategory carCategory1=new CarCategory();
            carCategory1.setCarId(entity.getId());
            carCategoryService.delete(carCategory1);
        }
        List<CarUsage> carUsageList=new ArrayList<>();
        for (String s : carUsage) {
            CarUsage carUsage2=new CarUsage();
            carUsage2.setCarId(entity.getId());
            carUsage2.setCode(s);
            carUsageList.add(carUsage2);
        }
        carUsageService.save(carUsageList);
        List<CarCategory> carCategoryList=new ArrayList<>();
        for (String s : carCategory) {
            CarCategory carCategory2=new CarCategory();
            carCategory2.setCarId(entity.getId());
            carCategory2.setCode(s);
            carCategoryList.add(carCategory2);
        }
        carCategoryService.save(carCategoryList);
        return entity;
    }
}
