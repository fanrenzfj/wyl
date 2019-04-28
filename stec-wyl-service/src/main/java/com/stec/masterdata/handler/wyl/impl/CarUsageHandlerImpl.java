package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.CarUsage;
import com.stec.masterdata.handler.wyl.CarUsageHandler;
import com.stec.masterdata.service.wyl.CarUsageService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/30
 * Time: 15:53
 */
@Service
@Component
public class CarUsageHandlerImpl extends AdvMySqlHandlerService<CarUsageService, CarUsage, Long> implements CarUsageHandler {
}
