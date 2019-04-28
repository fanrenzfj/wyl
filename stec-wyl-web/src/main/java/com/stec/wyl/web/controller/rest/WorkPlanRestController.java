package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.entity.wyl.PlanItemDevice;
import com.stec.masterdata.entity.wyl.WorkPlan;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.wyl.PlanItemDeviceHandler;
import com.stec.masterdata.handler.wyl.PlanItemHandler;
import com.stec.masterdata.handler.wyl.WorkPlanHandler;
import com.stec.utils.*;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:56
 */
@Api(tags = {"养护计划管理"})
@RestController
@RequestMapping(value = "/rest/workPlan", method = RequestMethod.POST)
public class WorkPlanRestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private WorkPlanHandler workPlanHandler;

    @Reference
    private PlanItemHandler planItemHandler;

    @Reference
    private PlanItemDeviceHandler planItemDeviceHandler;

    @Reference
    private DeviceHandler deviceHandler;

    @ApiOperation("养护计划列表，分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody) {
        WorkPlan entity = jsonRequestBody.tryGet(WorkPlan.class);
        PageForm pageForm = jsonRequestBody.getPageForm();
        EntityWrapper<WorkPlan> wrapper = new EntityWrapper<>();

        String year = jsonRequestBody.getString("year");
        if(StringUtils.isNotBlank(year)) {
            try {
                Date date = TimeUtil.parseDate(year + "-01-02");
                Date start = DataTimeUtils.floorDate(date, DataTimeUtils.YEAR);
                Date end = DataTimeUtils.ceilDate(date, DataTimeUtils.YEAR);
                wrapper.lt("start_date", end).and().gt("end_date", start);
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (StringUtils.isNotBlank(entity.getType())) {
            wrapper.eq("type", entity.getType());
        }
        if (StringUtils.isNotBlank(entity.getFrequency())) {
            wrapper.eq("frequency", entity.getFrequency());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        wrapper.orderBy("start_date", false);
        return ResultForm.createSuccessResultForm(workPlanHandler.selectEntities(wrapper, pageForm), "查询成功！");
    }


    @ApiOperation("保存养护计划，包含计划项")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{\"name\":\"计划1\",items:[{\"name\":\"任务1\",\"structureId\":\"1\",deviceList:[1,2,3]},\n{\"name\":\"任务2\",\"structureId\":\"1\",deviceList:[1,2,3]}]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("保存养护计划，包含计划项", jsonRequestBody);
        WorkPlan entity = jsonRequestBody.tryGet(WorkPlan.class);
        if (!ObjectUtils.allIsNotNull(entity.getName(), entity.getStartDate(), entity.getEndDate(), entity.getFrequency(), entity.getType())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "计划名称、时间、计划频次、计划类型均不能为空！");
        }
        List<PlanItem> items = jsonRequestBody.getList("items", PlanItem.class);

        if (CollectionUtils.isEmpty(items)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "计划项不能为空！");
        }
        //临时为了应付文一路现场录入计划时候  不选择设施设备   只输入名称
//        for (PlanItem item : items) {
//            if(ObjectUtils.isNull(item.getStructureId())) {
//                return ResultForm.createErrorResultForm(jsonRequestBody, "计划项必须关联设施结构！");
//            }
//        }
        if (ObjectUtils.isNull(entity.getId())) {
            entity.setCreateUserId(currentSysUserId());
            entity.setCreateDate(TimeUtil.now());
        } else {
            entity.setUpdateUserId(currentSysUserId());
            entity.setUpdateDate(TimeUtil.now());
        }
        List<List<Long>> deviceLists = new ArrayList<>();

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
            workPlanHandler.save(entity, items, deviceLists);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultForm.createErrorResultForm(jsonArray, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(jsonRequestBody, "保存成功！");
    }

    @ApiOperation("养护计划审核")
    @RequestMapping("/confirm")
    public ResultForm confirm(@ApiParam(value = "{id【计划id】, status【0/1】, confirm【审核内容】}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("养护计划审核", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        String status = jsonRequestBody.getString("status");
        if (!ObjectUtils.allIsNotNull(id, status)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID、审核状态不能为空！");
        }
        WorkPlan entity = workPlanHandler.selectByPrimaryKey(id);
        if (StringUtils.equals(WorkPlan.PLAN_STATUS_CONFIRM, entity.getStatus())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "该计划已经审核！");
        }
        entity.setStatus(status);
        entity.setConfirm(jsonRequestBody.getString("confirm"));
        entity.setConfirmUserId(currentSysUserId());
        entity.setConfirmDate(TimeUtil.now());
        workPlanHandler.save(entity);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "计划审核成功！");
    }

    @ApiOperation("获取养护计划详情")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        Map<String, Object> retMap;
        try {
            WorkPlan entity = workPlanHandler.selectByPrimaryKey(id);
            retMap = MapUtils.toMapIgnoreNull(entity);
            PlanItem param = new PlanItem();
            param.setWorkPlanId(entity.getId());
            List<PlanItem> items = planItemHandler.selectEntities(param);

            List<Map<String, Object>> itemMaps = new ArrayList<>(items.size());
            for (PlanItem item : items) {
                Map<String, Object> itemMap = MapUtils.toMapIgnoreNull(item);
                itemMaps.add(itemMap);
                if (item.getRelatedDevice()) {
                    PlanItemDevice p = new PlanItemDevice();
                    p.setPlanItemId(item.getId());
                    List<PlanItemDevice> list = planItemDeviceHandler.selectEntities(p);
                    List<Long> deviceList = new ArrayList<>(list.size());
                    for (PlanItemDevice planItemDevice : list) {
                        deviceList.add(planItemDevice.getDeviceId());
                    }
                    itemMap.put("deviceList", deviceHandler.selectBatchByIds(deviceList));
                }
            }
            retMap.put("items", itemMaps);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultForm.createErrorResultForm(jsonRequestBody, "获取失败！");
        }
        return ResultForm.createSuccessResultForm(retMap, "获取养护计划信息成功！");
    }

    @ApiOperation("养护计划删除，只能删除未审核的计划")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("养护计划删除，只能删除未审核的计划", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        WorkPlan entity = workPlanHandler.selectByPrimaryKey(id);
        if(StringUtils.equals(WorkPlan.PLAN_STATUS_CONFIRM, entity.getStatus())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "不能删除已审核的计划！");
        }
        workPlanHandler.deleteByPrimaryKey(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "删除成功！");
    }

}
