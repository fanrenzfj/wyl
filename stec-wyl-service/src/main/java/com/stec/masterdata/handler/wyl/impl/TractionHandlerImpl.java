package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.Traction;
import com.stec.masterdata.handler.wyl.TractionHandler;
import com.stec.masterdata.service.wyl.TractionService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 18:48
 */
@Service
@Component
public class TractionHandlerImpl extends AdvMySqlHandlerService<TractionService, Traction, Long> implements TractionHandler {
}
