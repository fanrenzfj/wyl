package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.bean.ImageInfoBean;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.basic.DocAttach;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.entity.wyl.*;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.basic.DocAttachHandler;
import com.stec.masterdata.handler.basic.DocInfoHandler;
import com.stec.masterdata.handler.basic.SysDicHandler;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.wyl.DefectInspectUserHandler;
import com.stec.masterdata.handler.wyl.DefectTypeHandler;
import com.stec.masterdata.handler.wyl.HistoricalDefectHandler;
import com.stec.utils.*;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import com.stec.wyl.web.utils.ExcelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
import stec.framework.excel.ExcelTemplate;
import stec.framework.excel.handler.ExcelHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：历史缺陷
 *
 * @author Li.peng
 * @create 2018-10-12 11:11
 */
@Api(value = "历史缺陷controller", tags = {"历史缺陷"})
@RestController
@RequestMapping(value = "/rest/historicalDefect", method = RequestMethod.POST)
public class HistoricalDefectRestController extends BaseController {

    @Value("${spring.pic.url}")
    private String PICURL;
    @Autowired
    private DocumentService documentService;
    @Reference
    private HistoricalDefectHandler historicalDefectHandler;
    @Reference
    private DefectTypeHandler defectTypeHandler;
    @Reference
    private DeviceHandler deviceHandler;
    @Reference
    private StructureHandler structureHandler;
    @Reference
    private SysUserHandler sysUserHandler;

    @Reference
    private DocInfoHandler docInfoHandler;
    @Reference
    private DocAttachHandler docAttachHandler;

    @Reference
    private DefectInspectUserHandler defectInspectUserHandler;
    @Autowired
    private ExcelTemplate excelTemplate;

