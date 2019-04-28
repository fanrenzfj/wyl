package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.DefectTypeUsage;
import com.stec.masterdata.handler.wyl.DefectTypeUsageHandler;
import com.stec.masterdata.service.wyl.DefectTypeUsageService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/17 0017
 * Time: 15:59
 */
@Service
@Component
public class DefectTypeUsageHandlerImpl extends AdvMySqlHandlerService<DefectTypeUsageService, DefectTypeUsage, Long> implements DefectTypeUsageHandler {
}
