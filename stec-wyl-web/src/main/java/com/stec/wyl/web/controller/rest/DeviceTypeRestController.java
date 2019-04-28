package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.utils.MapUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.masterdata.entity.protocol.DeviceType;
import com.stec.masterdata.entity.protocol.MeasurePoint;
import com.stec.masterdata.handler.protocol.DeviceTypeHandler;
import com.stec.masterdata.handler.protocol.MeasurePointHandler;
import com.stec.masterdata.handler.protocol.ProtocolHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @author liweigao
 * Date: 2018-07-24
 * Time: 8:45
 */
@Api(value = "设备类型controller", tags = "设备类型管理")
@RestController
@RequestMapping(value = "/rest/deviceType", method = RequestMethod.POST)
public class DeviceTypeRestController extends BaseController {
    @Reference
    private DeviceTypeHandler deviceTypeHandler;
    @Reference
    private MeasurePointHandler measurePointHandler;
    @Reference
    private ProtocolHandler protocolHandler;

    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "设备类型分页列表查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        DeviceType deviceType = jsonRequestBody.tryGet(DeviceType.class);
        EntityWrapper<DeviceType> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(deviceType.getCode())) {
            wrapper.like("code", deviceType.getCode());
        }
        if (StringUtils.isNotEmpty(deviceType.getName())) {
            wrapper.like("name", deviceType.getName());
        }
        return ResultForm.createSuccessResultForm(deviceTypeHandler.selectEntities(wrapper, pageForm), "查询成功！");
    }

    @ApiOperation(value = "设备类型的保存和修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] delIds, DeviceType entity) {
        logger("保存或修改设备类型", JSONObject.toJSONString(entity));
        if (!StringUtils.isAllNotBlank(entity.getName(), entity.getCode())) {
            ResultForm.createErrorResultForm(entity, "设备名称，设备编码不能为空！");
        }
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", entity.getDocId(), delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        entity.setDocId(docInfo.getId());
        return ResultForm.createSuccessResultForm(deviceTypeHandler.save(entity), "保存成功！");
    }

    @ApiOperation("根据ID或CODE获取设备类型信息+")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "id/code") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        String code = jsonRequestBody.getString("code");
        DeviceType entity = null;
        if(ObjectUtils.isNotNull(id)){
            entity = deviceTypeHandler.selectByPrimaryKey(id);
        }
        else if(StringUtils.isNotEmpty(code)){
            entity = deviceTypeHandler.selectEntity(MapUtils.newHashMap("code", code));
        }
        return ResultForm.createSuccessResultForm(entity, "设备类型查询成功！");
    }

    @ApiOperation(value = "设备类型的删除")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody) {
        logger("设备类型的删除", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备类型ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(deviceTypeHandler.deleteByPrimaryKey(id), "删除成功！");
    }

    @ApiOperation("测点列表保存和修改")
    @RequestMapping("/saveMeasure")
    public ResultForm saveMeasure(@RequestBody @ApiParam(value = "{code:code 编码，name:name 测点名称,\ndeviceTypeId:deviceTypeId 设备类型id,state:state 状态,\nmaxLimit:maxLimit 最大限定值," +
            "minLimit:minLimit 最小限定值,\nmaxValue:maxValue 最大值，minValue:minValue 最小值,\nperiod 周期，standard 标准,type 类型}") JsonRequestBody jsonRequestBody) {
        logger("保存或修改测点", jsonRequestBody);
        MeasurePoint measurePoint = jsonRequestBody.tryGet(MeasurePoint.class);
        if (!StringUtils.isAllNotBlank(measurePoint.getName(), measurePoint.getCode(), measurePoint.getType())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "测点名称，测点编码，测点类型不能为空！");
        }
        return ResultForm.createSuccessResultForm(measurePointHandler.save(measurePoint));
    }

    @ApiOperation(value = "测点的删除")
    @RequestMapping("/deleteMeasure")
    public ResultForm deleteMeasure(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody) {
        logger("测点删除", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (id == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "测点ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(measurePointHandler.deleteByPrimaryKey(id), "删除成功！");
    }

    @ApiOperation(value = "查询设备的测点列表")
    @RequestMapping("/measureList")
    public ResultForm measureList(@RequestBody @ApiParam(value = "{deviceTypeCode:deviceTypeCode 设备编码 或者,\nid:id 设备类型id,\name:name 测点名称,\ntype:type 测点类型}") JsonRequestBody jsonRequestBody) {
        String deviceTypeCode = jsonRequestBody.getString("deviceTypeCode");
        Long id = jsonRequestBody.getLong("id");
        String name =jsonRequestBody.getString("name");
        String type =jsonRequestBody.getString("type");
        List<MeasurePoint> measurePoints;
        if (!StringUtils.isEmpty(deviceTypeCode)) {
            measurePoints = protocolHandler.selectMeasurePoints(deviceTypeCode);
        }else if(id != null){
            Map<String,Object> map = new HashMap<>();
            map.put("deviceTypeId",id);
            if(!StringUtils.isEmpty(name)){
                map.put("name",name);
            }
            if(!StringUtils.isEmpty(type)){
                map.put("type",type);
            }
            measurePoints=measurePointHandler.selectEntities(map);
        }else{
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备编码deviceTypeCode或者设备类型id不能为空！");
        }

        return ResultForm.createSuccessResultForm(measurePoints, "查询成功！");
    }

    @ApiOperation(value = "用父类型ID查子类型列表")
    @RequestMapping("/getSonListByParentId")
    public ResultForm getSonListByParentId(@RequestBody @ApiParam(value = "{id:id  大类id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (id == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "大类ID不能为空！");
        }
        DeviceType deviceType=new DeviceType();
        deviceType.setParentId(id);
        List<DeviceType> deviceTypeList=deviceTypeHandler.selectEntities(deviceType);
        return ResultForm.createSuccessResultForm(deviceTypeList, "查询成功！");
    }

    @ApiOperation(value = "大类小类列表查询")
    @RequestMapping("/getParentOrSonList")
    public ResultForm getParentOrSonList(@RequestBody @ApiParam(value = "{listType:son/parent}") JsonRequestBody jsonRequestBody) {
        String listType = jsonRequestBody.getString("listType");
        if(StringUtils.isEmpty(listType)){
            return ResultForm.createErrorResultForm(jsonRequestBody, "大类小类的类型不能为空！");
        }
        String son="son";
        String parent="parent";
        EntityWrapper<DeviceType> wrapper=new EntityWrapper<>();
        if(listType.equals(son)){
            wrapper.isNotNull("parent_id");
        }else if(listType.equals(parent)){
            wrapper.isNull("parent_id");
        }else{
            return ResultForm.createErrorResultForm(jsonRequestBody, "大类小类的类型只能是son或者parent！");
        }
        List<DeviceType> deviceTypeList=deviceTypeHandler.selectEntities(wrapper);
        return ResultForm.createSuccessResultForm(deviceTypeList, "查询成功！");
    }
}
