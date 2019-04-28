package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.entity.wyl.PlanItemDevice;
import com.stec.masterdata.entity.wyl.WorkOrder;
import com.stec.masterdata.entity.wyl.WorkPlan;
import com.stec.masterdata.service.basic.SysDicService;
import com.stec.masterdata.service.wyl.PlanItemDeviceService;
import com.stec.masterdata.service.wyl.PlanItemService;
import com.stec.masterdata.service.wyl.WorkOrderService;
import com.stec.masterdata.service.wyl.WorkPlanService;
import com.stec.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 9:59
 */
@Service
public class WorkPlanServiceImpl extends AdvSqlDaoImpl<WorkPlan, String> implements WorkPlanService {

    @Autowired
    private PlanItemService planItemService;

    @Autowired
    private PlanItemDeviceService planItemDeviceService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private SysDicService sysDicService;

    @Override
    public void save(WorkPlan entity, List<PlanItem> items, List<List<Long>> deviceLists) throws DataServiceException {
        if (CollectionUtils.isEmpty(items)) {
            throw new DataServiceException("计划项不能为空！");
        }
        //临时为了应付文一路现场录入计划时候  不选择设施设备   只输入名称
//        if (items.size() != deviceLists.size()) {
//            throw new DataServiceException("计划项与计划项设备不对等！");
//        }
        if (ObjectUtils.isNotNull(entity.getId())) {
            WorkPlan eldPlan = selectByPrimaryKey(entity.getId());
            if(StringUtils.equals(WorkPlan.PLAN_STATUS_CONFIRM, eldPlan.getStatus())) {
                throw new DataServiceException("不能编辑已经审核通过的计划！");
            }

            PlanItem param = new PlanItem();
            param.setWorkPlanId(entity.getId());
            List<PlanItem> eldList = planItemService.selectEntities(param);
            if (CollectionUtils.isNotEmpty(eldList)) {
                List<Long> newList = new ArrayList<>();
                for (PlanItem item : items) {
                    if (ObjectUtils.isNotNull(item.getId())) {
                        newList.add(item.getId());
                    }
                }
                for (PlanItem eldItem : eldList) {
                    if (!newList.contains(eldItem.getId())) {
                        planItemService.deleteByPrimaryKey(eldItem.getId());
                    }
                }
            }
        }
        entity.setStatus(WorkPlan.PLAN_STATUS_CREATE);
        entity = save(entity);

        for (int i = 0; i < items.size(); i++) {
            PlanItem item = items.get(i);
            item.setWorkPlanId(entity.getId());
            planItemService.save(item, deviceLists.get(i));
        }
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) throws DataServiceException{
        PlanItem param = new PlanItem();
        param.setWorkPlanId(id);
        List<PlanItem> items = planItemService.selectEntities(param);
        for (PlanItem item : items) {
            planItemService.deleteByPrimaryKey(item.getId());
        }
        return super.deleteByPrimaryKey(id);
    }

    @Override
    public WorkPlan save(WorkPlan entity) throws DataServiceException {
        entity =  super.save(entity);
        if(StringUtils.equals(entity.getStatus(), WorkPlan.PLAN_STATUS_CONFIRM)) {
            workOrderService.createFromWorkPlan(entity);
        }
        return entity;
    }
}
