package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EmergencyProgramme;
import com.stec.masterdata.handler.wyl.EmergencyProgrammeHandler;
import com.stec.masterdata.service.wyl.EmergencyProgrammeService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 17:33
 */
@Service
@Component
public class EmergencyProgrammeHandlerImpl extends AdvMySqlHandlerService<EmergencyProgrammeService, EmergencyProgramme, Long> implements EmergencyProgrammeHandler {
}
