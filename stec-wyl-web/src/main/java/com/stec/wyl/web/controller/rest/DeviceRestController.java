package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.project.Maintenance;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.entity.protocol.DeviceType;
import com.stec.masterdata.entity.protocol.Manufacturer;
import com.stec.masterdata.entity.protocol.Product;
import com.stec.masterdata.handler.project.MaintenanceHandler;
import com.stec.masterdata.handler.project.ChannelHandler;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.protocol.DeviceTypeHandler;
import com.stec.masterdata.handler.protocol.ManufacturerHandler;
import com.stec.masterdata.handler.protocol.ProductHandler;
import com.stec.utils.MapUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.project.ProjectDiyHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.SequenceUtils;
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

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/31 0031
 * Time: 9:11
 */
@Api(value = "设备管理controller", tags = {"设备管理"})
@RestController
@RequestMapping(value = "/rest/device", method = RequestMethod.POST)
public class DeviceRestController extends BaseController {

    @Reference
    private DeviceHandler deviceHandler;

    @Reference
    private ProjectDiyHandler projectDiyHandler;

    @Reference
    private DeviceTypeHandler deviceTypeHandler;

    @Reference
    private ManufacturerHandler manufacturerHandler;

    @Reference
    private StructureHandler structureHandler;

    @Reference
    private ProductHandler productHandler;

    @Reference
    private MaintenanceHandler maintenanceHandler;

    @Reference
    private ChannelHandler channelHandler;


    @Autowired
    private DocumentService documentService;

    @ApiOperation("设备列表查询，分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(value = "{projectId:projectId*;deviceTypeId:deviceTypeId;\nmanufacturerId:manufacturerId;productId:productId;\nname:name;code:code,pageForm}")JsonRequestBody jsonRequestBody) {
        Device entity = jsonRequestBody.tryGet(Device.class);
        return ResultForm.createSuccessResultForm(projectDiyHandler.deviceList(entity, jsonRequestBody.getPageForm()), "查询设备分页信息成功！");
    }

