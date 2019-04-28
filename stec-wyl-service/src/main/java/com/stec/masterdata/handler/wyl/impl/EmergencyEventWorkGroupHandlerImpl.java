package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EmergencyEventWorkGroup;
import com.stec.masterdata.handler.wyl.EmergencyEventWorkGroupHandler;
import com.stec.masterdata.service.wyl.EmergencyEventWorkGroupService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 9:03
 */
@Service
@Component
public class EmergencyEventWorkGroupHandlerImpl extends AdvMySqlHandlerService<EmergencyEventWorkGroupService, EmergencyEventWorkGroup, Long> implements EmergencyEventWorkGroupHandler {
}
