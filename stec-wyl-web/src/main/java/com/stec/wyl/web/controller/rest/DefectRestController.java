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
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.entity.wyl.Defect;
import com.stec.masterdata.entity.wyl.RoadEvaluateItem;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.basic.DocAttachHandler;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.wyl.DefectHandler;
import com.stec.masterdata.handler.wyl.WorkOrderHandler;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.SequenceUtils;
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

/**
 * 描述：缺陷管理
 *
 * @author Li.peng
 * @create 2018-08-28 14:13
 */
@Api(tags = {"缺陷管理"})
@RestController
@RequestMapping(value = "/rest/defect", method = RequestMethod.POST)
public class DefectRestController extends BaseController {

    @Value("${spring.pic.url}")
    private String PICURL;
    @Reference
    private DefectHandler defectHandler;

    @Reference
    private WorkOrderHandler workOrderHandler;

    @Reference
    private SysUserHandler sysUserHandler;

    @Reference
    private StructureHandler structureHandler;

    @Reference
    private DeviceHandler deviceHandler;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ExcelTemplate excelTemplate;

    @Reference
    private DocAttachHandler docAttachHandler;

    @ApiOperation("缺陷列表，分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody) {
        //数据库查询规则封装实体 EntityWrapper
        EntityWrapper<Defect> wrapper = new EntityWrapper<>();
        PageForm pageForm = jsonRequestBody.getPageForm();
        //从JsonRequestBody实例中 获取传入的参数
        String name = jsonRequestBody.getString("name");
        String status = jsonRequestBody.getString("status");
        //判断前台参数是否为空
        if (StringUtils.isNotEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("structure_name", name).or().like("device_name", name);
        }
        wrapper.orderBy("discovery_date", false);
        //向数据库进行分页查询 并进行封装成DataPaging对象
        DataPaging<Defect> dataPaging = defectHandler.selectEntities(wrapper, pageForm);
        //从DataPaging对象中 获取 Defect集合对象
        List<Defect> list = dataPaging.getList();
        for (Defect defect : list) {
            makeDefect(defect);
        }
        return ResultForm.createSuccessResultForm(dataPaging, "分页查询成功！");
    }


   /* @ApiOperation("导出所有缺陷包括图片url的excel文件")
    @GetMapping("/downloadDefectXls")
    public ResultForm downloadDefectXls(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Access-control-Expose-Headers", "attachment");
        response.setHeader("attachment", "download.xlsx");

        EntityWrapper<Defect> wrapper = new EntityWrapper<>();
        PageForm pageForm = new PageForm();
        pageForm.setCurrPage(1);
        pageForm.setPageSize(100);

        wrapper.orderBy("discovery_date", false);
        DataPaging<Defect> dataPaging = defectHandler.selectEntities(wrapper, pageForm);
        List<Defect> list = dataPaging.getList();
        for (Defect defect : list) {
            makeDefect(defect);
            DocAttach attach = new DocAttach();
            attach.setDocInfoId(defect.getDocId());
            List<DocAttach> docAttaches = docAttachHandler.selectEntities(attach);
            String remark ="";
            for (DocAttach docAttach : docAttaches) {
                remark = " http://183.129.193.154:6280/stec-platform-doc/img/"+remark + docAttach.getFileName()+"；";
            }
            defect.setRemark(remark);
        }

        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("结构", "name", String.class));
        headers.add(new ExcelHeader("发现时间", 1, "discoveryDate", Date.class, "yyyy年MM月dd日"));
        headers.add(new ExcelHeader("故障类型名称", "defectTypeName", String.class));
        headers.add(new ExcelHeader("设施名称", "structureName", String.class));
        headers.add(new ExcelHeader("设备名称", "deviceName", String.class));
        headers.add(new ExcelHeader("缺陷类型", "defectTypeName", String.class));
        headers.add(new ExcelHeader("发现人", "discoveryUserName", String.class));
        headers.add(new ExcelHeader("图片", "remark", String.class));

        try {
            excelTemplate.exportObjects2Excel(list, headers, "缺陷列表", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }*/

