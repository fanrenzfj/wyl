package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.DefectInspectUser;
import com.stec.masterdata.handler.wyl.DefectInspectUserHandler;
import com.stec.masterdata.service.wyl.DefectInspectUserService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/11 0011
 * Time: 14:59
 */
@Service
@Component
public class DefectInspectUserHandlerImpl extends AdvMySqlHandlerService<DefectInspectUserService, DefectInspectUser, Long> implements DefectInspectUserHandler {
}