    @ApiOperation("添加/保存设备信息")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody @ApiParam(value = "projectId:projectId*;structureId:structureId;deviceTypeId*:deviceTypeId;\nmanufacturerId*:manufacturerId;productId*:productId;\nname*:name;code:code;longitude,latitude") JsonRequestBody jsonRequestBody) {
        logger("添加/保存设备信息", jsonRequestBody);
        Device entity = jsonRequestBody.tryGet(Device.class);
//        if(!ObjectUtils.allIsNotNull(entity.getDeviceTypeId(), entity.getProjectId(), entity.getManufacturerId(), entity.getProductId(),entity.getName())){
        if(!ObjectUtils.allIsNotNull(entity.getDeviceTypeId(), entity.getProjectId(), entity.getName())){
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备所属项目、设备类别、厂商、型号、名称不能为空！");
        }
        if(ObjectUtils.isNull(entity.getId())) {
            entity.setCode(SequenceUtils.getSecondUID("ST"));
            entity.setPsn(SequenceUtils.getMillisUID());
        }
        return ResultForm.createSuccessResultForm(deviceHandler.save(entity), "设备保存成功！");
    }

    @ApiOperation("删除设备")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam("{id*:id}") JsonRequestBody jsonRequestBody) {
        logger("删除设备", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备ID不能为空！");
        }
        if(deviceHandler.deleteByPrimaryKey(id)) {
            return ResultForm.createSuccessResultForm(jsonRequestBody, "设备删除成功！");
        }
        return ResultForm.createErrorResultForm(jsonRequestBody, "设备删除成功！");
    }

    @ApiOperation("获取设备列表")
    @RequestMapping("/getDevices")
    public ResultForm getDevices(@RequestBody @ApiParam("{projectId:projectId 项目id,\nor structureId:structureId 空间位置id,\nname:name 设备名称,\ndeviceTypeId:deviceTypeId 小类id,\ndeviceParentTypeId:deviceParentTypeId 大类id,}") JsonRequestBody jsonRequestBody) {
        Long projectId = jsonRequestBody.getLong("projectId");
        Long structureId = jsonRequestBody.getLong("structureId");
        String name=jsonRequestBody.getString("name");
        Long deviceTypeId=jsonRequestBody.getLong("deviceTypeId");
        Long deviceParentTypeId=jsonRequestBody.getLong("deviceParentTypeId");
        List<Device> devices;
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        if (ObjectUtils.isNotNull(projectId)) {
            wrapper.eq("project_id", projectId);
            wrapper.isNull("structure_id");
        } else if (ObjectUtils.isNotNull(structureId)) {
            wrapper.eq("structure_id",structureId);
        } else {
            return ResultForm.createErrorResultForm(jsonRequestBody, "项目id或者空间位置id不能为空！");
        }
        if(StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        if(ObjectUtils.isNotNull(deviceTypeId)){
            wrapper.eq("device_type_id",deviceTypeId);
        }
        if(ObjectUtils.isNotNull(deviceParentTypeId)){
            wrapper.eq("device_parent_type_id",deviceParentTypeId);
        }
        devices = deviceHandler.selectEntities(wrapper);
        return ResultForm.createSuccessResultForm(devices);
    }

    @ApiOperation("设备文档新增/修改")
    @RequestMapping("/saveDevicesDoc")
    public ResultForm saveDevicesDoc(MultipartRequest multipartRequest, Long[] delIds, Long docId,Long deviceId) {
        logger("设备文档新增/修改", JSONObject.toJSONString("docId:"+docId+" "+"deviceId:"+deviceId));
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", docId, delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        Device device=new Device();
        device.setId(deviceId);
        device.setDocId(docInfo.getId());
        device = deviceHandler.save(device);
        return ResultForm.createSuccessResultForm(device, "文档保存成功！");
    }

    @ApiOperation("设备图片新增/修改")
    @RequestMapping("/saveDevicesImageDoc")
    public ResultForm saveDevicesImageDoc(MultipartRequest multipartRequest, Long[] delIds, Long imageDocId,Long deviceId) {
        logger("设备图片新增/修改", JSONObject.toJSONString("imageDocId:"+imageDocId+" "+"deviceId:"+deviceId));
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", imageDocId, delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        Device device=new Device();
        device.setId(deviceId);
        device.setImageDocId(docInfo.getId());
        device = deviceHandler.save(device);
        return ResultForm.createSuccessResultForm(device, "文档保存成功！");
    }

    @ApiOperation("获取设备信息")
    @RequestMapping("/getDevice")
    public ResultForm getDevice(@RequestBody @ApiParam(value = "{id:id*}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备ID不能为空！");
        }
        Device device=deviceHandler.selectByPrimaryKey(id);
        Map<String, Object> deviceMap;
        try {
            deviceMap = MapUtils.toMapIgnoreNull(device);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        Manufacturer manufacturer=manufacturerHandler.selectByPrimaryKey(device.getManufacturerId());
        DeviceType deviceType =deviceTypeHandler.selectByPrimaryKey(device.getDeviceTypeId());
        Structure structure =structureHandler.selectByPrimaryKey(device.getStructureId());
        Product product=productHandler.selectByPrimaryKey(device.getProductId());
        Maintenance maintenance = maintenanceHandler.selectByPrimaryKey(device.getMaintenanceId());
        if(ObjectUtils.isNotNull(manufacturer)){
            deviceMap.put("manufacturerName",manufacturer.getName());
        }else{
            deviceMap.put("manufacturerName","");
        }
        if(ObjectUtils.isNotNull(deviceType)){
            deviceMap.put("deviceTypeName",deviceType.getName());
        }else{
            deviceMap.put("deviceTypeName","");
        }
        if(ObjectUtils.isNotNull(structure)){
            deviceMap.put("structureName",structure.getName());
        }else{
            deviceMap.put("structureName","");
        }
        if(ObjectUtils.isNotNull(product)){
            deviceMap.put("productName",product.getName());
        }else{
            deviceMap.put("productName","");
        }
        if(ObjectUtils.isNotNull(maintenance)){
            deviceMap.put("maintenanceName",maintenance.getName());
        }else{
            deviceMap.put("maintenanceName","");
        }
        return ResultForm.createSuccessResultForm(deviceMap, "获取设备信息成功！");
    }

    @ApiOperation("根据设备类别获取设备列表，并根据里程号排序")
    @RequestMapping("/listWithDeviceTypeCode")
    public ResultForm listWithDeviceTypeCode(@ApiParam("{deviceTypeCode:*}") @RequestBody JsonRequestBody jsonRequestBody) {
        String deviceTypeCode = jsonRequestBody.getString("deviceTypeCode");
        if(StringUtils.isEmpty(deviceTypeCode)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备类别编码不能为空！");
        }
        DeviceType deviceType = deviceTypeHandler.selectEntity(MapUtils.newHashMap("code", deviceTypeCode));
        if(ObjectUtils.isNull(deviceType)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "所输入的设备类型编码找不到相应的设备类型！");
        }
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("device_type_id", deviceType.getId());
        wrapper.orderBy("mileage");
        return ResultForm.createSuccessResultForm(deviceHandler.selectEntities(wrapper), "查询成功！");
    }

    @ApiOperation("获取设备测点")
    @RequestMapping("/channels")
    public ResultForm channels(@ApiParam(value = "{deviceId:deviceId*}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long deviceId = jsonRequestBody.getLong("deviceId");
        if(ObjectUtils.isNull(deviceId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(channelHandler.selectEntities(MapUtils.newHashMap("deviceId", deviceId)), "查询成功！");
    }
}
