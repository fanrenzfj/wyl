package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.entity.wyl.*;
import com.stec.masterdata.service.basic.SysDicService;
import com.stec.masterdata.service.wyl.*;
import com.stec.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
public class WorkOrderServiceImpl extends AdvSqlDaoImpl<WorkOrder, String> implements WorkOrderService {

    @Autowired
    private OrderMaterialService orderMaterialService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderCarService orderCarService;

    @Autowired
    private DefectService defectService;

    @Autowired
    private WorkPlanService workPlanService;

    @Autowired
    private PlanItemService planItemService;

    @Autowired
    private PlanItemDeviceService planItemDeviceService;

    @Autowired
    private SysDicService sysDicService;

    @Override
    public WorkOrder save(WorkOrder workOrder) throws DataServiceException {
        workOrder = super.save(workOrder);
        if(StringUtils.equals(workOrder.getProcess(), WorkOrder.ORDER_PROCESS_CONFIRM) && StringUtils.equals(workOrder.getSource(), WorkOrder.ORDER_SOURCE_DEFECT)) {
            defectService.completeDefectFromOrder(workOrder.getId());
        }
        return workOrder;
    }

    @Override
    public WorkOrder save(WorkOrder entity, List<OrderItem> items, List<List<Long>> deviceLists) throws DataServiceException {

        if (CollectionUtils.isEmpty(items)) {
            throw new DataServiceException("工单项不能为空！");
        }
        if (items.size() != deviceLists.size()) {
            throw new DataServiceException("计划项与计划项设备不对等！");
        }
        if (ObjectUtils.isNotNull(entity.getId())) {
            WorkOrder eldOrder = selectByPrimaryKey(entity.getId());
            if (!StringUtils.equals(WorkOrder.ORDER_PROCESS_CREATE, eldOrder.getProcess())) {
                throw new DataServiceException("只能编辑未分配的工单！");
            }

            OrderItem param = new OrderItem();
            param.setWorkOrderId(entity.getId());
            List<OrderItem> eldList = orderItemService.selectEntities(param);
            if (CollectionUtils.isNotEmpty(eldList)) {
                List<Long> newList = new ArrayList<>();
                for (OrderItem item : items) {
                    if (ObjectUtils.isNotNull(item.getId())) {
                        newList.add(item.getId());
                    }
                }
                for (OrderItem eldItem : eldList) {
                    if (!newList.contains(eldItem.getId())) {
                        orderItemService.deleteByPrimaryKey(eldItem.getId());
                    }
                }
            }
        }
        entity.setProcess(WorkOrder.ORDER_PROCESS_CREATE);
        entity = save(entity);

        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            item.setWorkOrderId(entity.getId());
            orderItemService.save(item, deviceLists.get(i));
        }
        return entity;
    }

    @Override
    public void assign(WorkOrder entity, List<Long> carList) throws DataServiceException {
        OrderCar param = new OrderCar();
        param.setOrderId(entity.getId());
        orderCarService.delete(param);
        if (CollectionUtils.isNotEmpty(carList)) {
            List<OrderCar> saveList = new ArrayList<>();
            for (Long car : carList) {
                OrderCar orderCar = new OrderCar();
                orderCar.setOrderId(entity.getId());
                orderCar.setCarId(car);
                saveList.add(orderCar);
            }
            orderCarService.save(saveList);
        }
        save(entity);
    }

    @Override
    public boolean matchProcess(Long id, String process) throws DataServiceException {
        WorkOrder entity = selectByPrimaryKey(id);
        return StringUtils.equals(entity.getProcess(), process);
    }

    @Override
    public List<OrderMaterial> addMaterials(Long id, List<OrderMaterial> materials) throws DataServiceException {
        List<OrderMaterial> eldList = orderMaterialService.selectEntities(MapUtils.newHashMap("orderId", id));
        if (CollectionUtils.isNotEmpty(eldList)) {
            List<Long> newIds = new ArrayList<>();
            for (OrderMaterial material : materials) {
                if(ObjectUtils.isNotNull(material.getId())) {
                    newIds.add(material.getId());
                }
            }
            for (OrderMaterial orderMaterial : eldList) {
                if(!newIds.contains(orderMaterial.getId())) {
                    orderMaterialService.delete(orderMaterial);
                }
            }
        }
        for (OrderMaterial material : materials) {
            material.setOrderId(id);
        }
        orderMaterialService.save(materials);
        return orderMaterialService.selectEntities(MapUtils.newHashMap("orderId", id));
    }

    @Override
    public WorkOrder createFromWorkPlan(WorkPlan entity) throws DataServiceException {
        List<PlanItem> items = planItemService.selectEntities(MapUtils.newHashMap("workPlanId", entity.getId()));
        List<List<Long>> deviceLists = new ArrayList<>(items.size());
        for (PlanItem item : items) {
            List<Long> deviceIds;
            if(item.getRelatedDevice()){
                List<PlanItemDevice> deviceList = planItemDeviceService.selectEntities(MapUtils.newHashMap("planItemId", item.getId()));
                deviceIds = new ArrayList<>(deviceList.size());
                for (PlanItemDevice planItemDevice : deviceList) {
                    deviceIds.add(planItemDevice.getDeviceId());
                }
            }
            else {
                deviceIds = new ArrayList<>(0);
            }
            deviceLists.add(deviceIds);
        }


        Date start = entity.getStartDate();
        Date end = entity.getEndDate();
        String frequency = entity.getFrequency();
        Date startDate = DataTimeUtils.ceilDate(start, frequency);
        SysDic type = sysDicService.selectSysDicByCode(WorkOrder.ORDER_SOURCE_PLAN, entity.getType());

        while (!startDate.after(end)) {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setProjectId(entity.getProjectId());
            workOrder.setSource(WorkOrder.ORDER_SOURCE_PLAN);
            workOrder.setPlanStartDate(startDate);

            workOrder.setPlanEndDate(new Date(DataTimeUtils.next(startDate, frequency).getTime() - DataTimeUtils.ONE_SECOND));

            workOrder.setType(entity.getType());
            if(ObjectUtils.isNotNull(type)){
                workOrder.setTypeName(type.getName());
            }
            workOrder.setRemark(entity.getRemark());
            workOrder.setName(entity.getName() + TimeUtil.format(startDate, "yyyyMMdd"));
            workOrder.setCreateDate(TimeUtil.now());
            workOrder.setCreateUserId(entity.getConfirmUserId());
            workOrder.setWorkPlanId(entity.getId());

            List<OrderItem> orderItems = new ArrayList<>(items.size());
            for (PlanItem item : items) {

                OrderItem orderItem = new OrderItem();
                orderItem.setCreateDate(TimeUtil.now());
                orderItem.setRemark(item.getRemark());
                orderItem.setStructureId(item.getStructureId());
                orderItem.setStructureName(item.getStructureName());
                orderItem.setCreateUserId(entity.getConfirmUserId());
                orderItems.add(orderItem);
            }
            save(workOrder, orderItems, deviceLists);
            startDate = DataTimeUtils.next(startDate, frequency);
        }

        return null;
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) throws DataServiceException {
        OrderItem param = new OrderItem();
        param.setWorkOrderId(id);
        List<OrderItem> items = orderItemService.selectEntities(param);
        for (OrderItem item : items) {
            orderItemService.deleteByPrimaryKey(item.getId());
        }
        return super.deleteByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> countBySource(Map<String, Object> map) {
        return this.selectMapperOne("com.stec.masterdata.mapper.wyl.WorkOrderMapper.countBySource",map);
    }

    @Override
    public Map<String, Object> countByProcess(Map<String, Object> map) {
        return this.selectMapperOne("com.stec.masterdata.mapper.wyl.WorkOrderMapper.countByProcess",map);
    }

}
