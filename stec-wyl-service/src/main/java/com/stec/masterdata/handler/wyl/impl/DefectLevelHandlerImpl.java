package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.DefectLevel;
import com.stec.masterdata.handler.wyl.DefectLevelHandler;
import com.stec.masterdata.service.wyl.DefectLevelService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 13:32
 */
@Service
@Component
public class DefectLevelHandlerImpl extends AdvMySqlHandlerService<DefectLevelService, DefectLevel, Long> implements DefectLevelHandler {
}
