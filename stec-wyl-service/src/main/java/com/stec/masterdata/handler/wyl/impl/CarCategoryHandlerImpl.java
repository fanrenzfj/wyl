package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.CarCategory;
import com.stec.masterdata.handler.wyl.CarCategoryHandler;
import com.stec.masterdata.service.wyl.CarCategoryService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/30
 * Time: 15:54
 */
@Service
@Component
public class CarCategoryHandlerImpl extends AdvMySqlHandlerService<CarCategoryService, CarCategory, Long> implements CarCategoryHandler {
}
