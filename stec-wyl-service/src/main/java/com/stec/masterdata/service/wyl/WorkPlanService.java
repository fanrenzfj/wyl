package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.entity.wyl.WorkPlan;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 9:58
 */
public interface WorkPlanService extends IAdvMySqlService<WorkPlan, Long> {

    public void save(WorkPlan entity, List<PlanItem> items, List<List<Long>> deviceLists) throws DataServiceException;
}