    /**
     * 导入历史缺陷的方法   不对外
     *
     * @param jsonRequestBody
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @ApiOperation("")
    @RequestMapping("/import")
    public ResultForm importDate(@ApiParam(value = "") @RequestBody JsonRequestBody jsonRequestBody) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // todo 注意 pic字段设置为text
        List<HistoricalDefect> list = historicalDefectHandler.selectLists(new HistoricalDefect());

        for (HistoricalDefect historicalDefect : list) {
            System.out.println(historicalDefect.getPic_url());
            if (StringUtils.isBlank(historicalDefect.getPic_url())) {
                continue;
            }

            DocInfo docInfo = new DocInfo();
            docInfo.setCreateDate(TimeUtil.now());
            docInfo.setUpdateUserId(1L);
            docInfo = docInfoHandler.save(docInfo);
            Long docId = docInfo.getId();
            String[] pics = historicalDefect.getPic_url().split(";");
            for (String pic : pics) {
                System.out.println(historicalDefect.getId());
                downloadPicture(pic);
                ImageInfoBean imageInfoBean = documentService.uploadLocalPic("E:/test.jpg");

                DocAttach docAttach = new DocAttach();
                docAttach.setContentType(imageInfoBean.getContentType());
                docAttach.setOriginalFileName(imageInfoBean.getOriginalName());
                docAttach.setFileName(imageInfoBean.getId() + ".jpg");
                docAttach.setLength(imageInfoBean.getLength());
                docAttach.setDocInfoId(docId);
                docAttach.setUpdateDate(TimeUtil.now());
                docAttach = docAttachHandler.save(docAttach);

                historicalDefect.setReportDocId(docId);
                historicalDefectHandler.save(historicalDefect);
//                break;
            }
//            break;
            // TODO: 2018/11/8 图片转存到本地 ，上传服务器，存储docInfo，关联id
        }
        System.out.println("finish");
        return null;
    }


    //链接url下载图片
    private static void downloadPicture(String urlList) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String imageName = "E:/test.jpg";

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("历史缺陷新增/修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] delReportDocIds, Long[] delDealDocIds, HistoricalDefect entity, Long[] inspectUserIds) {
        logger("历史缺陷新增/修改", JSONObject.toJSONString(entity));

        //创建DocInfo对象  现场
        DocInfo reportDocInfo;
        try {
            reportDocInfo = documentService.uploadFiles(multipartRequest, "reportFile", entity.getReportDocId(), delReportDocIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "上报文档保存失败!");
        }
        //历史缺陷对象HistoricalDefect
        entity.setReportDocId(reportDocInfo.getId());
        //创建DocInfo对象  处理反馈
        DocInfo dealDocInfo;
        try {
            dealDocInfo = documentService.uploadFiles(multipartRequest, "dealFile", entity.getDealDocId(), delDealDocIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "处理文档保存失败!");
        }
        entity.setDealDocId(dealDocInfo.getId());
        //判断设施为空，且设备不为空
        if (ObjectUtils.isNull(entity.getStructureId()) && ObjectUtils.isNotNull(entity.getDeviceId())) {
            Device device = deviceHandler.selectByPrimaryKey(entity.getDeviceId());
            if (ObjectUtils.isNotNull(device) && ObjectUtils.isNotNull(device.getStructureId())) {
                Structure structure = structureHandler.selectByPrimaryKey(device.getStructureId());
                if (ObjectUtils.isNotNull(structure)) {
                    entity.setStructureId(structure.getId());
                    entity.setStructureName(structure.getName());
                }
            }
        }

        entity = historicalDefectHandler.save(entity);
        //创建缺陷巡检人对象
        DefectInspectUser param = new DefectInspectUser();
        param.setDefectId(entity.getId());
        //为何进行删除操作
        defectInspectUserHandler.delete(param);
        if (inspectUserIds.length > 0) {
            List<DefectInspectUser> saveList = new ArrayList<>(inspectUserIds.length);
            for (Long inspectUserId : inspectUserIds) {
                DefectInspectUser inspectUser = new DefectInspectUser();
                inspectUser.setDefectId(entity.getId());
                inspectUser.setUserId(inspectUserId);
                saveList.add(inspectUser);
            }
            defectInspectUserHandler.save(saveList);
        }
        return ResultForm.createSuccessResultForm(entity, "保存成功！");
    }

    @ApiOperation("历史缺陷分页列表查询")
    @RequestMapping("/list")
    public ResultForm list(@ApiParam(value = "{deviceOrStructure:device||structure,deviceId or structureId;discoveryBeginDate:discoveryBeginDate;discoveryEndDate;discoveryEndDate;major:major;severity;severity;dealStatus;dealStatus;[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) throws Exception {
        //封装历史缺陷实体
        PageForm pageForm = jsonRequestBody.getPageForm();
        HistoricalDefect entity = jsonRequestBody.tryGet(HistoricalDefect.class);
        if (StringUtils.isEmpty(entity.getMajor())) {
            entity.setMajor(null);
        }
        if (StringUtils.isEmpty(entity.getSeverity())) {
            entity.setSeverity(null);
        }
        if (StringUtils.isEmpty(entity.getDealStatus())) {
            entity.setDealStatus(null);
        }
        if (StringUtils.isEmpty(entity.getDeviceName())) {
            entity.setDeviceName(null);
        }
        Map<String, Object> param = MapUtils.toMapIgnoreNull(entity);
        Date startDate = jsonRequestBody.getDate("discoveryBeginDate");
        Date endDate = jsonRequestBody.getDate("discoveryEndDate");
        String defectTypeName = jsonRequestBody.getString("defectTypeName");
        if (ObjectUtils.allIsNotNull(startDate, endDate)) {
            param.put("startDate", startDate);
            param.put("endDate", endDate);
        }
        if (StringUtils.isNotEmpty(defectTypeName)) {
            param.put("defectTypeName", defectTypeName);
        }

        DataPaging<HistoricalDefect> dataPaging = historicalDefectHandler.selectList(param, pageForm);

        List<HistoricalDefect> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (HistoricalDefect historicalDefect : list) {
            Map<String, Object> historicalDefectMap;
            try {
                historicalDefectMap = MapUtils.toMapIgnoreNull(historicalDefect);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(historicalDefectMap);
            DefectType defectType = defectTypeHandler.selectByPrimaryKey(historicalDefect.getDefectTypeId());
            if (defectType == null) {
                historicalDefectMap.put("defectTypeName", "暂无缺陷类型");
            } else {
                historicalDefectMap.put("defectTypeName", defectType.getName());
            }
            DefectInspectUser inspectUser = new DefectInspectUser();
            inspectUser.setDefectId(historicalDefect.getId());
            List<DefectInspectUser> inspectUsers = defectInspectUserHandler.selectEntities(inspectUser);
            if (CollectionUtils.isNotEmpty(inspectUsers)) {
                List<Long> userIds = new ArrayList<>(inspectUsers.size());
                for (DefectInspectUser user : inspectUsers) {
                    userIds.add(user.getUserId());
                }
                historicalDefectMap.put("inspectUsers", sysUserHandler.selectBatchByIds(userIds));
            }

//            String location = "暂无位置信息";
//            if (HistoricalDefect.DEVICE.equals(historicalDefect.getDeviceOrStructure())) {
//                Device device = deviceHandler.selectByPrimaryKey(historicalDefect.getDeviceOrStructureId());
//                if (device != null) {
//                    location = device.getInstallPosition();
//                }
//            } else {
//                Structure structure = structureHandler.selectByPrimaryKey(historicalDefect.getDeviceOrStructureId());
//                if (structure != null) {
//                    location = structure.getLocation();
//                }
//            }
//            historicalDefectMap.put("location", location);
        }
        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "历史缺陷列表查询成功！");
    }

    @ApiOperation("导出历史缺陷excel")
    @RequestMapping("/downloadList")
    public ResultForm downloadList(@ApiParam(value = "{deviceOrStructure:device||structure,deviceId or structureId;discoveryBeginDate:discoveryBeginDate;discoveryEndDate;discoveryEndDate;major:major;severity;severity;dealStatus;dealStatus;[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody, HttpServletResponse response) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            ExcelResponse.responseHeader(response, "文一路隧道施工期缺陷");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PageForm pageForm = jsonRequestBody.getPageForm();
        pageForm.setCurrPage(1);
        pageForm.setPageSize(2000);
        //调用封装参数的方法 获取DataPaging对象
        DataPaging<HistoricalDefect> dataPaging = getHistoricalDefects(pageForm, jsonRequestBody);
        List<HistoricalDefect> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        //获取HistoricalDefectBetter对象
        List<HistoricalDefectBetter> listBetter = new ArrayList<>();
        for (HistoricalDefect historicalDefect : list) {
            //创建缺陷巡检对象

            //封装HistoricalDefectBetter对象 ，将HistoricalDefect对象放进此对象里
            HistoricalDefectBetter better = new HistoricalDefectBetter();
            //调取excel字段与数据库数据处理映射的方法
            excelServiceMapper(historicalDefect, better);
            //把HistoricalDefectBetter对象放进 集合
            listBetter.add(better);
        }
        //获取header集合对象
        List<ExcelHeader> headers = getExcelHeder();
        try {
            excelTemplate.exportObjects2Excel(listBetter, headers, "施工期缺陷", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }

    //获取前段参数并封装
    private DataPaging<HistoricalDefect> getHistoricalDefects(PageForm pageForm, JsonRequestBody jsonRequestBody) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //封装历史缺陷实体
        HistoricalDefect entity = jsonRequestBody.tryGet(HistoricalDefect.class);
        if (StringUtils.isEmpty(entity.getMajor())) {
            entity.setMajor(null);
        }
        if (StringUtils.isEmpty(entity.getSeverity())) {
            entity.setSeverity(null);
        }
        if (StringUtils.isEmpty(entity.getDealStatus())) {
            entity.setDealStatus(null);
        }
        if (StringUtils.isEmpty(entity.getDeviceName())) {
            entity.setDeviceName(null);
        }
        Map<String, Object> param = MapUtils.toMapIgnoreNull(entity);
        Date startDate = jsonRequestBody.getDate("discoveryBeginDate");
        Date endDate = jsonRequestBody.getDate("discoveryEndDate");
        String defectTypeName = jsonRequestBody.getString("defectTypeName");
        if (ObjectUtils.allIsNotNull(startDate, endDate)) {
            param.put("startDate", startDate);
            param.put("endDate", endDate);
        }
        if (StringUtils.isNotEmpty(defectTypeName)) {
            param.put("defectTypeName", defectTypeName);
        }
        /*List<HistoricalDefect>  ss=historicalDefectHandler.selectEntities(param);*/
        DataPaging<HistoricalDefect> dataPaging = historicalDefectHandler.selectList(param, pageForm);
        return dataPaging;
    }

    //为数据库与excel表字段进行映射
    private void excelServiceMapper(HistoricalDefect historicalDefect, HistoricalDefectBetter better) {
        //根据缺陷ID 封装缺陷类型
        DefectType defectType = defectTypeHandler.selectByPrimaryKey(historicalDefect.getDefectTypeId());
        String defectTypen = "";
        if (defectType == null) {
            defectTypen = "暂无缺陷类型";
        } else {
            defectTypen = defectType.getName();
        }
        better.setHd(historicalDefect);


        //获取设施设备类型
        String deviceOrStructure = historicalDefect.getDeviceOrStructure();
        if (deviceOrStructure != null) {
            if (deviceOrStructure.equals("device")) {
                historicalDefect.setDeviceOrStructure("设备");
            } else if (deviceOrStructure.equals("structure")) {
                historicalDefect.setDeviceOrStructure("设施");
            }
        }
       /* if (deviceOrStructure != null) {
            SysDic sysDic=new SysDic();
            sysDic.setCode(deviceOrStructure);
            sysUserHandler.
        }*/
        //获取处理状态
        String delStatus = historicalDefect.getDealStatus();
        if (delStatus != null) {
            if (delStatus.equals("qualified")) {
                historicalDefect.setDealStatus("已验收且合格");
            } else if (delStatus.equals("add")) {
                historicalDefect.setDealStatus("新增缺陷");
            } else if (delStatus.equals("Unanswered")) {
                historicalDefect.setDealStatus("未回复");
            } else if (delStatus.equals("Immediate_rectify")) {
                historicalDefect.setDealStatus("已回复且立即整改");
            } else if (delStatus.equals("not_rectify")) {
                historicalDefect.setDealStatus("已回复但延后整改");
            } else if (delStatus.equals("Non_acceptance")) {
                historicalDefect.setDealStatus("已修复但未验收");
            } else if (delStatus.equals("Unqualified")) {
                historicalDefect.setDealStatus("已验收未合格");
            } else if (delStatus.equals("programme")) {
                historicalDefect.setDealStatus("整改方案确定中");

            }
        }
        //获取专业
        String major = historicalDefect.getMajor();
        if (major != null) {
            if (major.equals("electromechanical")) {
                historicalDefect.setMajor("机电");
            } else if (major.equals("electricity")) {
                historicalDefect.setMajor("消防");
            } else if (major.equals("civil_engineering")) {
                historicalDefect.setMajor("土建");
            }
        }
        //获取严重程度
        String severity = historicalDefect.getSeverity();
        if (severity != null) {
            if (severity.equals("serious")) {
                historicalDefect.setSeverity("严重");
            } else if (severity.equals("more_serious")) {
                historicalDefect.setSeverity("较严重");
            } else if (severity.equals("general")) {
                historicalDefect.setSeverity("一般");
            } else if (severity.equals("slight")) {
                historicalDefect.setSeverity("轻微");
            }
        }
        //为Better对象 设置缺陷类型
        better.setDefectTypeName(defectTypen);
        //根据设备ID查询设备名称
        Device device = null;
        if (historicalDefect.getDeviceId() != null) {
            device = deviceHandler.selectByPrimaryKey(historicalDefect.getDeviceId());
        }
        if (device != null) {
            better.setDeviceName(device.getName());
        } else {
            better.setDeviceName("");
        }
        //根据设施id查询设施名称
        Structure structure = null;
        if (historicalDefect.getStructureId() != null) {
            structure = structureHandler.selectByPrimaryKey(historicalDefect.getStructureId());
        }
        if (structure != null) {
            better.setStructureName(structure.getName());
        } else {
            better.setStructureName("");
        }
        //根据获取的Boolean类型 维修占道 把true 和false 改为 占道和未占道
        Boolean blockRoad = historicalDefect.getBlockRoad();
        if (blockRoad != null) {
            if (blockRoad) {
                better.setBlockRoad("是");
            } else {
                better.setBlockRoad("否");
            }
        } else {
            better.setBlockRoad("");
        }
        //巡检人 展示
        if (historicalDefect.getInspectUser() == null) {
            DefectInspectUser diu = new DefectInspectUser();

            diu.setDefectId(historicalDefect.getId());

            DefectInspectUser defectInspectUser = defectInspectUserHandler.selectEntity(diu);
            SysUser su = new SysUser();

            if (defectInspectUser.getId() != null) {
                su.setId(defectInspectUser.getUserId());
                su = sysUserHandler.selectByPrimaryKey(su.getId());
                if (historicalDefect.getInspectUser() == null) {
                    historicalDefect.setInspectUser(su.getName());
                }
            }
        }
        //获取报道图片url
        if (historicalDefect.getPic_url() == null) {
            if (historicalDefect.getReportDocId() != null) {
                DocAttach attach = new DocAttach();
                attach.setDocInfoId(historicalDefect.getReportDocId());
                List<DocAttach> docAttaches = docAttachHandler.selectEntities(attach);
                String remark = "";
                for (DocAttach d : docAttaches) {
                    remark = PICURL + d.getFileName() + "；" + remark;
                }
                historicalDefect.setPic_url(remark);
            }

        }
        //获取反馈图片url
        Long docid = historicalDefect.getDealDocId();
        DocAttach da = new DocAttach();
        da.setDocInfoId(docid);
        List<DocAttach> docAttaches = docAttachHandler.selectEntities(da);
        String remark = "";
        for (DocAttach d : docAttaches) {
            /*remark = " http://183.129.193.154:6280/stec-platform-doc/img/" + d.getFileName() +  "；"+ remark;*/
            remark = PICURL + d.getFileName() + "；" + remark;
        }
        better.setDeal_url(remark);


    }


    @ApiOperation("获取历史缺陷")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id 历史缺陷id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "历史缺陷ID不能为空");
        }
        HistoricalDefect historicalDefect = historicalDefectHandler.selectByPrimaryKey(id);

        Map<String, Object> historicalDefectMap;
        try {
            historicalDefectMap = MapUtils.toMapIgnoreNull(historicalDefect);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        DefectType defectType = defectTypeHandler.selectByPrimaryKey(historicalDefect.getDefectTypeId());
        if (defectType == null) {
            historicalDefectMap.put("defectTypeName", "暂无缺陷类型");
        } else {
            historicalDefectMap.put("defectTypeName", defectType.getName());
        }

        DefectInspectUser inspectUser = new DefectInspectUser();
        inspectUser.setDefectId(historicalDefect.getId());
        List<DefectInspectUser> inspectUsers = defectInspectUserHandler.selectEntities(inspectUser);
        if (CollectionUtils.isNotEmpty(inspectUsers)) {
            List<Long> userIds = new ArrayList<>(inspectUsers.size());
            for (DefectInspectUser user : inspectUsers) {
                userIds.add(user.getUserId());
            }
            historicalDefectMap.put("inspectUsers", sysUserHandler.selectBatchByIds(userIds));
        }
//        String location = "";
//        if (HistoricalDefect.DEVICE.equals(historicalDefect.getDeviceOrStructure())) {
//            Device device = deviceHandler.selectByPrimaryKey(historicalDefect.getDeviceOrStructureId());
//            location = device.getInstallPosition();
//        } else if (HistoricalDefect.STRUCTURE.equals(historicalDefect.getDeviceOrStructure())) {
//            Structure structure = structureHandler.selectByPrimaryKey(historicalDefect.getDeviceOrStructureId());
//            location = structure.getLocation();
//        }
//        historicalDefectMap.put("location", location);
        return ResultForm.createSuccessResultForm(historicalDefectMap, "历史缺陷信息获取成功！");
    }

    @ApiOperation("删除历史缺陷")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id 历史缺陷id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("删除历史缺陷", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "历史缺陷id不能为空");
        }
        return ResultForm.createSuccessResultForm(historicalDefectHandler.deleteEntity(id));
    }

