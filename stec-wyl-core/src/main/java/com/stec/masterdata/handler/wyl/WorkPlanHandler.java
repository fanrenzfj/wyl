package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.entity.wyl.WorkPlan;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 9:53
 */
public interface WorkPlanHandler extends IAdvMySqlHandlerService<WorkPlan, Long> {

    public void save(WorkPlan entity, List<PlanItem> items, List<List<Long>> deviceLists) throws DataServiceException;
}
