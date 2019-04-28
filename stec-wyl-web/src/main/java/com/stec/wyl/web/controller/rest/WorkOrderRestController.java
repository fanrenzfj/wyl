package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.wyl.*;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.wyl.*;
import com.stec.utils.*;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import com.stec.wyl.web.service.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 13:46
 */
@Api(tags = {"工单管理"})
@RestController
@RequestMapping(value = "/rest/workOrder", method = RequestMethod.POST)
public class WorkOrderRestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private WorkOrderHandler workOrderHandler;

    @Reference
    private OrderItemHandler orderItemHandler;

    @Reference
    private OrderItemDeviceHandler orderItemDeviceHandler;

    @Reference
    private OrderItemRecordHandler orderItemRecordHandler;

    @Reference
    private OrderItemRecordDeviceHandler orderItemRecordDeviceHandler;

    @Reference
    private WorkGroupHandler workGroupHandler;

    @Reference
    private OrderCarHandler orderCarHandler;

    @Reference
    private CarHandler carHandler;

    @Reference
    private OrderMaterialHandler orderMaterialHandler;

    @Reference
    private DeviceHandler deviceHandler;

    @Reference
    private StructureHandler structureHandler;

    @Reference
    private BlockRoadHandler blockRoadHandler;

    @Reference
    private SysUserHandler sysUserHandler;

    @Reference
    private BlockRoadOrderHandler blockRoadOrderHandler;

    @Reference
    private BlockRoadCarHandler blockRoadCarHandler;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SessionUtils sessionUtils;

    @ApiOperation("养护计划列表，分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody) throws ParseException {
        WorkOrder entity = jsonRequestBody.tryGet(WorkOrder.class);
        PageForm pageForm = jsonRequestBody.getPageForm();
        Long channel = jsonRequestBody.getLong("channel");
        if (ObjectUtils.isNull(channel)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "调用接口渠道类型不能为空");
        }
        if (channel != 0 && channel != 1) {//0:App调用;1:PC调用。
            return ResultForm.createErrorResultForm(jsonRequestBody, "调用接口渠道类型不正确");
        }
        EntityWrapper<WorkOrder> wrapper = new EntityWrapper<>();

        if (ObjectUtils.isNotNull(entity.getWorkPlanId())) {
            wrapper.eq("work_plan_id", entity.getWorkPlanId());
        }
        if (StringUtils.isNotBlank(entity.getSource())) {
            wrapper.eq("source", entity.getSource());
        }
        if (ObjectUtils.isNotNull(entity.getWorkGroupId())) {
            wrapper.eq("work_group_id", entity.getWorkGroupId());
        }
        if (StringUtils.isNotBlank(entity.getProcess())) {
            wrapper.eq("process", entity.getProcess());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        if (StringUtils.isNotBlank(entity.getType())) {
            wrapper.like("type", entity.getType());
        }
        if (ObjectUtils.allIsNotNull(entity.getPlanEndDate(), entity.getPlanEndDate())) {
            //wrapper.le("plan_start_date", entity.getPlanEndDate()).and().ge("plan_end_date", entity.getPlanStartDate());
            wrapper.le("plan_end_date",entity.getPlanEndDate()).and().ge("plan_start_date",entity.getPlanStartDate());
        }
        if (channel == 0) {
            if (entity.getProcess().equals(WorkOrder.ORDER_PROCESS_CREATE)) {
                Date planEndDate = getNextMonthDate();
                wrapper.le("plan_end_date", planEndDate);
            }
        }
        wrapper.orderBy("process");
        DataPaging<WorkOrder> dataPaging = workOrderHandler.selectEntities(wrapper, pageForm);
        List<WorkOrder> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (WorkOrder workOrder : list) {
            WorkGroup workGroup = workGroupHandler.selectByPrimaryKey(workOrder.getWorkGroupId());
            if (ObjectUtils.isNotNull(workGroup)) {
                workOrder.setWorkGroupName(workGroup.getName());
            } else {
                workOrder.setWorkGroupName("");
            }
            Map<String, Object> workOrderMap;
            try {
                workOrderMap = MapUtils.toMapIgnoreNull(workOrder);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(workOrderMap);
            if (workOrderHandler.matchProcess(workOrder.getId(), WorkOrder.ORDER_PROCESS_ASSIGN)) {
                if (workGroupHandler.containMembers(workOrder.getWorkGroupId(), currentSysUserId())) {
                    workOrderMap.put("assignButtonUseAble", true);
                } else {
                    workOrderMap.put("assignButtonUseAble", false);
                }
            }
        }
        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "查询成功！");
    }

    @ApiOperation("封道工单列表，供封道使用")
    @RequestMapping("/blockList")
    public ResultForm blockList(@ApiParam(value = "{start,end}") @RequestBody JsonRequestBody jsonRequestBody) {
        Date start = jsonRequestBody.getDate("start");
        Date end = jsonRequestBody.getDate("end");
        EntityWrapper<WorkOrder> wrapper = new EntityWrapper<>();
        wrapper.le("plan_start_date", end).and().ge("plan_end_date", start);
        List<WorkOrder> orders = workOrderHandler.selectEntities(wrapper);
        List<Map<String, Object>> retList = new ArrayList<>(orders.size());
        for (WorkOrder order : orders) {
            WorkGroup workGroup = workGroupHandler.selectByPrimaryKey(order.getWorkGroupId());
            Map<String, Object> orderMap;
            try {
                orderMap = MapUtils.toMapIgnoreNull(order);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(orderMap);
            if (ObjectUtils.isNotNull(workGroup)) {
                SysUser sysUser = sysUserHandler.selectByPrimaryKey(workGroup.getLeaderId());
                orderMap.put("leaderName", sysUser.getName());
                orderMap.put("leaderMobile", sysUser.getMobile());
            } else {
                orderMap.put("leaderName", "");
                orderMap.put("leaderMobile", "");
            }


        }
        return ResultForm.createSuccessResultForm(retList, "查询成功！");
    }

    @ApiOperation("保存工单")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{\"name\":\"工单名称\",items:[{\"name\":\"工单任务1\",\"structureId\":\"1\",deviceList:[1,2,3]},\n{\"name\":\"工单任务2\",\"structureId\":\"1\",deviceList:[1,2,3]}]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("保存工单", jsonRequestBody);
        WorkOrder entity = jsonRequestBody.tryGet(WorkOrder.class);
        if (!ObjectUtils.allIsNotNull(entity.getName(), entity.getSource())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单名称和来源不能为空！");
        }
        List<OrderItem> items = jsonRequestBody.getList("items", OrderItem.class);

        if (CollectionUtils.isEmpty(items)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单项不能为空！");
        }
        if (ObjectUtils.isNull(entity.getId())) {
            entity.setCreateUserId(currentSysUserId());
            entity.setCreateDate(TimeUtil.now());
        }
        List<List<Long>> deviceLists = new ArrayList<>();

        Date now = TimeUtil.now();
        for (OrderItem item : items) {
            if (ObjectUtils.isNull(item.getStructureId())) {
                return ResultForm.createErrorResultForm(jsonRequestBody, "工单项务必关联设施结构！");
            }
            if (ObjectUtils.isNull(item.getId())) {
                item.setCreateUserId(currentSysUserId());
                item.setCreateDate(now);
            }
        }
        if (ObjectUtils.isNull(entity.getPlanStartDate())) {
            entity.setPlanStartDate(DataTimeUtils.ceilDate(TimeUtil.now(), DataTimeUtils.DAY));
            entity.setPlanEndDate(new Date(entity.getPlanStartDate().getTime() + DataTimeUtils.ONE_DAY));
        }

        JSONArray jsonArray = jsonRequestBody.getJSONArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (ObjectUtils.isNotNull(obj.getJSONArray("deviceList"))) {
                deviceLists.add(JSONArray.parseArray(obj.getJSONArray("deviceList").toJSONString(), Long.class));
            } else {
                deviceLists.add(new ArrayList<>(0));
            }
        }

        try {
            workOrderHandler.save(entity, items, deviceLists);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultForm.createErrorResultForm(jsonArray, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(jsonRequestBody, "保存成功！");
    }

    @ApiOperation("工单删除，只能删除未分配的工单")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("工单删除，只能删除未分配的工单", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        if (!workOrderHandler.matchProcess(id, WorkOrder.ORDER_PROCESS_CREATE)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能删除未分配的工单！");
        }
        workOrderHandler.deleteByPrimaryKey(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "删除成功！");
    }

    @ApiOperation("分配工单，只能分配未分配的工单")
    @RequestMapping("/assign")
    public ResultForm assign(@ApiParam(value = "{id, planStartTime, planEndTime, workGroupId, workGroupName, carList[Long]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("分配工单，只能分配未分配的工单", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        Date planStartTime = jsonRequestBody.getDate("planStartTime");
        Date planEndTime = jsonRequestBody.getDate("planEndTime");
        Long workGroupId = jsonRequestBody.getLong("workGroupId");
        Double hours = jsonRequestBody.getDouble("hours");
        List<Long> carList = jsonRequestBody.getList("carList", Long.class);
        //hours 工时
        if (!ObjectUtils.allIsNotNull(id, planStartTime, planEndTime, workGroupId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单ID、计划开始结束时间，执行班组不能为空！");
        }
        WorkOrder entity = workOrderHandler.selectByPrimaryKey(id);
        if (!StringUtils.equals(WorkOrder.ORDER_PROCESS_CREATE, entity.getProcess())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能分配未分配的工单!");
        }
        if (StringUtils.isEmpty(entity.getCode())) {
            entity.setCode(SequenceUtils.getSecondUID("OD"));
        }
        entity.setWorkGroupName(jsonRequestBody.getString("workGroupName"));
        entity.setWorkGroupId(workGroupId);
        entity.setAssignUserId(currentSysUserId());
        entity.setAssignDate(TimeUtil.now());
        entity.setPlanStartDate(planStartTime);
        entity.setPlanEndDate(planEndTime);
        entity.setHours(hours);
        entity.setProcess(WorkOrder.ORDER_PROCESS_ASSIGN);
        entity = workOrderHandler.save(entity);
        orderCarHandler.saveCars(entity.getId(), carList);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "工单分配成功！");
    }

    @ApiOperation("获取工单详情")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        Map<String, Object> retMap = new HashMap<>();
        try {
            WorkOrder entity = workOrderHandler.selectByPrimaryKey(id);
            if (ObjectUtils.isNotNull(entity)) {
                retMap = MapUtils.toMapIgnoreNull(entity);
                if (ObjectUtils.isNotNull(entity.getWorkGroupId())) {
                    WorkGroup workGroup = workGroupHandler.selectByPrimaryKey(entity.getWorkGroupId());
                    if (ObjectUtils.isNotNull(workGroup)) {
                        SysUser sysUser = sysUserHandler.selectByPrimaryKey(workGroup.getLeaderId());
                        workGroup.setLeaderName(sysUser.getName());
                        workGroup.setMobile(sysUser.getMobile());
                    } else {
                        workGroup.setLeaderName("");
                        workGroup.setMobile("");
                    }
                    retMap.put("workGroup", workGroup);
                }
                if (ObjectUtils.isNotNull(entity.getCreateUserId())) {
                    retMap.put("createUser", sysUserHandler.selectByPrimaryKey(entity.getCreateUserId()));
                }
                if (ObjectUtils.isNotNull(entity.getAssignUserId())) {
                    retMap.put("assignUser", sysUserHandler.selectByPrimaryKey(entity.getAssignUserId()));
                }
                if (ObjectUtils.isNotNull(entity.getConfirmUserId())) {
                    retMap.put("confirmUser", sysUserHandler.selectByPrimaryKey(entity.getConfirmUserId()));
                }
                if (ObjectUtils.isNotNull(entity.getAmountUserId())) {
                    retMap.put("amountUser", sysUserHandler.selectByPrimaryKey(entity.getAmountUserId()));
                }
            }
            retMap.put("cars", orderCarHandler.selectCars(entity.getId()));

            List<OrderMaterial> materials = orderMaterialHandler.selectEntities(MapUtils.newHashMap("orderId", entity.getId()));
            if (CollectionUtils.isNotEmpty(materials)) {
                retMap.put("materials", materials);
            }
            List<BlockRoadOrder> blockRoadOrderList = blockRoadOrderHandler.selectEntities(MapUtils.newHashMap("workOrderId", entity.getId()));
            if (blockRoadOrderList.size() > 0) {
                List<Long> blockRoadOrderIds = new ArrayList<>();
                for (BlockRoadOrder blockRoadOrder : blockRoadOrderList) {
                    blockRoadOrderIds.add(blockRoadOrder.getBlockRoadId());
                }
                List<BlockRoad> blockRoadList = blockRoadHandler.selectBatchByIds(blockRoadOrderIds);
                List<Map<String, Object>> roadList = new ArrayList<>(blockRoadList.size());
                for (BlockRoad blockRoad : blockRoadList) {
                    SysUser sysUser = sysUserHandler.selectByPrimaryKey(blockRoad.getLeaderId());
                    blockRoad.setLeaderName(sysUser.getName());
                    blockRoad.setMobile(sysUser.getMobile());
                    Map<String, Object> roadMap;
                    try {
                        roadMap = MapUtils.toMapIgnoreNull(blockRoad);
                    } catch (Exception e) {
                        return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
                    }
                    if (ObjectUtils.isNotNull(blockRoad)) {
                        List<BlockRoadCar> blockRoadCarList = blockRoadCarHandler.selectEntities(MapUtils.newHashMap("blockRoadId", blockRoad.getId()));
                        List<Long> carIds = new ArrayList<>(blockRoadCarList.size());
                        for (BlockRoadCar blockRoadCar : blockRoadCarList) {
                            carIds.add(blockRoadCar.getCarId());
                        }
                        List<Car> cars = carHandler.selectBatchByIds(carIds);
                        roadMap.put("blockRoadCar", cars);
                    }
                    roadList.add(roadMap);
                }
                retMap.put("blockRoads", roadList);
            }

            if (CollectionUtils.isNotEmpty(materials)) {
                retMap.put("materials", materials);
            }


            List<OrderItem> orderItems = orderItemHandler.selectEntities(MapUtils.newHashMap("workOrderId", entity.getId()));
            if (CollectionUtils.isNotEmpty(orderItems)) {
                List<Map<String, Object>> items = new ArrayList<>(orderItems.size());
                retMap.put("items", items);
                for (OrderItem orderItem : orderItems) {
                    Map<String, Object> itemMap = MapUtils.toMapIgnoreNull(orderItem);
                    items.add(itemMap);
                    if (orderItem.getRelatedDevice()) {
                        List<OrderItemDevice> orderItemDevices = orderItemDeviceHandler.selectEntities(MapUtils.newHashMap("orderItemId", orderItem.getId()));
                        List<Long> deviceIds = new ArrayList<>(orderItemDevices.size());
                        for (OrderItemDevice orderItemDevice : orderItemDevices) {
                            deviceIds.add(orderItemDevice.getDeviceId());
                        }
                        itemMap.put("deviceList", deviceHandler.selectBatchByIds(deviceIds));
                    }
                    if (orderItem.getAction()) {
                        List<OrderItemRecord> orderItemRecords = orderItemRecordHandler.selectEntities(MapUtils.newHashMap("orderItemId", orderItem.getId()));
                        List<Map<String, Object>> records = new ArrayList<>(orderItemRecords.size());
                        itemMap.put("records", records);
                        for (OrderItemRecord record : orderItemRecords) {
                            Map<String, Object> recordMap = MapUtils.toMapIgnoreNull(record);
                            records.add(recordMap);
                            if (record.getRelatedDevice()) {
                                List<OrderItemRecordDevice> orderItemRecordDevices = orderItemRecordDeviceHandler.selectEntities(MapUtils.newHashMap("itemRecordId", record.getId()));
                                List<Long> deviceIds = new ArrayList<>(orderItemRecordDevices.size());
                                for (OrderItemRecordDevice orderItemRecordDevice : orderItemRecordDevices) {
                                    deviceIds.add(orderItemRecordDevice.getDeviceId());
                                }
                                recordMap.put("deviceList", deviceHandler.selectBatchByIds(deviceIds));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(retMap, "获取工单成功！");
    }

    @ApiOperation("工单工作记录")
    @RequestMapping("/record")
    public ResultForm record(MultipartRequest multipartRequest, Long[] delIds, OrderItemRecord entity, Long[] deviceList) {
        logger("工单工作记录", JsonRequestBody.toJSONString(entity));
        if (!ObjectUtils.allIsNotNull(entity.getOrderItemId(), entity.getRemark())) {
            return ResultForm.createErrorResultForm(null, "关联工单项和记录内容不能为空！");
        }
        OrderItem orderItem = orderItemHandler.selectByPrimaryKey(entity.getOrderItemId());
        if (!workOrderHandler.matchProcess(orderItem.getWorkOrderId(), WorkOrder.ORDER_PROCESS_ASSIGN)) {
            return ResultForm.createErrorResultForm(null, "只能对实施中的工单进行工作记录！");
        }
        WorkOrder workOrder = workOrderHandler.selectByPrimaryKey(orderItem.getWorkOrderId());
        if (!workGroupHandler.containMembers(workOrder.getWorkGroupId(), currentSysUserId())) {
            return ResultForm.createErrorResultForm(null, "无权进行工作记录（班组成员）！");
        }
        Map<String, Object> retMap;
        try {
            DocInfo docInfo = documentService.uploadFiles(multipartRequest, "file", entity.getDocId(), delIds);
            orderItem.setAction(true);
            entity.setDocId(docInfo.getId());
            entity.setRecordUserId(currentSysUserId());
            entity.setRecordUser(currentSysUserName());
            entity.setRecordDate(TimeUtil.now());
            entity.setRelatedDevice(!ArrayUtils.isEmpty(deviceList));

            entity = orderItemHandler.record(orderItem, entity, ListUtils.newArrayList(deviceList));
            if (ObjectUtils.isNull(workOrder.getRealStartDate())) {
                workOrder.setRealStartDate(TimeUtil.now());
                workOrderHandler.save(workOrder);
            }
            retMap = MapUtils.toMapIgnoreNull(entity);
            if (entity.getRelatedDevice()) {
                retMap.put("deviceList", deviceHandler.selectBatchByIds(ListUtils.newArrayList(deviceList)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(retMap, "记录成功！");
    }

    @ApiOperation("提交工单使用物资")
    @RequestMapping("/useMaterials")
    public ResultForm useMaterials(@ApiParam(value = "{orderId,materials:[OrderMaterial Objects]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("提交工单使用物资", jsonRequestBody);
        Long orderId = jsonRequestBody.getLong("orderId");
        if (ObjectUtils.isNull(orderId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单ID不能为空！");
        }
        if (!workOrderHandler.matchProcess(orderId, WorkOrder.ORDER_PROCESS_ASSIGN)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "实施中的工单才能提交物资！");
        }
        List<OrderMaterial> materials = jsonRequestBody.getList("materials", OrderMaterial.class);
        materials = workOrderHandler.addMaterials(orderId, materials);
        return ResultForm.createSuccessResultForm(materials, "提交物资材料成功！");
    }

    @ApiOperation("完成工单，工单实施完成提交")
    @RequestMapping("/complete")
    public ResultForm complete(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("完成工单，工单实施完成提交", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单ID不能为空！");
        }
        if (!workOrderHandler.matchProcess(id, WorkOrder.ORDER_PROCESS_ASSIGN)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能对实施中的工单进行该操作！");
        }
        List<OrderItem> itemList = orderItemHandler.selectEntities(MapUtils.newHashMap("workOrderId", id));
        for (OrderItem orderItem : itemList) {
            if (!orderItem.getAction()) {
                return ResultForm.createErrorResultForm(jsonRequestBody, "尚有未完成的工作项！");
            }
        }
        WorkOrder entity = workOrderHandler.selectByPrimaryKey(id);
        entity.setProcess(WorkOrder.ORDER_PROCESS_COMPLETE);
        entity.setRealEndDate(TimeUtil.now());
        workOrderHandler.save(entity);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "操作成功！");
    }

    @ApiOperation("工单审核")
    @RequestMapping("/confirm")
    public ResultForm confirm(@ApiParam(value = "{id, confirm:[true/false], confirmMsg}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        String confirm = jsonRequestBody.getString("confirm");
        String confirmMsg = jsonRequestBody.getString("confirmMsg");
        if (!ObjectUtils.allIsNotNull(id, confirm, confirmMsg)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单ID号、审核状态、审核信息均不能为空！");
        }
        if (!workOrderHandler.matchProcess(id, WorkOrder.ORDER_PROCESS_COMPLETE)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能对待审核的工单进行审核操作！");
        }
        WorkOrder entity = workOrderHandler.selectByPrimaryKey(id);
        entity.setConfirmUserId(currentSysUserId());
        entity.setConfirmDate(TimeUtil.now());
        entity.setConfirmMsg(confirmMsg);
        if (StringUtils.equals(Boolean.TRUE.toString(), confirm)) {
            entity.setProcess(WorkOrder.ORDER_PROCESS_CONFIRM);
        } else {
            entity.setProcess(WorkOrder.ORDER_PROCESS_ASSIGN);
        }
        workOrderHandler.save(entity);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "操作成功！");
    }

    @ApiOperation("工单结算")
    @RequestMapping("/amount")
    public ResultForm amount(@ApiParam(value = "{id, amount[double]}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        Double amount = jsonRequestBody.getDouble("amount");
        if (!ObjectUtils.allIsNotNull(id, amount)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单ID号、结算金额不能为空！");
        }
        if (!workOrderHandler.matchProcess(id, WorkOrder.ORDER_PROCESS_CONFIRM)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能对审核通过的工单进行结算！");
        }
        WorkOrder entity = workOrderHandler.selectByPrimaryKey(id);
        entity.setAssignUserId(currentSysUserId());
        entity.setAmount(amount);
        entity.setAmountDate(TimeUtil.now());
        entity.setProcess(WorkOrder.ORDER_PROCESS_AMOUNT);
        workOrderHandler.save(entity);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "操作成功！");
    }

    @ApiOperation("工单统计--根据工单来源")
    @RequestMapping("/countBySource")
    public ResultForm countBySource(@ApiParam(value = "{date:2018/2018-05/2018-08-12, dateType:year/month/day}") @RequestBody JsonRequestBody jsonRequestBody) throws ParseException {
        String dateStr = jsonRequestBody.getString("date");
        String dateType = jsonRequestBody.getString("dateType");
        if (!ObjectUtils.allIsNotNull(dateStr, dateType)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "日期、日期类型不能为空！");
        }
        Date start, end;
        String datePatter = "";
        String period = "";
        String year = "year";
        String month = "month";
        String day = "day";
        if (StringUtils.equals(year, dateType)) {
            datePatter = "yyyy";
            period = DataTimeUtils.YEAR;
        } else if (StringUtils.equals(month, dateType)) {
            datePatter = "yyyy-MM";
            period = DataTimeUtils.MONTH;
        } else if (StringUtils.equals(day, dateType)) {
            datePatter = "yyyy-MM-dd";
            period = DataTimeUtils.DAY;
        }
        start = TimeUtil.parseDate(dateStr, datePatter);
        end = DataTimeUtils.next(start, period);
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        return ResultForm.createSuccessResultForm(workOrderHandler.countBySource(map), "操作成功！");
    }

    @ApiOperation("工单统计--根据工单流程状态")
    @RequestMapping("/countByProcess")
    public ResultForm countByProcess(@RequestBody JsonRequestBody jsonRequestBody) throws ParseException {
        Long id = sessionUtils.getCurrentUser().getId();
        Map<String, Object> map = new HashMap<>();
        Date end = getNextMonthDate();
        map.put("end", end);
        map.put("id", id);
        return ResultForm.createSuccessResultForm(workOrderHandler.countByProcess(map), "操作成功！");
    }

    public Date getNextMonthDate() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar canlendar = Calendar.getInstance();
        canlendar.add(Calendar.DATE, 1);
/*        Date startTime=canlendar.getTime();
        String startStr=sf.format(startTime).substring(0,10);
        Date start = TimeUtil.parseDate(startStr, "yyyy-MM-dd");*/
        canlendar.add(Calendar.DATE, 30);
        Date endTime = canlendar.getTime();
        String endStr = sf.format(endTime).substring(0, 10);
        Date end = TimeUtil.parseDate(endStr, "yyyy-MM-dd");
        return end;
    }


    @ApiOperation("首页工单，本周完成率")
    @GetMapping("/weekOrderComplete")
    public ResultForm weekOrderComplete() {
        Map<String,Object> map = new HashMap<>();
        //计划工单总数
        map.put("planNum","17/20");
        //计划工单已完成数量
        map.put("planCompleteNum",1);
        //缺陷工单数量
        map.put("defectNum","1/1");
        //计划工单完成率
        map.put("planRate",0.85);
        map.put("defectRate",1);
        map.put("defectCompleteNum",1);
        //临时工单
        map.put("temCompleteNum",1);
        map.put("temNum","6/6");
        map.put("temRate",1);
//        return ResultForm.createSuccessResultForm(workOrderHandler.weekOrderComplete(), "操作成功！");
        return ResultForm.createSuccessResultForm(map, "操作成功！");
    }
}
