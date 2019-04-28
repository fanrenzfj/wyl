package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.MeasureData;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysPrivilege;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.wyl.*;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.wyl.*;
import com.stec.platform.monitor.api.DataHandler;
import com.stec.platform.monitor.api.DataSendHandler;
import com.stec.platform.monitor.usage.InfluxSearchForm;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.TimeUtil;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import com.stec.wyl.web.service.RoadEvaluateService;
import com.stec.wyl.web.utils.CoodinateCovertor;
import com.stec.wyl.web.utils.LngLat;
import com.stec.wyl.web.wrapper.DiseaseWrapper;
import com.stec.wyl.web.wrapper.LocationWrapper;
import com.stec.wyl.web.wrapper.RoadWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartRequest;
import stec.framework.excel.ExcelTemplate;
import stec.framework.excel.handler.ExcelHeader;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/5 0005
 * Time: 13:46
 */
@Api(value = "道面评估", tags = "上大道面专项评估")
@RestController
@RequestMapping(value = "/rest/roadEvaluate", method = RequestMethod.POST)
public class RoadEvaluateRestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RoadEvaluateService roadEvaluateService;

    @Reference
    private RoadEvaluateTemplateHandler roadEvaluateTemplateHandler;

    @Reference
    private RoadEvaluateReportHandler roadEvaluateReportHandler;

    @Reference
    private RoadEvaluateItemHandler roadEvaluateItemHandler;

    @Reference
    private RoadEvaluateDetailHandler roadEvaluateDetailHandler;

    @Reference
    private DataHandler dataHandler;

    @Reference
    private AnchorPointHandler anchorPointHandler;

    @Autowired
    private RestTemplate restTemplate;

    @Reference
    private RoadDataUploadRecordHandler roadDataUploadRecordHandler;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ExcelTemplate excelTemplate;

    @Reference
    private DeviceHandler deviceHandler;

    @Reference
    private DataSendHandler dataSendHandler;


    @ApiOperation("评估结果列表")
    @RequestMapping("/reportList")
    public ResultForm reportList() {
        EntityWrapper<RoadEvaluateReport> wrapper = new EntityWrapper<>();
        wrapper.orderBy("start_date", false);
        return ResultForm.createSuccessResultForm(roadEvaluateReportHandler.selectEntities(wrapper), "评估列表查询成功！");
    }

    @ApiOperation("根据结果ID获取评估结果树")
    @RequestMapping("/evaluateResult")
    public ResultForm evaluateResult(@ApiParam(value = "reportId*") @RequestBody JsonRequestBody jsonRequestBody) {
        Long reportId = jsonRequestBody.getLong("reportId");
        if (ObjectUtils.isNull(reportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "报告ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(roadEvaluateItemHandler.selectEntities(MapUtils.newHashMap("reportId", reportId)), "评估结果查询成功！");
    }

    @ApiOperation("获取评估详细信息，包含GIS显示")
    @RequestMapping("/evaluateDetails")
    public ResultForm evaluateDetails(@ApiParam(value = "reportId*") @RequestBody JsonRequestBody jsonRequestBody) {
        Long reportId = jsonRequestBody.getLong("reportId");
        if (ObjectUtils.isNull(reportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "报告ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(roadEvaluateReportHandler.evaluateDetails(reportId), "查询成功！");
    }

    @ApiOperation("获取评估结果统计")
    @RequestMapping("/indexState")
    public ResultForm indexState(@ApiParam(value = "id:reportId,index:") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        String index = jsonRequestBody.getString("index");
        if (ObjectUtils.allIsNotNull(id, index)) {
            return ResultForm.createSuccessResultForm(roadEvaluateReportHandler.indexState(id, index), "统计结果查询成功！");
        }
        return ResultForm.createErrorResultForm(jsonRequestBody, "报告ID和统计指标均不能为空！");
    }

    @ApiOperation("同步评估结果，从上大服务")
    @RequestMapping("/synEvaluate")
    public ResultForm synEvaluate() {
        List<RoadEvaluateReport> reports = roadEvaluateReportHandler.selectEntities(MapUtils.newHashMap("download", false));
        for (RoadEvaluateReport report : reports) {
            JSONObject result = roadEvaluateService.acceptResult(report.getCode());
            roadEvaluateReportHandler.sdEvaluate(report, result);
        }
        return ResultForm.createSuccessResultForm(null, "上大评估结果数据同步入库成功！");
    }

    @ApiOperation("删除评估报告")
    @RequestMapping("/deleteReport")
    public ResultForm deleteReport(@ApiParam(value = "id:reportId") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "报告ID不能为空！");
        }
        RoadEvaluateDetail detail = new RoadEvaluateDetail();
        detail.setReportId(id);
        roadEvaluateDetailHandler.delete(detail);

        RoadEvaluateItem item = new RoadEvaluateItem();
        item.setReportId(id);
        roadEvaluateItemHandler.delete(item);

        roadEvaluateReportHandler.deleteByPrimaryKey(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "报告删除成功！");
    }

    @ApiOperation("下载评估报告")
    @RequestMapping("/downloadItems")
    public ResultForm downloadItems(HttpServletResponse response, @ApiParam(value = "id:reportId*") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "报告ID不能为空！");
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Access-control-Expose-Headers", "attachment");
        response.setHeader("attachment", "download.xlsx");

        List<RoadEvaluateItem> items = roadEvaluateItemHandler.selectEntities(MapUtils.newHashMap("reportId", 4));

        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("结构", "name", String.class));
        headers.add(new ExcelHeader("评价时间", 1, "startDate", Date.class, "yyyy年MM月dd日"));
        headers.add(new ExcelHeader("RDI", "rdi", Double.class));
        headers.add(new ExcelHeader("RQI", "rqi", Double.class));
        headers.add(new ExcelHeader("PCI", "pci", Double.class));
        headers.add(new ExcelHeader("SRI", "sri", Double.class));
        headers.add(new ExcelHeader("PSSI", "pssi", Double.class));
        headers.add(new ExcelHeader("MQI", "mqi", Double.class));
        headers.add(new ExcelHeader("SCI", "sci", Double.class));
        headers.add(new ExcelHeader("PQI", "pqi", Double.class));

        try {
            excelTemplate.exportObjects2Excel(items, headers, "路面评估", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }


    @ApiOperation("查找所有数据记录")
    @GetMapping("/findDates")
    public ResultForm findDates() {
        try {
            return ResultForm.createSuccessResultForm(roadDataUploadRecordHandler.selectEntities(new RoadDataUploadRecord()), "启动成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    @ApiOperation("查找所有数据记录，分页")
    @PostMapping("/findPageDates")
    public ResultForm findPageDates(@RequestBody JsonRequestBody jsonRequestBody) {
        try {
            PageForm pageForm = jsonRequestBody.getPageForm();
            return ResultForm.createSuccessResultForm(roadDataUploadRecordHandler.selectEntities(new RoadDataUploadRecord(), pageForm), "查找成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 平整度数据上传，直接发送给上大
     *
     * @param multipartRequest
     * @return
     */
    @ApiOperation("上传平整度，传递给上大")
    @RequestMapping("/uploadIriExcel")
    public ResultForm uploadExcelTest(MultipartRequest multipartRequest) {
        try {
            List<RoadWrapper> wrappers = documentService.restUploadExcel(multipartRequest, "file", RoadWrapper.class);
            return ResultForm.createSuccessResultForm(uploadIriExcel(wrappers), "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 上传病害信息，直接入influxDB
     *
     * @param multipartRequest
     * @return
     */
    @ApiOperation("上传病害信息，传递给上大")
    @RequestMapping("/uploadDiseaseExcel")
    public ResultForm uploadExcelTest2(MultipartRequest multipartRequest) {
        try {
            List<DiseaseWrapper> wrappers = documentService.restUploadExcel(multipartRequest, "file", DiseaseWrapper.class);
            return ResultForm.createSuccessResultForm(uploadDiseaseExcel(wrappers), "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 平整度数据入influxDB数据库
     *
     * @param multipartRequest
     * @return
     */
    @ApiOperation("上传平整度信息给influxDB")
    @RequestMapping("/uploadIriInfluxDB")
    public ResultForm uploadIriInfluxDB(MultipartRequest multipartRequest) {
        try {
            logger.info("上传平整度信息给influxDB");
            Date dataDate = null;

            Device device = new Device();
            device.setDeviceTypeId(124L);
            List<Device> devices = deviceHandler.selectEntities(device);
            Map<String, Long> deviceMap = new HashMap<>();
            for (Device d : devices) {
                deviceMap.put(d.getCode(), d.getPsn());
            }
            List<RoadWrapper> wrappers = documentService.restUploadExcel(multipartRequest, "file", RoadWrapper.class);
            Long time = 0L;
            List<MeasureData> measureDatas = new ArrayList<>();
            MeasureData measureData;
            for (RoadWrapper wrapper : wrappers) {
                if (wrapper.getIri() == null) {
                    return ResultForm.createErrorResultForm("文件格式错误");
                }
                measureData = new MeasureData();
                if (wrapper.getDate().length() >= 13) {
                    Long mills = Long.valueOf(wrapper.getDate());
                    wrapper.setDate(TimeUtil.format(new Date(mills)));
                } else {
                    wrapper.setDate(TimeUtil.format(TimeUtil.addDays(TimeUtil.parseDate("1899-12-31 0:00:00"), Integer.valueOf(wrapper.getDate().split("\\.")[0]))));
                }

                if (time.equals(0L)) {
                    time = TimeUtil.parseDate(wrapper.getDate()).getTime();
                } else {
                    time = time + 1000;
                }
                if (null == dataDate) {
                    dataDate = TimeUtil.parseDate(TimeUtil.format(time, "yyyy-MM-dd 00:00:00"));
                }


                measureData.setTime(time);
                measureData.setPsn(deviceMap.get(wrapper.getLaneNo()));
                measureData.setDataTime(new Date(time));
                wrapper.setLineNo(null);
                wrapper.setLaneNo(null);
                wrapper.setDate(null);
                wrapper.setId(null);
                measureData.setData(MapUtils.toMapIgnoreNull(wrapper));
                measureDatas.add(measureData);
            }


            RoadDataUploadRecord roadDataUploadRecord = new RoadDataUploadRecord();
            roadDataUploadRecord.setDataTime(dataDate);
            roadDataUploadRecord = roadDataUploadRecordHandler.selectEntity(roadDataUploadRecord);
            //首次上传数据
            if (roadDataUploadRecord == null) {
                dataSendHandler.send(measureDatas);
                RoadDataUploadRecord road = new RoadDataUploadRecord();
                road.setSendToAnalysis(false);
                road.setDiseaseRecord(false);
                road.setIriDataUploadTime(new Date());
                road.setStartAnalysis(false);
                road.setIriRecord(true);
                road.setName(TimeUtil.format(dataDate, "yyyy-MM-dd") + "道面数据");
                road.setDataTime(dataDate);
                roadDataUploadRecordHandler.save(road);
            } else if (roadDataUploadRecord.getSendToAnalysis()) {
                return ResultForm.createErrorResultForm("已经启动分析，无法修改数据");
            } else if (roadDataUploadRecord.getIriRecord()) {
                // TODO: 2018/12/7 删除influxDB，重新插入
                for (Device device1 : devices) {
                    dataHandler.deleteByPsnInDay(device1.getPsn(), new Date(time));
                }
                dataSendHandler.send(measureDatas);
            } else {
                //已经上传病害，没有上传平整度
                dataSendHandler.send(measureDatas);
                roadDataUploadRecord.setIriDataUploadTime(new Date());
                roadDataUploadRecord.setDataTime(dataDate);
                roadDataUploadRecord.setIriRecord(true);
                roadDataUploadRecordHandler.save(roadDataUploadRecord);
            }


            return ResultForm.createSuccessResultForm(null, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 平整度数据入influxDB数据库
     *
     * @param multipartRequest
     * @return
     */
    @ApiOperation("上传病害信息给influxDB")
    @RequestMapping("/uploadDiseaseInfluxDB")
    public ResultForm uploadDiseaseInfluxDB(MultipartRequest multipartRequest) {
        try {
            Date dataDate = null;
            Device device = new Device();
            device.setDeviceTypeId(124L);
            List<Device> devices = deviceHandler.selectEntities(device);
            Map<String, Long> deviceMap = new HashMap<>();
            for (Device d : devices) {
                deviceMap.put(d.getCode(), d.getPsn());
            }
            List<DiseaseWrapper> wrappers = documentService.restUploadExcel(multipartRequest, "file", DiseaseWrapper.class);
            Long time = 0L;
            List<MeasureData> measureDatas = new ArrayList<>();
            MeasureData measureData;
            for (DiseaseWrapper wrapper : wrappers) {
                if (wrapper.getDis() == null) {
                    return ResultForm.createErrorResultForm("文件格式错误");
                }
                measureData = new MeasureData();
                if (wrapper.getDate().length() >= 13) {
                    Long mills = Long.valueOf(wrapper.getDate());
                    wrapper.setDate(TimeUtil.format(new Date(mills)));
                } else {
                    wrapper.setDate(TimeUtil.format(TimeUtil.addDays(TimeUtil.parseDate("1899-12-31 0:00:00"), Integer.valueOf(wrapper.getDate().split("\\.")[0]))));
                }

                if (time.equals(0L)) {
                    time = TimeUtil.parseDate(wrapper.getDate()).getTime();
                } else {
                    time = time + 1000;
                }

                if (null == dataDate) {
                    dataDate = TimeUtil.parseDate(TimeUtil.format(time, "yyyy-MM-dd 00:00:00"));
                }
                measureData.setTime(time);
                measureData.setPsn(deviceMap.get(wrapper.getLaneNo()));
                measureData.setDataTime(new Date(time));
                wrapper.setLineNo(null);
                wrapper.setLaneNo(null);
                wrapper.setDate(null);
                wrapper.setId(null);
                measureData.setData(MapUtils.toMapIgnoreNull(wrapper));
                measureDatas.add(measureData);
            }


            RoadDataUploadRecord roadDataUploadRecord = new RoadDataUploadRecord();
            roadDataUploadRecord.setDataTime(dataDate);
            roadDataUploadRecord = roadDataUploadRecordHandler.selectEntity(roadDataUploadRecord);
            //首次上传数据
            if (roadDataUploadRecord == null) {
                dataSendHandler.send(measureDatas);
                RoadDataUploadRecord road = new RoadDataUploadRecord();
                road.setSendToAnalysis(false);
                road.setDiseaseRecord(true);
                road.setStartAnalysis(false);
                road.setDataTime(dataDate);
                road.setDiseaseDataUploadTime(new Date());
                road.setIriRecord(false);
                road.setName(TimeUtil.format(dataDate, "yyyy-MM-dd") + "道面数据");
                roadDataUploadRecordHandler.save(road);
            } else if (roadDataUploadRecord.getSendToAnalysis()) {
                return ResultForm.createErrorResultForm("已经启动分析，无法修改数据");
            } else if (roadDataUploadRecord.getDiseaseRecord()) {
                // TODO: 2018/12/7 删除influxDB，重新插入
                for (Device device1 : devices) {
                    dataHandler.deleteByPsnInDay(device1.getPsn(), new Date(time));
                }
                dataSendHandler.send(measureDatas);
            } else {
//                已经上传平整度，没有上传病害
                dataSendHandler.send(measureDatas);
                roadDataUploadRecord.setDataTime(dataDate);
                roadDataUploadRecord.setDiseaseRecord(true);
                roadDataUploadRecord.setDiseaseDataUploadTime(new Date());
                roadDataUploadRecordHandler.save(roadDataUploadRecord);
            }

            return ResultForm.createSuccessResultForm(null, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 从influxDB查找数据
     *
     * @param startDay
     * @param endDay
     * @return
     */
    @ApiOperation("把病害数据和路面信息发送给上大")
    @GetMapping("/sendToShangDa")
    public ResultForm sendToShangDa(String startDay, String endDay) {
        try {
            Date dataTime = null;
            Device device = new Device();
            device.setDeviceTypeId(124L);
            List<Device> devices = deviceHandler.selectEntities(device);
            Map<Long, String> deviceMap = new HashMap<>();
            for (Device d : devices) {
                deviceMap.put(d.getPsn(), d.getCode());
            }
            String[] iris = {"psn", "start", "end", "iri", "rd", "bpn", "LO"};

            InfluxSearchForm searchForm = new InfluxSearchForm();
            searchForm.setColumns(Arrays.asList(iris));
            searchForm.addPsns(devices.stream().map(e -> e.getPsn()).collect(Collectors.toList()));
            searchForm.setBeginTime(TimeUtil.parseDate(startDay + " 00:00:00"));
            searchForm.setEndTime(TimeUtil.parseDate(endDay + " 23:59:59"));

            List<Map<String, Object>> maps = dataHandler.query(searchForm);

            for (Map<String, Object> map : maps) {
                map.put("laneNo", MapUtils.getString(deviceMap, MapUtils.getLong(map, "psn")));
                map.put("lineNo", findLineNo(MapUtils.getString(map, "laneNo")));
                map.put("date", TimeUtil.format(MapUtils.getDate(map, "time"), "yyyy-MM-dd 00:00:00"));
                if (dataTime == null) {
                    dataTime = TimeUtil.parseDate(TimeUtil.format(MapUtils.getDate(map, "time"), "yyyy-MM-dd 00:00:00"));
                }
            }
            List<RoadWrapper> roadWrappers = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                roadWrappers.add(MapUtils.toObject(RoadWrapper.class, map));
            }

            RoadDataUploadRecord roadDataUploadRecord = new RoadDataUploadRecord();
            roadDataUploadRecord.setIriRecord(true);
            roadDataUploadRecord.setDiseaseRecord(true);
            roadDataUploadRecord.setSendToAnalysis(false);
            roadDataUploadRecord.setStartAnalysis(false);
            roadDataUploadRecord.setDataTime(dataTime);
            roadDataUploadRecord = roadDataUploadRecordHandler.selectEntity(roadDataUploadRecord);
            if (roadDataUploadRecord != null) {
                roadDataUploadRecord = new RoadDataUploadRecord();
                roadDataUploadRecord.setSendToAnalysis(true);
                roadDataUploadRecord.setDataAnalysisTime(new Date());
                roadDataUploadRecord.setDataTime(dataTime);
                roadDataUploadRecordHandler.updateByDataDate(roadDataUploadRecord);
            } else {
                return ResultForm.createErrorResultForm("状态错误，请确保平整度和病害信息都已经上传，并且是首次启动分析");
            }
            // 传数据给上大
            uploadIriExcel(roadWrappers);

            String[] diseases = {"psn", "dis", "pnt", "catalog", "name", "severity", "location", "depth", "length", "width"};
            searchForm.setColumns(Arrays.asList(diseases));
            // 2018/12/10
            maps = dataHandler.query(searchForm);
            for (Map<String, Object> map : maps) {
                map.put("laneNo", MapUtils.getString(deviceMap, MapUtils.getLong(map, "psn")));
                map.put("lineNo", findLineNo(MapUtils.getString(map, "laneNo")));
                map.put("date", TimeUtil.format(MapUtils.getDate(map, "time"), "yyyy-MM-dd 00:00:00"));
            }
            List<DiseaseWrapper> diseaseWrappers = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                diseaseWrappers.add(MapUtils.toObject(DiseaseWrapper.class, map));
            }
            // 传数据给上大
            uploadDiseaseExcel(diseaseWrappers);

            return ResultForm.createSuccessResultForm(null, "启动分析成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 启动分析模型
     *
     * @param
     * @param
     * @return
     */
    @ApiOperation("启动分析模型")
    @PostMapping("/startModel")
    public ResultForm startModel(@RequestBody JsonRequestBody jsonRequestBody) {
        try {
            RoadDataUploadRecord roadRecord = roadDataUploadRecordHandler.selectByPrimaryKey(jsonRequestBody.getLong("id"));
            String start = TimeUtil.format(roadRecord.getDataTime(), "yyyy-MM-dd");
            String end = TimeUtil.format(roadRecord.getDataTime().getTime() + 1000 * 60 * 60 * 24, "yyyy-MM-dd");
            sendToShangDa(start, end);

            Date now = new Date();
            String md5txt = "1" + start + end + now.getTime() + "fkgjheroiureofsa37thsdfn30486ksldfkejy40956tjemlkfgjsdktg039483orfjmsldktgu3409i3p4rfmjselwkrfj3049u3ngt";
            String sign = DigestUtils.md5Hex(md5txt);
            JSONObject paramMap = new JSONObject();
            paramMap.put("ModelType", 1);
            paramMap.put("start", start);
            paramMap.put("end", end);
            paramMap.put("TimeStamp", now.getTime());
            paramMap.put("Sign", sign);
            logger.info("启动分析参数：" + paramMap.toJSONString());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> formEntity = new HttpEntity<String>(paramMap.toString(), requestHeaders);
            ResponseEntity<String> resultStr = restTemplate.postForEntity("http://121.41.10.16:9199/api/startAnalysis", formEntity, String.class);
            JSONObject jsonObject = JSON.parseObject(resultStr.getBody());

            RoadEvaluateReport roadEvaluateReport = new RoadEvaluateReport();
            roadEvaluateReport.setStartDate(TimeUtil.parseDate(start));
            roadEvaluateReport.setEndDate(TimeUtil.parseDate(end));
            roadEvaluateReport.setDownload(false);
            roadEvaluateReport.setName(start + "道面评估报告");
            if (jsonObject.getBoolean("IsSuccess")) {
                roadEvaluateReport.setCode(jsonObject.getString("Message"));
            }
            RoadEvaluateReport roadE = roadEvaluateReportHandler.save(roadEvaluateReport);

            RoadDataUploadRecord roadDataUploadRecord = new RoadDataUploadRecord();
            roadDataUploadRecord.setIriRecord(true);
            roadDataUploadRecord.setDiseaseRecord(true);
            roadDataUploadRecord.setSendToAnalysis(true);
            roadDataUploadRecord.setStartAnalysis(false);
            roadDataUploadRecord.setDataTime(TimeUtil.parseDate(start));
            roadDataUploadRecord = roadDataUploadRecordHandler.selectEntity(roadDataUploadRecord);
            if (roadDataUploadRecord != null) {
                roadDataUploadRecord = new RoadDataUploadRecord();
                roadDataUploadRecord.setStartAnalysis(true);
                roadDataUploadRecord.setRoadEvaluateReportId(roadE.getId());
                roadDataUploadRecord.setDataTime(TimeUtil.parseDate(start));
                roadDataUploadRecordHandler.updateByDataDate(roadDataUploadRecord);
            } else {
                return ResultForm.createErrorResultForm("状态错误，请确保平整度和病害信息都已经上传，并且是首次启动分析");
            }

            logger.info("启动分析模型结果：" + jsonObject.toJSONString());
            return ResultForm.createSuccessResultForm(jsonObject, "启动成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 把百度平面坐标转换为高德坐标
     *
     * @param multipartRequest
     * @return
     */
    @ApiOperation("把百度平面坐标转换为高德坐标")
    @RequestMapping("/changeLocation")
    public ResultForm changeLocation(MultipartRequest multipartRequest) {
        try {
            List<LocationWrapper> wrappers = documentService.restUploadExcel(multipartRequest, "file", LocationWrapper.class);
            for (LocationWrapper wrapper : wrappers) {
                Map<String, Double> map = CoodinateCovertor.convertMC2LL(wrapper.getPOINT_X(), wrapper.getPOINT_Y());
                Double lng = map.get("lng");
                Double lat = map.get("lat");
                LngLat lngLat_bd = new LngLat(lng, lat);
                LngLat lngLat = CoodinateCovertor.bd_decrypt(lngLat_bd);
                System.out.println(lngLat.getLongitude() + "\t" + lngLat.getLantitude());
            }

            return ResultForm.createSuccessResultForm(null, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    /**
     * 获取excel下载模板
     *
     * @param
     * @return
     */
    @ApiOperation("获取excel下载模板")
    @RequestMapping("/getExcelModel")
    public ResultForm getExcelModel(@RequestBody JsonRequestBody jsonRequestBody) {
        try {
            String code = jsonRequestBody.getString("code");
            if (code.equals("iri")) {
                return ResultForm.createSuccessResultForm("http://183.129.193.154:6280/stec-platform-doc/doc/wKh0AlwcRVqAJupkAE7a3mCe82859.xlsx", "上传成功！");
            } else if (code.equals("pci")) {
                return ResultForm.createSuccessResultForm("http://183.129.193.154:6280/stec-platform-doc/doc/wKh0AlwcRcCAYFK-AAApNbzgXq830.xlsx", "上传成功！");
            }
            return ResultForm.createSuccessResultForm(null, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
    }


    String findLineNo(String laneNo) {
        switch (laneNo) {
            case "N1":
                return "N";
            case "N2":
                return "N";
            case "ES":
                return "N";
            case "EN":
                return "N";
            case "NZD1":
                return "N";
            case "NZD2":
                return "N";
            case "S1":
                return "S";
            case "S2":
                return "S";
            case "NE":
                return "S";
            case "SE":
                return "S";
            case "SZD1":
                return "S";
            case "SZD2":
                return "S";
        }
        return "";
    }

    public JSONObject uploadIriExcel(List<RoadWrapper> wrappers) {
        JSONObject WYL = new JSONObject();
        JSONObject N = new JSONObject();
        JSONObject S = new JSONObject();

        JSONArray N1 = new JSONArray();
        JSONArray N2 = new JSONArray();
        JSONArray ES = new JSONArray();
        JSONArray EN = new JSONArray();
        JSONArray NZD1 = new JSONArray();
        JSONArray NZD2 = new JSONArray();

        JSONArray S1 = new JSONArray();
        JSONArray S2 = new JSONArray();
        JSONArray NE = new JSONArray();
        JSONArray SE = new JSONArray();
        JSONArray SZD1 = new JSONArray();
        JSONArray SZD2 = new JSONArray();

        for (RoadWrapper wrapper : wrappers) {
            if (wrapper.getDate().length() >= 13) {
                Long mills = Long.valueOf(wrapper.getDate());
                wrapper.setDate(TimeUtil.format(new Date(mills)));
            } else {
                try {
                    wrapper.setDate(TimeUtil.format(TimeUtil.addDays(TimeUtil.parseDate("1899-12-31 0:00:00"), Integer.valueOf(wrapper.getDate().split("\\.")[0]))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String laneNo = wrapper.getLaneNo();
            wrapper.setLaneNo(null);
            wrapper.setLineNo(null);
            wrapper.setId(null);
            formatLine(laneNo, N1, N2, ES, EN, NZD1, NZD2, S1, S2, NE, SE, SZD1, SZD2, wrapper);
        }

        N.put("N1", N1);
        N.put("N2", N2);
        N.put("ES", ES);
        N.put("EN", EN);
        N.put("NZD1", NZD1);
        N.put("NZD2", NZD2);

        S.put("S1", S1);
        S.put("S2", S2);
        S.put("NE", NE);
        S.put("SE", SE);
        S.put("SZD1", SZD1);
        S.put("SZD2", SZD2);

        WYL.put("N", N);
        WYL.put("S", S);

        System.out.println(WYL.toJSONString());
        System.out.println("结束");
        Date now = new Date();
        String data = WYL.toJSONString();
        String md5txt = "1" + data + now.getTime() + "fkgjheroiureofsa37thsdfn30486ksldfkejy40956tjemlkfgjsdktg039483orfjmsldktgu3409i3p4rfmjselwkrfj3049u3ngt";
        String sign = DigestUtils.md5Hex(md5txt);
        JSONObject paramMap = new JSONObject();
        paramMap.put("ModelType", 1);
        paramMap.put("Data", data);
        paramMap.put("TimeStamp", now.getTime());
        paramMap.put("Sign", sign);
        logger.info("参数：" + paramMap.toJSONString());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(paramMap.toString(), requestHeaders);
        ResponseEntity<String> resultStr = restTemplate.postForEntity("http://121.41.10.16:9199/api/upload/irird", formEntity, String.class);
        JSONObject jsonObject = JSON.parseObject(resultStr.getBody());
        logger.info("结果：" + jsonObject.toJSONString());
        return jsonObject;
    }


    public JSONObject uploadDiseaseExcel(List<DiseaseWrapper> wrappers) {

        JSONObject WYL = new JSONObject();
        JSONObject N = new JSONObject();
        JSONObject S = new JSONObject();

        JSONArray N1 = new JSONArray();
        JSONArray N2 = new JSONArray();
        JSONArray ES = new JSONArray();
        JSONArray EN = new JSONArray();
        JSONArray NZD1 = new JSONArray();
        JSONArray NZD2 = new JSONArray();

        JSONArray S1 = new JSONArray();
        JSONArray S2 = new JSONArray();
        JSONArray NE = new JSONArray();
        JSONArray SE = new JSONArray();
        JSONArray SZD1 = new JSONArray();
        JSONArray SZD2 = new JSONArray();

        for (DiseaseWrapper wrapper : wrappers) {
            if (wrapper.getDate().length() >= 13) {
                Long mills = Long.valueOf(wrapper.getDate());
                wrapper.setDate(TimeUtil.format(new Date(mills)));
            } else {
                try {
                    wrapper.setDate(TimeUtil.format(TimeUtil.addDays(TimeUtil.parseDate("1899-12-31 0:00:00"), Integer.valueOf(wrapper.getDate().split("\\.")[0]))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String laneNo = wrapper.getLaneNo();
            wrapper.setLaneNo(null);
            wrapper.setLineNo(null);
            wrapper.setId(null);
            formatLine(laneNo, N1, N2, ES, EN, NZD1, NZD2, S1, S2, NE, SE, SZD1, SZD2, wrapper);
        }

        N.put("N1", N1);
        N.put("N2", N2);
        N.put("ES", ES);
        N.put("EN", EN);
        N.put("NZD1", NZD1);
        N.put("NZD2", NZD2);

        S.put("S1", S1);
        S.put("S2", S2);
        S.put("NE", NE);
        S.put("SE", SE);
        S.put("SZD1", SZD1);
        S.put("SZD2", SZD2);

        WYL.put("N", N);
        WYL.put("S", S);

        System.out.println(WYL.toJSONString());
        System.out.println("结束2");
        Date now = new Date();
        String data = WYL.toJSONString();
        String md5txt = "1" + data + now.getTime() + "fkgjheroiureofsa37thsdfn30486ksldfkejy40956tjemlkfgjsdktg039483orfjmsldktgu3409i3p4rfmjselwkrfj3049u3ngt";
        String sign = DigestUtils.md5Hex(md5txt);
        JSONObject paramMap = new JSONObject();
        paramMap.put("ModelType", 1);
        paramMap.put("Data", data);
        paramMap.put("TimeStamp", now.getTime());
        paramMap.put("Sign", sign);
        logger.info("参数：" + paramMap.toJSONString());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(paramMap.toString(), requestHeaders);
        ResponseEntity<String> resultStr = restTemplate.postForEntity("http://121.41.10.16:9199/api/upload/disease", formEntity, String.class);
        JSONObject jsonObject = JSON.parseObject(resultStr.getBody());
        logger.info("结果：" + jsonObject.toJSONString());
        return jsonObject;
    }


    private void formatLine(String laneNo, JSONArray N1, JSONArray N2, JSONArray ES, JSONArray EN, JSONArray NZD1, JSONArray NZD2,
                            JSONArray S1, JSONArray S2, JSONArray NE, JSONArray SE, JSONArray SZD1, JSONArray SZD2, Object wrapper) {

        switch (laneNo) {
            case "N1":
                N1.add(JSONObject.toJSON(wrapper));
                break;
            case "N2":
                N2.add(JSONObject.toJSON(wrapper));
                break;
            case "ES":
                ES.add(JSONObject.toJSON(wrapper));
                break;
            case "EN":
                EN.add(JSONObject.toJSON(wrapper));
                break;
            case "NZD1":
                NZD1.add(JSONObject.toJSON(wrapper));
                break;
            case "NZD2":
                NZD2.add(JSONObject.toJSON(wrapper));
                break;
            case "S1":
                S1.add(JSONObject.toJSON(wrapper));
                break;
            case "S2":
                S2.add(JSONObject.toJSON(wrapper));
                break;
            case "NE":
                NE.add(JSONObject.toJSON(wrapper));
                break;
            case "SE":
                SE.add(JSONObject.toJSON(wrapper));
                break;
            case "SZD1":
                SZD1.add(JSONObject.toJSON(wrapper));
                break;
            case "SZD2":
                SZD2.add(JSONObject.toJSON(wrapper));
                break;
        }
    }

    @ApiOperation("下载百米级评价数据")
    @RequestMapping("/downloadEvaData")
    public ResultForm downloadEvaData(HttpServletResponse response, @ApiParam(value = "reportId:reportId") @RequestBody JsonRequestBody jsonRequestBody) {
        Long reportId = jsonRequestBody.getLong("reportId");
        if (ObjectUtils.isNull(reportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "请先选择评价结果！");
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Access-control-Expose-Headers", "attachment");
        response.setHeader("attachment", "evaDataReport.xlsx");

        List<RoadEvaluateDetail> items = roadEvaluateDetailHandler.selectEvaDataReport(reportId);

        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("评价时间", 1, "startDate", Date.class, "yyyy年MM月dd日"));
        headers.add(new ExcelHeader("线路号", "lineCode", String.class));
        headers.add(new ExcelHeader("车道号", "code", String.class));
        headers.add(new ExcelHeader("起点里程km", "start", Double.class));
        headers.add(new ExcelHeader("结束里程km", "end", Double.class));
        headers.add(new ExcelHeader("车辙RD", "rd", Double.class));
        headers.add(new ExcelHeader("平整度IRI", "iri", Double.class));
        headers.add(new ExcelHeader("RDI", "rdi", Double.class));
        headers.add(new ExcelHeader("RQI", "rqi", Double.class));
        headers.add(new ExcelHeader("PCI", "pci", Double.class));
        headers.add(new ExcelHeader("SRI", "sri", Double.class));
        headers.add(new ExcelHeader("PSSI", "pssi", Double.class));
        headers.add(new ExcelHeader("PQI", "pqi", Double.class));

        try {
            excelTemplate.exportObjects2Excel(items, headers, "百米级评估数据", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }
}
