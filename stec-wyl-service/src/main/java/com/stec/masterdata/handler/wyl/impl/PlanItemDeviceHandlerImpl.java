package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.PlanItemDevice;
import com.stec.masterdata.handler.wyl.PlanItemDeviceHandler;
import com.stec.masterdata.service.wyl.PlanItemDeviceService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:02
 */
@Service
@Component
public class PlanItemDeviceHandlerImpl extends AdvMySqlHandlerService<PlanItemDeviceService, PlanItemDevice, Long> implements PlanItemDeviceHandler {
}
