package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EmergencyEventCar;
import com.stec.masterdata.handler.wyl.EmergencyEventCarHandler;
import com.stec.masterdata.service.wyl.EmergencyEventCarService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 9:08
 */
@Service
@Component
public class EmergencyEventCarHandlerImpl extends AdvMySqlHandlerService<EmergencyEventCarService, EmergencyEventCar, Long> implements EmergencyEventCarHandler {
}