    @ApiOperation("导出所有缺陷包括图片url的excel文件")
    @RequestMapping("/downloadDefectXlsBetter")
    public ResultForm downloadDefectXlsBetter(@RequestBody JsonRequestBody jsonRequestBody, HttpServletResponse response)  {
        //设置响应头部信息 以及设置excel表名
        try {
            ExcelResponse.responseHeader(response, "文一路隧道运维缺陷");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //封装实体传参
        EntityWrapper<Defect> wrapper = new EntityWrapper<>();
        PageForm pageForm = new PageForm();
        pageForm.setCurrPage(1);
        pageForm.setPageSize(100);
        //获取前台参数
        String name = jsonRequestBody.getString("name");
        String status = jsonRequestBody.getString("status");
        //判断参数是否为空
        if (StringUtils.isNotEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("structure_name", name).or().like("device_name", name);
        }
        //根据日期排序
        wrapper.orderBy("discovery_date", false);
        //向数据库查询数据 封装集合
        DataPaging<Defect> dataPaging = defectHandler.selectEntities(wrapper, pageForm);
        List<Defect> list = dataPaging.getList();
        //获取封装图片路径的Defect
        for (Defect defect : list) {
            makeDefect(defect);
            DocAttach attach = new DocAttach();
            attach.setDocInfoId(defect.getDocId());
            List<DocAttach> docAttaches = docAttachHandler.selectEntities(attach);
            String remark = "";
            for (DocAttach docAttach : docAttaches) {
                remark = PICURL + docAttach.getFileName() + "；" + remark;
                /*remark = " http://183.129.193.154:6280/stec-platform-doc/img/" + docAttach.getFileName() +  "；"+ remark;*/
            }
            defect.setName(remark);
            //映射status
            String statusdb = defect.getStatus();
            if (statusdb != null) {
                if (statusdb.equals("20")) {
                    defect.setStatus("处理中");
                } else if (statusdb.equals("10")) {
                    defect.setStatus("待处理");
                } else if (statusdb.equals("0")) {
                    defect.setStatus("待确认");
                } else if (statusdb.equals("30")) {
                    defect.setStatus("已处理");
                }
            }
            //映射defectSource
            String dataSource = defect.getSource();
            if (dataSource != null) {
                if (dataSource.equals("plan_order")) {
                    defect.setSource("计划工单");
                } else if (dataSource.equals("temporary_order")) {
                    defect.setSource("临时工单");
                } else if (dataSource.equals("defect_order")) {
                    defect.setSource("缺陷工单");
                } else if (dataSource.equals("emergency_event")) {
                    defect.setSource("应急事件");
                } else if (dataSource.equals("monitor_center")) {
                    defect.setSource("监控中心");
                } else if (dataSource.equals("other")) {
                    defect.setSource("其他");
                }
            }
        }
        //映射excel表
        List<ExcelHeader> headers = getExcelHeder();
        try {
            excelTemplate.exportObjects2Excel(list, headers, "缺陷列表", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }

    @ApiOperation("缺陷新增/修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] delIds, Defect entity) {
        logger("缺陷新增/修改", JSONObject.toJSONString(entity));
        if (!ObjectUtils.allIsNotNull(entity.getDiscoveryDate(), entity.getStructureId())) {
            return ResultForm.createErrorResultForm(entity, "发现时间、设施结构、缺陷类型不能为空");
        }
        try {
            if (ObjectUtils.isNotNull(entity.getId())) {
                if (!defectHandler.matchStatus(entity.getId(), Defect.DEFECT_STATUS_CREATE)) {
                    return ResultForm.createErrorResultForm(entity, "只能对待确认的缺陷进行修改！");
                }
            }
            DocInfo docInfo = documentService.uploadFiles(multipartRequest, "file", entity.getDocId(), delIds);
            entity.setDocId(docInfo.getId());
            entity.setStatus(Defect.DEFECT_STATUS_CREATE);
            defectHandler.save(entity);
            if (ObjectUtils.isNull(entity.getDeviceId())) {
                defectHandler.removeDeviceId(entity.getId());
            }
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        return ResultForm.createSuccessResultForm(entity, "保存成功！");
    }

    @ApiOperation("获取病害详情")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        Defect defect = makeDefect(defectHandler.selectByPrimaryKey(id));

        return ResultForm.createSuccessResultForm(defect, "查询成功！");
    }

    @ApiOperation("根据缺陷工单ID查询缺陷")
    @RequestMapping("/getByOrderId")
    public ResultForm getByOrderId(@ApiParam(value = "{orderId:orderId}") @RequestBody JsonRequestBody
                                           jsonRequestBody) {
        Long orderId = jsonRequestBody.getLong("orderId");
        if (ObjectUtils.isNull(orderId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "工单ID不能为空！");
        }
        Defect defect = makeDefect(defectHandler.selectEntity(MapUtils.newHashMap("workOrderId", orderId)));
        return ResultForm.createSuccessResultForm(defect, "查询成功！");
    }

    private Defect makeDefect(Defect defect) {
        if (ObjectUtils.isNotNull(defect)) {
            SysUser sysUser = sysUserHandler.selectByPrimaryKey(defect.getDiscoveryUserId());
            defect.setDiscoveryUserName(sysUser.getName());
            Structure structure = structureHandler.selectByPrimaryKey(defect.getStructureId());
            if (ObjectUtils.isNull(structure)) {
                defect.setStructureName("");
            } else {
                defect.setStructureName(structure.getName());
            }
            Device device = deviceHandler.selectByPrimaryKey(defect.getDeviceId());
            if (ObjectUtils.isNull(device)) {
                defect.setDeviceName("");
            } else {
                defect.setDeviceName(device.getName());
            }
        }
        return defect;
    }

    //映射excel表与数据库字段
    private List<ExcelHeader> getExcelHeder() {
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        /* headers.add(new ExcelHeader("结构", "name", String.class));*/
        headers.add(new ExcelHeader("发现时间", 1, "discoveryDate", Date.class, "yyyy年MM月dd日 hh:mm:ss"));
        headers.add(new ExcelHeader("发现人", "discoveryUserName", String.class));
        headers.add(new ExcelHeader("缺陷来源", "source", String.class));
        headers.add(new ExcelHeader("设施名称", "structureName", String.class));
        headers.add(new ExcelHeader("设备名称", "deviceName", String.class));
        headers.add(new ExcelHeader("缺陷类型", "defectTypeName", String.class));
        headers.add(new ExcelHeader("缺陷等级", "defectLevelCode", String.class));
        headers.add(new ExcelHeader("缺陷描述", "remark", String.class));
        headers.add(new ExcelHeader("状态", "status", String.class));
        headers.add(new ExcelHeader("图片", "name", String.class));
        return headers;
    }

    @ApiOperation("审核确认病害缺陷")
    @RequestMapping("/confirm")
    public ResultForm confirm(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("审核确认病害缺陷", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        if (!defectHandler.matchStatus(id, Defect.DEFECT_STATUS_CREATE)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能对待确认的病害进行该操作！");
        }
        Defect entity = defectHandler.selectByPrimaryKey(id);
        entity.setStatus(Defect.DEFECT_STATUS_CONFIRM);
        if (StringUtils.isEmpty(entity.getCode())) {
            entity.setCode(SequenceUtils.getSecondUID("DF"));
        }
        defectHandler.save(entity);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "操作成功！");
    }

    @ApiOperation("创建工单")
    @RequestMapping("/createOrder")
    public ResultForm createOrder(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("创建工单", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        if (!defectHandler.matchStatus(id, Defect.DEFECT_STATUS_CONFIRM)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能对已确认的病害进行该操作！");
        }
        defectHandler.createWorkOrder(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "操作成功！");
    }

    @ApiOperation("删除缺陷")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("删除缺陷", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "ID不能为空！");
        }
        if (!defectHandler.matchStatus(id, Defect.DEFECT_STATUS_CREATE)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "只能对待确认的缺陷进行删除！");
        }
        defectHandler.deleteByPrimaryKey(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "删除成功！");
    }

    @ApiOperation("根据设施或设备查询已有缺陷")
    @RequestMapping("/existList")
    public ResultForm existList
            (@RequestBody @ApiParam(value = "{structureId,deviceId,defectTypeId}") JsonRequestBody jsonRequestBody) {
        Defect entity = jsonRequestBody.tryGet(Defect.class);
        if (ObjectUtils.allIsNull(entity.getStructureId(), entity.getDeviceId())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "结构或者设备不能同时为空！");
        }
        EntityWrapper<Defect> wrapper = new EntityWrapper<>(entity);
        wrapper.orderBy("discovery_date", false);
        return ResultForm.createSuccessResultForm(defectHandler.selectEntities(wrapper), "已有缺陷查询成功！");
    }
}
