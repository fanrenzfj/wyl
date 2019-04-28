package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.Car;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/22
 * Time: 17:55
 */
public interface CarHandler extends IAdvMySqlHandlerService<Car, Long> {
    boolean deleteEntity(Long id);
    Car saveEntity(Car entity,String[] carUsage,String[] carCategory);
}
