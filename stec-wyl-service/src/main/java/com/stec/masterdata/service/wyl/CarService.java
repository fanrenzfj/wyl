package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.Car;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/22
 * Time: 17:56
 */
public interface CarService extends IAdvMySqlService<Car, Long> {
    boolean deleteEntity(Long id);
    Car saveEntity(Car entity, String[] carUsage, String[] carCategory);
}
