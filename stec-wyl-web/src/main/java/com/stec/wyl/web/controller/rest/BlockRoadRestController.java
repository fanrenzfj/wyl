package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
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
import com.stec.masterdata.handler.wyl.*;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *描述：封道管理
 * @author Li.peng
 *@create 2018-08-23 16:06
 */
@Api(value = "封道管理controller",tags = {"封道管理"})
@RestController
@RequestMapping(value = "/rest/blockRoad",method = RequestMethod.POST)
public class BlockRoadRestController extends BaseController {
    @Reference
    private BlockRoadHandler blockRoadHandler;
    @Reference
    private SysUserHandler sysUserHandler;
    @Reference
    private BlockRoadCarHandler blockRoadCarHandler;
    @Reference
    private CarHandler carHandler;
    @Reference
    private BlockRoadOrderHandler blockRoadOrderHandler;
    @Reference
    private WorkOrderHandler workOrderHandler;
    @Reference
    private WorkGroupHandler workGroupHandler;
    @Autowired
    private DocumentService documentService;
    @ApiOperation("封道管理新增/编辑")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{planStartDate:planStartDate,planEndDate:planEndDate,type:type,position;position,roadNo;roadNo,roadStart;roadStart,roadEnd:roadEnd,leaderId;leaderId,mobile:mobile,blockRoadCar:[],workOrder:[]}") @RequestBody JsonRequestBody jsonRequestBody){
        logger("封道管理新增/编辑",jsonRequestBody);
        BlockRoad entity=jsonRequestBody.tryGet(BlockRoad.class);
        String blockRoadCar="blockRoadCar";
        List<Long> blockRoadCarIds=null;
        if(jsonRequestBody.containsKey(blockRoadCar)){
            blockRoadCarIds=jsonRequestBody.getList("blockRoadCar",Long.class);
        }
        String workOrder="workOrder";
        List<Long> workOrderIds=null;
        if(jsonRequestBody.containsKey(workOrder)){
            workOrderIds=jsonRequestBody.getList("workOrder",Long.class);
        }
        if(!ObjectUtils.allIsNotNull(entity.getPlanStartDate(),entity.getPlanEndDate(),entity.getType(),entity.getPosition(),entity.getRoadNo(),entity.getLeaderId())){
            return ResultForm.createErrorResultForm(jsonRequestBody,"计划开始时间、计划结束时间、封道类型、施工方位、封闭车道号、现场负责人不能为空");
        }
        return ResultForm.createSuccessResultForm(blockRoadHandler.save(entity,blockRoadCarIds,workOrderIds),"新增/编辑封道成功");
    }

    @ApiOperation("封道管理处理")
    @RequestMapping("/handle")
    public ResultForm handle(MultipartRequest multipartRequest, Long[] delIds,Long docId, Long id,String remark){
        logger("封道管理处理", JSONObject.toJSONString("id:"+id+","+"remark:"+remark));
        if(ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm("id:"+id+","+"remark:"+remark,"封道id不能为空");
        }
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", docId, delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        BlockRoad blockRoad=new BlockRoad();
        blockRoad.setId(id);
        blockRoad.setDocId(docInfo.getId());
        blockRoad.setRemark(remark);
        blockRoad.setHandleStatus(true);
        blockRoad = blockRoadHandler.save(blockRoad);
        return ResultForm.createSuccessResultForm(blockRoad, "保存成功！");
    }

    @ApiOperation("删除封道")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 封道id}") JsonRequestBody jsonRequestBody){
        logger("删除封道",jsonRequestBody);
        Long id=jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"封道id不能为空");
        }
        return ResultForm.createSuccessResultForm(blockRoadHandler.deleteEntity(id));
    }

    @ApiOperation(value = "封道列表,分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(value = "{planStartDate:planStartDate,planEndDate:planEndDate,type:type[pageForm properties]}") JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        BlockRoad entity = jsonRequestBody.tryGet(BlockRoad.class);
        EntityWrapper<BlockRoad> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getType())) {
            wrapper.eq("type", entity.getType());
        }
        if (ObjectUtils.allIsNotNull(entity.getPlanStartDate(),entity.getPlanEndDate())) {
//            wrapper.le("plan_start_date",entity.getPlanEndDate()).and().ge("plan_end_date",entity.getPlanStartDate());
            wrapper.le("plan_end_date",entity.getPlanEndDate()).and().ge("plan_start_date",entity.getPlanStartDate());
        }
        DataPaging<BlockRoad> dataPaging = blockRoadHandler.selectEntities(wrapper, pageForm);
        List<BlockRoad> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (BlockRoad blockRoad : list) {
            SysUser sysUser=sysUserHandler.selectByPrimaryKey(blockRoad.getLeaderId());
            blockRoad.setLeaderName(sysUser.getName());
            Map<String, Object> road;
            try {
                road = MapUtils.toMapIgnoreNull(blockRoad);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(road);
            List<BlockRoadCar> blockRoadCarList = blockRoadCarHandler.selectEntities(MapUtils.newHashMap("blockRoadId", blockRoad.getId()));
            List<Long> carIds = new ArrayList<>(blockRoadCarList.size());
            for (BlockRoadCar blockRoadCar : blockRoadCarList) {
                carIds.add(blockRoadCar.getCarId());
            }
            List<Car> cars = carHandler.selectBatchByIds(carIds);
            road.put("blockRoadCar", cars);
        }
        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "班组列表查询成功！");
    }

    @ApiOperation(value = "获取封道")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(value = "{id:id  封道id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"封道id不能为空！");
        }
        BlockRoad blockRoad =blockRoadHandler.selectByPrimaryKey(id);
        SysUser sysUser=sysUserHandler.selectByPrimaryKey(blockRoad.getLeaderId());
        blockRoad.setLeaderName(sysUser.getName());
        blockRoad.setMobile(sysUser.getMobile());
        Map<String, Object> road;
        try {
            road = MapUtils.toMapIgnoreNull(blockRoad);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        if(ObjectUtils.isNotNull(blockRoad)){
            List<BlockRoadCar> blockRoadCarList = blockRoadCarHandler.selectEntities(MapUtils.newHashMap("blockRoadId", blockRoad.getId()));
            List<Long> carIds = new ArrayList<>(blockRoadCarList.size());
            for (BlockRoadCar blockRoadCar : blockRoadCarList) {
                carIds.add(blockRoadCar.getCarId());
            }
            List<Car> cars = carHandler.selectBatchByIds(carIds);
            road.put("blockRoadCar", cars);

            List<BlockRoadOrder> blockRoadOrderList = blockRoadOrderHandler.selectEntities(MapUtils.newHashMap("blockRoadId", blockRoad.getId()));
            if (ObjectUtils.isNotNull(blockRoadOrderList)  &&  blockRoadOrderList.size()>0){
                List<Long> orderIds = new ArrayList<>(blockRoadOrderList.size());
                for (BlockRoadOrder blockRoadOrder : blockRoadOrderList) {
                    orderIds.add(blockRoadOrder.getWorkOrderId());
                }
                List<WorkOrder> orders = workOrderHandler.selectBatchByIds(orderIds);
                List<Map<String, Object>> orderList = new ArrayList<>(orders.size());
                for (WorkOrder order : orders) {
                    Map<String, Object> orderMap;
                    try {
                        orderMap = MapUtils.toMapIgnoreNull(order);
                    } catch (Exception e) {
                        return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
                    }
                    WorkGroup workGroup = workGroupHandler.selectByPrimaryKey(order.getWorkGroupId());
                    SysUser user=sysUserHandler.selectByPrimaryKey(workGroup.getLeaderId());
                    orderMap.put("leaderName",user.getName());
                    orderMap.put("leaderMobile",user.getMobile());
                    orderList.add(orderMap);
                }
                road.put("blockRoadOrder", orderList);
            }
            return ResultForm.createSuccessResultForm(road,"获取封道成功！");
        }else{
            return ResultForm.createErrorResultForm(jsonRequestBody,"没有封道！");
        }
    }
}