//    @ApiOperation("获取项目所有历史缺陷")
//    @RequestMapping("/getByProjectId")
//    public ResultForm getByProjectId(@ApiParam(value = "{id:id 项目id}") @RequestBody JsonRequestBody jsonRequestBody) {
//        Long id = jsonRequestBody.getLong("id");
//        if (ObjectUtils.isNull(id)) {
//            return ResultForm.createErrorResultForm(jsonRequestBody, "项目ID不能为空");
//        }
//        HistoricalDefect historicalDefect = new HistoricalDefect();
//        historicalDefect.setProjectId(id);
//        List<HistoricalDefect> historicalDefects = historicalDefectHandler.selectEntities(historicalDefect);
//        List<Map<String, Object>> retList = new ArrayList<>(historicalDefects.size());
//        for (HistoricalDefect defect : historicalDefects) {
//            Map<String, Object> historicalDefectMap;
//            try {
//                historicalDefectMap = MapUtils.toMapIgnoreNull(defect);
//            } catch (Exception e) {
//                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
//            }
//            retList.add(historicalDefectMap);
//            DefectType defectType = defectTypeHandler.selectByPrimaryKey(defect.getDefectTypeId());
//            String defectTypeName = defectType.getName();
//            historicalDefectMap.put("defectTypeName", defectTypeName);
//            String location = "";
//            if (HistoricalDefect.DEVICE.equals(defect.getDeviceOrStructure())) {
//                Device device = deviceHandler.selectByPrimaryKey(defect.getDeviceOrStructureId());
//                location = device.getInstallPosition();
//            } else if (HistoricalDefect.STRUCTURE.equals(defect.getDeviceOrStructure())) {
//                Structure structure = structureHandler.selectByPrimaryKey(defect.getDeviceOrStructureId());
//                location = structure.getLocation();
//            } else {
//            }
//            historicalDefectMap.put("location", location);
//        }
//        return ResultForm.createSuccessResultForm(retList, "历史缺陷信息获取成功！");
//    }

    @ApiOperation("获取设施或者设备的历史缺陷")
    @RequestMapping("/getByDeviceOrStructureId")
    public ResultForm getByDeviceOrStructureId(@ApiParam(value = "{id:id 设施或者设备id;deviceOrStructure:device||structure;}") @RequestBody JsonRequestBody jsonRequestBody) {
       //git二次提交
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设施或者设备ID不能为空");
        }
        String deviceOrStructure = jsonRequestBody.getString("deviceOrStructure");
        if (StringUtils.isEmpty(deviceOrStructure)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设施或者设备类型不能为空");
        }
        if (!HistoricalDefect.DEVICE.equals(deviceOrStructure) && !HistoricalDefect.STRUCTURE.equals(deviceOrStructure)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设施或者设备类型必须为device或者structure");
        }
        HistoricalDefect historicalDefect = new HistoricalDefect();
        historicalDefect.setDeviceOrStructureId(id);
        historicalDefect.setDeviceOrStructure(deviceOrStructure);
        List<HistoricalDefect> historicalDefects = historicalDefectHandler.selectEntities(historicalDefect);
        List<Map<String, Object>> retList = new ArrayList<>(historicalDefects.size());
        for (HistoricalDefect defect : historicalDefects) {
            Map<String, Object> historicalDefectMap;
            try {
                historicalDefectMap = MapUtils.toMapIgnoreNull(defect);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(historicalDefectMap);
            DefectType defectType = defectTypeHandler.selectByPrimaryKey(defect.getDefectTypeId());
            String defectTypeName = defectType.getName();
            historicalDefectMap.put("defectTypeName", defectTypeName);
            String location = "";
            if (HistoricalDefect.DEVICE.equals(defect.getDeviceOrStructure())) {
                Device device = deviceHandler.selectByPrimaryKey(defect.getDeviceOrStructureId());
                location = device.getInstallPosition();
            } else if (HistoricalDefect.STRUCTURE.equals(defect.getDeviceOrStructure())) {
                Structure structure = structureHandler.selectByPrimaryKey(defect.getDeviceOrStructureId());
                location = structure.getLocation();
            } else {
            }
            historicalDefectMap.put("location", location);
        }
        return ResultForm.createSuccessResultForm(retList, "历史缺陷信息获取成功！");
    }
    //封装excel表映射字段
    private List<ExcelHeader> getExcelHeder() {
        //创建excel Header对象
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("时间", 1, "hd.discoveryDate", Date.class, "yyyy年MM月dd日 hh:mm:ss"));
        headers.add(new ExcelHeader("巡检人", "hd.inspectUser", String.class));
        headers.add(new ExcelHeader("专业", "hd.major", String.class));
        headers.add(new ExcelHeader("设施设备类型", "hd.deviceOrStructure", String.class));
        headers.add(new ExcelHeader("设施名称", "structureName", String.class));
        headers.add(new ExcelHeader("设备名称", "deviceName", String.class));
        headers.add(new ExcelHeader("缺陷类型", "defectTypeName", String.class));
        headers.add(new ExcelHeader("严重程度", "hd.severity", String.class));
        headers.add(new ExcelHeader("位置", "hd.location", String.class));
        headers.add(new ExcelHeader("维修占道", "blockRoad", Boolean.class));
        headers.add(new ExcelHeader("上报情况", "hd.reportSituation", String.class));
        headers.add(new ExcelHeader("上报节点", "hd.reportNode", String.class));
        headers.add(new ExcelHeader("巡检问题描述", "hd.problemRemark", String.class));
        headers.add(new ExcelHeader("现场图片", "hd.pic_url", String.class));
        headers.add(new ExcelHeader("处理状态", "hd.dealStatus", String.class));
        headers.add(new ExcelHeader("反馈情况处理说明", "hd.dealRemark", String.class));
        headers.add(new ExcelHeader("反馈处理图片", "deal_url", String.class));
        return headers;
    }

}
