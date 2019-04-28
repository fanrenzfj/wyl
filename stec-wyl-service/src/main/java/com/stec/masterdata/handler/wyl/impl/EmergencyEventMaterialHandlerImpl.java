package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EmergencyEventMaterial;
import com.stec.masterdata.handler.wyl.EmergencyEventMaterialHandler;
import com.stec.masterdata.service.wyl.EmergencyEventMaterialService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 9:02
 */
@Service
@Component
public class EmergencyEventMaterialHandlerImpl extends AdvMySqlHandlerService<EmergencyEventMaterialService, EmergencyEventMaterial, Long> implements EmergencyEventMaterialHandler {
}
