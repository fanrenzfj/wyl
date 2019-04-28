package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.EmergencyEvent;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderMaterial;
import com.stec.masterdata.entity.wyl.WorkOrder;
import com.stec.masterdata.handler.wyl.WorkOrderHandler;
import com.stec.masterdata.service.wyl.WorkOrderService;
import com.stec.utils.NumberUtils;
import com.stec.utils.StringUtils;
import com.stec.utils.TimeUtil;
import org.springframework.stereotype.Component;

import java.sql.Wrapper;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:03
 */
@Service
@Component
public class WorkOrderHandlerImpl extends AdvMySqlHandlerService<WorkOrderService, WorkOrder, Long> implements WorkOrderHandler {
    @Override
    public WorkOrder save(WorkOrder entity, List<OrderItem> items, List<List<Long>> deviceLists) throws DataServiceException {
        return this.privateService.save(entity, items, deviceLists);
    }

    @Override
    public void assign(WorkOrder entity, List<Long> carList) throws DataServiceException {
        this.privateService.assign(entity, carList);
    }

    @Override
    public boolean matchProcess(Long id, String process) throws DataServiceException {
        return this.privateService.matchProcess(id, process);
    }

    @Override
    public List<OrderMaterial> addMaterials(Long id, List<OrderMaterial> materials) throws DataServiceException {
        return this.privateService.addMaterials(id, materials);
    }

    @Override
    public Map<String, Object> countBySource(Map<String, Object> map) throws DataServiceException {
        return this.privateService.countBySource(map);
    }

    @Override
    public Map<String, Object> countByProcess(Map<String, Object> map) throws DataServiceException {
        return this.privateService.countByProcess(map);
    }

    @Override
    public Map<String, Object> weekOrderComplete() throws DataServiceException {
        Map<String, Object> map = new HashMap<>();
        //计划工单数量
        Integer planNum = countWorkOrders("plan_type", "");
        //临时工单数量
        Integer temNum = countWorkOrders("temporary_type", "");
        //缺陷工单数量
        Integer defectNum = countWorkOrders("defect_type", "");
        //计划工单数量
        Integer planCompleteNum = countWorkOrders("plan_type", "30,40");
        //临时工单数量
        Integer temCompleteNum = countWorkOrders("temporary_type", "30,40");
        //缺陷工单数量
        Integer defectCompleteNum = countWorkOrders("defect_type", "30,40");
        map.put("planNum", planNum);
        map.put("temNum", temNum);
        map.put("defectNum", defectNum);
        map.put("planCompleteNum", planCompleteNum);
        map.put("temCompleteNum", temCompleteNum);
        map.put("defectCompleteNum", defectCompleteNum);
        if (planNum == null || planNum == 0) {
            map.put("planRate", 0.0);
        } else {
            map.put("planRate", NumberUtils.format(Double.valueOf(planCompleteNum.toString()) / Double.valueOf(planNum.toString()), "0.00"));
        }

        if (temNum == null || temNum == 0) {
            map.put("temRate", 0.0);
        } else {
            map.put("temRate", NumberUtils.format(Double.valueOf(temCompleteNum.toString()) / Double.valueOf(temNum.toString()), "0.00"));
        }
        if (defectNum == null || defectNum == 0) {
            map.put("defectRate", 0.0);
        } else {
            map.put("defectRate", NumberUtils.format(Double.valueOf(defectCompleteNum.toString()) / Double.valueOf(defectNum.toString()), "0.00"));
        }
        return map;
    }


    Integer countWorkOrders(String source, String process) {
        try {
            Date beginTime = TimeUtil.parseDate(TimeUtil.format(TimeUtil.firstDayOfWeek(TimeUtil.calendar(new Date()), 2)));
            Date endTime = TimeUtil.parseDate(TimeUtil.format(TimeUtil.lastDayOfWeek(TimeUtil.calendar(new Date()), 7)));
            EntityWrapper<WorkOrder> workOrderWrapper = new EntityWrapper<>();
            workOrderWrapper.ge("create_date", beginTime);
            workOrderWrapper.lt("create_date", endTime);
            if (StringUtils.isNotBlank(source)) {
                workOrderWrapper.eq("source", source);
            }
            if (StringUtils.isNotBlank(process)) {
                workOrderWrapper.in("process", process);
            }
            List<WorkOrder> workOrders = this.privateService.selectEntities(workOrderWrapper);
            return workOrders.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
