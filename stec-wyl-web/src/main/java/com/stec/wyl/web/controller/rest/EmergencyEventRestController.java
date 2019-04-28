package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.basic.DocAttach;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.entity.wyl.*;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.basic.DocAttachHandler;
import com.stec.masterdata.handler.basic.SysDicHandler;
import com.stec.masterdata.handler.wyl.*;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import com.stec.wyl.web.utils.ExcelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;
import stec.framework.excel.ExcelTemplate;
import stec.framework.excel.handler.ExcelHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：应急事件
 *
 * @author Li.peng
 * @create 2018-08-28 15:27
 */
@Api(value = "应急事件controller", tags = {"应急事件"})
@RestController
@RequestMapping(value = "/rest/emergencyEvent", method = RequestMethod.POST)
public class EmergencyEventRestController extends BaseController {
    @Value("${spring.pic.url}")
    private String PICURL;
    @Reference
    private EmergencyEventHandler emergencyEventHandler;
    @Autowired
    private DocumentService documentService;
    @Reference
    private EmergencyEventMaterialHandler emergencyEventMaterialHandler;
    @Reference
    private EmergencyEventWorkGroupHandler emergencyEventWorkGroupHandler;
    @Reference
    private EmergencyEventCarHandler emergencyEventCarHandler;
    @Reference
    private WorkGroupHandler workGroupHandler;
    @Reference
    private CarHandler carHandler;
    @Reference
    private SysUserHandler sysUserHandler;
    @Autowired
    private ExcelTemplate excelTemplate;
    @Reference
    private SysDicHandler sysDicHandler;
    @Reference
    private DocAttachHandler docAttachHandler;

    @ApiOperation("应急事件新增/修改")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{name:name,discoveryTime:discoveryTime,\neventType;eventType,level;level,\nposition:position,\ndetailPosition:detailPosition,\nsource;source,userId:userId,userName:userName,\nmobile;mobile,remark;remark}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("应急事件新增/修改", jsonRequestBody);

