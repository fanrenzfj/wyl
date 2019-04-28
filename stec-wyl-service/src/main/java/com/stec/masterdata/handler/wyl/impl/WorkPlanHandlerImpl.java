package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.entity.wyl.WorkPlan;
import com.stec.masterdata.handler.wyl.WorkPlanHandler;
import com.stec.masterdata.service.wyl.WorkPlanService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 9:59
 */
@Service
@Component
public class WorkPlanHandlerImpl extends AdvMySqlHandlerService<WorkPlanService, WorkPlan, Long> implements WorkPlanHandler {
    @Override
    public void save(WorkPlan entity, List<PlanItem> items, List<List<Long>> deviceLists) throws DataServiceException {
        this.privateService.save(entity, items, deviceLists);
    }
}
