package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.handler.wyl.PlanItemHandler;
import com.stec.masterdata.service.wyl.PlanItemService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:01
 */
@Service
@Component
public class PlanItemHandlerImpl extends AdvMySqlHandlerService<PlanItemService, PlanItem, Long> implements PlanItemHandler {
}