        EmergencyEvent emergencyEvent = jsonRequestBody.tryGet(EmergencyEvent.class);
        if (!ObjectUtils.allIsNotNull(emergencyEvent.getName(), emergencyEvent.getDiscoveryTime(), emergencyEvent.getEventType(), emergencyEvent.getLevel(), emergencyEvent.getPosition(), emergencyEvent.getDetailPosition())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, " 事件名称、发现时间、事件类型、级别、方位、详细部位不能为空！");
        }
        if (ObjectUtils.isNull(emergencyEvent.getId())) {
            emergencyEvent.setStatus(EmergencyEvent.EMERGENCY_EVENT_STATUS_CONFIRM);
        } else {
            if (!emergencyEventHandler.matchProcess(emergencyEvent.getId(), EmergencyEvent.EMERGENCY_EVENT_STATUS_CONFIRM)) {
                return ResultForm.createErrorResultForm(jsonRequestBody, "该应急事件不处于处确认状态，无法修改");
            }
        }
        return ResultForm.createSuccessResultForm(emergencyEventHandler.save(emergencyEvent), "新增/修改应急事件成功");
    }

    @ApiOperation(value = "新增应急事件 优化流程，增加现场图片")
    @RequestMapping("/saveBetter")
    public ResultForm saveBetter(@ApiParam(value = "{name:name,discoveryTime:discoveryTime,\neventType;eventType,level;level,\nposition:position,\ndetailPosition:detailPosition,\nsource;source,userId:userId,userName:userName,\nmobile;mobile,remark;remark}") MultipartRequest multipartRequest, EmergencyEvent emergencyEvent, Long[] delIds) {
        if (!ObjectUtils.allIsNotNull(emergencyEvent.getName(), emergencyEvent.getDiscoveryTime(), emergencyEvent.getEventType(), emergencyEvent.getLevel(), emergencyEvent.getPosition(), emergencyEvent.getDetailPosition())) {
            return ResultForm.createErrorResultForm(null, " 事件名称、发现时间、事件类型、级别、方位、详细部位不能为空！");
        }
        //上传现场图片
        DocInfo docInfo;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", emergencyEvent.getDocId(), delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        return ResultForm.createSuccessResultForm(emergencyEventHandler.save(emergencyEvent), "新增/修改应急事件成功");
    }

    @ApiOperation(value = "应急事件导出Excel")
    @RequestMapping("/downLoad")
    public ResultForm downLoad(@ApiParam(value = "{name:name}") @RequestBody JsonRequestBody jsonRequestBody, HttpServletResponse response) {
        //response响应
        try {
            ExcelResponse.responseHeader(response, "应急事件");
        } catch (UnsupportedEncodingException e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        //获取应急事件 参数
        String name = jsonRequestBody.getString("name");
        EntityWrapper<EmergencyEvent> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderBy("discovery_time", false);
        List<EmergencyEvent> events = emergencyEventHandler.selectEntities(wrapper);
        //循环events
        for (EmergencyEvent event : events) {
            packagingEmergencyEvent(event);
        }
        //映射excel表 把字段与表字段映射
        List<ExcelHeader> headers = packagingExcel();
        try {
            excelTemplate.exportObjects2Excel(events, headers, "应急事件", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }

    @ApiOperation(value = "应急事件列表,分页查询")
    @RequestMapping("/list")
    public ResultForm list(@ApiParam(value = "{name:name,[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        EmergencyEvent entity = jsonRequestBody.tryGet(EmergencyEvent.class);
        EntityWrapper<EmergencyEvent> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        wrapper.orderBy("discovery_time", false);
        DataPaging<EmergencyEvent> dataPaging = emergencyEventHandler.selectEntities(wrapper, pageForm);
        return ResultForm.createSuccessResultForm(dataPaging, "应急事件列表查询成功！");
    }


    @ApiOperation(value = "应急事件排序")
    @GetMapping("/topList")
    public ResultForm list() {
        PageForm pageForm = new PageForm();
        pageForm.setCurrPage(1);
        pageForm.setPageSize(10);
        EntityWrapper<EmergencyEvent> wrapper = new EntityWrapper<>();
        wrapper.orderBy("discovery_time", false);
        DataPaging<EmergencyEvent> dataPaging = emergencyEventHandler.selectEntities(wrapper, pageForm);
        return ResultForm.createSuccessResultForm(dataPaging, "应急事件列表查询成功！");
    }

    @ApiOperation(value = "获取应急事件")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id  应急事件id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "应急事件id不能为空！");
        }
        EmergencyEvent emergencyEvent = emergencyEventHandler.selectByPrimaryKey(id);
        if (ObjectUtils.isNotNull(emergencyEvent.getUserId())) {
            SysUser sysUser = sysUserHandler.selectByPrimaryKey(emergencyEvent.getUserId());
            emergencyEvent.setUserName(sysUser.getName());
        }
        Map<String, Object> emergencyEventMap;
        try {
            emergencyEventMap = MapUtils.toMapIgnoreNull(emergencyEvent);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        if (ObjectUtils.isNotNull(emergencyEvent)) {
            List<EmergencyEventMaterial> emergencyEventMaterialList = emergencyEventMaterialHandler.selectEntities(MapUtils.newHashMap("emergencyEventId", emergencyEvent.getId()));
            emergencyEventMap.put("materials", emergencyEventMaterialList);
            List<EmergencyEventWorkGroup> emergencyEventWorkGroupList = emergencyEventWorkGroupHandler.selectEntities(MapUtils.newHashMap("emergencyEventId", emergencyEvent.getId()));
            if (emergencyEventWorkGroupList.size() > 0) {
                List<Long> workGroupIds = new ArrayList<>(emergencyEventWorkGroupList.size());
                for (EmergencyEventWorkGroup emergencyEventWorkGroup : emergencyEventWorkGroupList) {
                    workGroupIds.add(emergencyEventWorkGroup.getWorkGroupId());
                }
                List<WorkGroup> workGroups = workGroupHandler.selectBatchByIds(workGroupIds);
                emergencyEventMap.put("workGroups", workGroups);
            }
            List<EmergencyEventCar> emergencyEventCarList = emergencyEventCarHandler.selectEntities(MapUtils.newHashMap("emergencyEventId", emergencyEvent.getId()));
            if (emergencyEventCarList.size() > 0) {
                List<Long> carIds = new ArrayList<>(emergencyEventCarList.size());
                for (EmergencyEventCar emergencyEventCar : emergencyEventCarList) {
                    carIds.add(emergencyEventCar.getCarId());
                }
                List<Car> cars = carHandler.selectBatchByIds(carIds);
                emergencyEventMap.put("cars", cars);
            }
            return ResultForm.createSuccessResultForm(emergencyEventMap, "应急事件获取成功！");
        } else {
            return ResultForm.createErrorResultForm(jsonRequestBody, "没有应急事件！");
        }
    }

    @ApiOperation(value = "删除应急事件")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id 应急事件id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("删除应急事件", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "应急事件id不能为空！");
        } else {
            if (!emergencyEventHandler.matchProcess(id, EmergencyEvent.EMERGENCY_EVENT_STATUS_CONFIRM)) {
                return ResultForm.createErrorResultForm(jsonRequestBody, "该应急事件不处于处确认状态，无法删除");
            } else {
                return ResultForm.createSuccessResultForm(emergencyEventHandler.deleteByPrimaryKey(id), "应急事件删除成功！");
            }
        }
    }

    @ApiOperation("应急事件确认操作")
    @RequestMapping("/confirm")
    public ResultForm confirm(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("应急事件确认操作", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "应急事件id不能为空！");
        }
        if (!emergencyEventHandler.matchProcess(id, EmergencyEvent.EMERGENCY_EVENT_STATUS_CONFIRM)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "该应急事件不处于处确认状态，无法执行确认操作");
        }
        EmergencyEvent emergencyEvent = new EmergencyEvent();
        emergencyEvent.setId(id);
        emergencyEvent.setStatus(EmergencyEvent.EMERGENCY_EVENT_STATUS_HANDLE);
        return ResultForm.createSuccessResultForm(emergencyEventHandler.save(emergencyEvent), "确认应急事件成功");
    }

    @ApiOperation("应急事件处理操作")
    @RequestMapping("/handle")
    public ResultForm handle(@ApiParam(value = "{id:id,materialIds:[{materialId:materialId,amount:amount,unit:unit},{}],workGroupIds:[],carIds:[]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("应急事件处理操作", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "应急事件id不能为空！");
        }
        if (!emergencyEventHandler.matchProcess(id, EmergencyEvent.EMERGENCY_EVENT_STATUS_HANDLE)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "该应急事件不处于处理状态，无法执行处理操作");
        }
        List<EmergencyEventMaterial> materialIds = jsonRequestBody.getList("materialIds", EmergencyEventMaterial.class);
        List<Long> workGroupIds = jsonRequestBody.getList("workGroupIds", Long.class);
        List<Long> carIds = jsonRequestBody.getList("carIds", Long.class);
        return ResultForm.createSuccessResultForm(emergencyEventHandler.handle(id, materialIds, workGroupIds, carIds), "确认应急事件成功");
    }

    @ApiOperation("应急事件评估操作")
    @RequestMapping("/assessment")
    public ResultForm assessment(MultipartRequest multipartRequest, Long[] delIds, Long id, Long docId) {
        logger("应急事件评估操作", JSONObject.toJSONString(id));
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(id, "应急事件id不能为空！");
        }
        if (!emergencyEventHandler.matchProcess(id, EmergencyEvent.EMERGENCY_EVENT_STATUS_ASSESSMENT)) {
            return ResultForm.createErrorResultForm(id, "该应急事件不处于评估状态，无法执行评估操作");
        }
        DocInfo docInfo;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", docId, delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        EmergencyEvent emergencyEvent = new EmergencyEvent();
        emergencyEvent.setId(id);
        emergencyEvent.setStatus(EmergencyEvent.EMERGENCY_EVENT_STATUS_COMPLETE);
        emergencyEvent.setDocId(docInfo.getId());
        emergencyEvent = emergencyEventHandler.save(emergencyEvent);
        return ResultForm.createSuccessResultForm(emergencyEvent, "保存成功！");
    }



    //封装excel表映射字段
    private List<ExcelHeader> packagingExcel() {
        //创建excel Header对象
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("发现时间", 1, "discoveryTime", Date.class, "yyyy年MM月dd日 hh:mm:ss"));
        headers.add(new ExcelHeader("事件类型", "eventType", String.class));
        headers.add(new ExcelHeader("级别", "level", String.class));
        headers.add(new ExcelHeader("方位", "position", String.class));
        headers.add(new ExcelHeader("详细部位", "detailPosition", String.class));
        headers.add(new ExcelHeader("事件来源", "source", String.class));
        headers.add(new ExcelHeader("负责人姓名", "userName", String.class));
        headers.add(new ExcelHeader("手机号码", "mobile", String.class));
        headers.add(new ExcelHeader("事件描述", "remark", String.class));
        headers.add(new ExcelHeader("现场图片", "status", String.class));
        return headers;
    }

    //封装db查出来的EmergencyEvent对象
    private void packagingEmergencyEvent(EmergencyEvent event) {
        //向数据字典 转换事件类型
        event.setEventType(dBConvertSysDic(event.getEventType()));
        //向数据字典 转换级别
        event.setLevel(dBConvertSysDic(event.getLevel()));
        //向数据字典 转换方位
        event.setPosition(dBConvertSysDic(event.getPosition()));
        //向数据字典 转换详细部位
        event.setDetailPosition(dBConvertSysDic(event.getDetailPosition()));
        //获取负责人姓名
        String codeName = event.getUserName();
        if (!StringUtils.isNotEmpty(codeName)) {
            Long userId = event.getUserId();
            if (ObjectUtils.isNotNull(userId)) {
                SysUser sysUser = sysUserHandler.selectByPrimaryKey(userId);
                event.setUserName(sysUser.getName());
            }
        }
        //获取现场图片
        Long docId = event.getDocId();
        if (ObjectUtils.isNotNull(docId)) {
            DocAttach attach = new DocAttach();
            attach.setDocInfoId(docId);
            //封装图片 暂入status
            List<DocAttach> docAttaches = docAttachHandler.selectEntities(attach);
            String remark = "";
            for (DocAttach docAttach : docAttaches) {
                remark = PICURL + docAttach.getFileName() + "；" + remark;
            }
            event.setStatus(remark);
        }
    }
    //数据库code 转换成数据字典字段
    public String dBConvertSysDic(String dbCode) {
        if (StringUtils.isNotEmpty(dbCode)) {
            SysDic sysDic = new SysDic();
            sysDic.setCode(dbCode);
            if (ObjectUtils.isNotNull(sysDic)) {
                SysDic dbSysDic = sysDicHandler.selectEntity(sysDic);
                if (ObjectUtils.isNotNull(dbSysDic)) {
                    dbCode = dbSysDic.getName();
                }
            }
        }
        return dbCode;
    }

}
