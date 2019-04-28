package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.protocol.DeviceType;
import com.stec.masterdata.handler.project.ChannelHandler;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.protocol.DeviceTypeHandler;
import com.stec.platform.monitor.api.DataHandler;
import com.stec.utils.DataTimeUtils;
import com.stec.utils.MapUtils;
import com.stec.utils.MileageUtils;
import com.stec.utils.ObjectUtils;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/10/9 0009
 * Time: 18:56
 */
@Api(tags = {"专项分析"})
@RestController
@RequestMapping(value = "/rest/analysis", method = RequestMethod.POST)
public class AnalysisRestController extends BaseController {

    @Reference
    private DeviceTypeHandler deviceTypeHandler;

    @Reference
    private DeviceHandler deviceHandler;

    @Reference
    private ChannelHandler channelHandler;

    @Reference
    private DataHandler dataHandler;

    /**
     * 设备类型编码，上下线，设备清单（里程顺序）
     */
    private Map<String, Map<String, List<Device>>> deviceMap = new HashMap<>();

    @ApiOperation("全线数据查询")
    @RequestMapping("/lineDatas")
    public ResultForm lineDatas(@ApiParam(value = "{time,deviceTypeCode,pointCode}") @RequestBody JsonRequestBody jsonRequestBody) {
        String deviceTypeCode = jsonRequestBody.getString("deviceTypeCode");
        String pointCode = jsonRequestBody.getString("pointCode");
        Date time = jsonRequestBody.getDate("time");
        return ResultForm.createSuccessResultForm(lineData(time, deviceTypeCode, pointCode), "查询成功！");
    }

    @ApiOperation("全线数据周期查询")
    @RequestMapping("/lineDurantDatas")
    public ResultForm lineDurantDatas(@ApiParam(value = "{start, end, period,deviceTypeCode,pointCode}") @RequestBody JsonRequestBody jsonRequestBody) {
        String deviceTypeCode = jsonRequestBody.getString("deviceTypeCode");
        String pointCode = jsonRequestBody.getString("pointCode");
        Date start = jsonRequestBody.getDate("start");
        Date end = jsonRequestBody.getDate("end");
        String period = jsonRequestBody.getString("period");
        start = DataTimeUtils.roundDate(start, period);
        List<Map<String, Object>> retList = new ArrayList<>();
        while (start.before(end)) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("time", start);
            dataMap.put("data", lineData(start, deviceTypeCode, pointCode));
            retList.add(dataMap);
            start = DataTimeUtils.next(start, period);
        }

        return ResultForm.createSuccessResultForm(retList, "查询成功！");
    }

    @ApiOperation("设备轮播数据查询")
    @RequestMapping("/devicePeriodDatas")
    public ResultForm devicePeriodDatas(@ApiParam(value = "{start, end, period, devicePsn, pointCodes[]}") @RequestBody JsonRequestBody jsonRequestBody) {
        Date start = jsonRequestBody.getDate("start");
        Date end = jsonRequestBody.getDate("end");
        String period = jsonRequestBody.getString("period");
        Long psn = jsonRequestBody.getLong("devicePsn");
        List<String> pipes = jsonRequestBody.getList("pointCodes", String.class);
        Date s = DataTimeUtils.roundDate(start, period);
        List<Map<String, Object>> retList = new ArrayList<>();
        while (s.before(end)) {
            retList.add(deviceData(s, psn, pipes));
            s = DataTimeUtils.next(s, period);
        }
        return ResultForm.createSuccessResultForm(retList, "查询成功！");
    }

    @ApiOperation("设备数据查询")
    @RequestMapping("/deviceData")
    public ResultForm deviceData(@ApiParam(value = "{time, devicePsn, pointCodes[]}") @RequestBody JsonRequestBody jsonRequestBody) {
        Date time = jsonRequestBody.getDate("time");
        Long psn = jsonRequestBody.getLong("devicePsn");
        List<String> pipes = jsonRequestBody.getList("pointCodes", String.class);

        return ResultForm.createSuccessResultForm(deviceData(time, psn, pipes), "查询成功！");
    }

    private Map<String, Object> deviceData(Date time, Long psn, List<String> pipes) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("time", time);
        for (String pipe : pipes) {
            Map<String, Object> data = dataHandler.queryLastData(psn, pipe, time);
            if (data != null){
                retMap.put(pipe, data.get(pipe));
            }
        }
        return retMap;
    }

    private Map<String, List<Map<String, Object>>> lineData(Date time, String deviceTypeCode, String pointCode) {
        Map<String, List<Device>> typeDeviceMap = deviceMap.get(deviceTypeCode);
        if (ObjectUtils.isNull(typeDeviceMap)) {
            typeDeviceMap = new HashMap<>();
            DeviceType deviceType = deviceTypeHandler.selectEntity(MapUtils.newHashMap("code", deviceTypeCode));
            EntityWrapper<Device> wrapper = new EntityWrapper<>();
            wrapper.eq("device_type_id", deviceType.getId());
            wrapper.orderBy("mileage");
            List<Device> list = deviceHandler.selectEntities(wrapper);
            for (Device device : list) {
                String line = MileageUtils.head(device.getMileage());
                List<Device> lineList = typeDeviceMap.get(line);
                if (ObjectUtils.isNull(lineList)) {
                    lineList = new ArrayList<>();
                    typeDeviceMap.put(line, lineList);
                }
                lineList.add(device);
            }
            deviceMap.put(deviceTypeCode, typeDeviceMap);
        }

        Map<String, List<Map<String, Object>>> retMap = new HashMap<>(typeDeviceMap.size());
        for (Map.Entry<String, List<Device>> entry : typeDeviceMap.entrySet()) {
            List<Device> list = entry.getValue();
            List<Map<String, Object>> dataList = new ArrayList<>(list.size());
            for (Device device : list) {
                Map<String, Object> map = new HashMap<>();
                dataList.add(map);
                map.put("mileage", MileageUtils.parse(device.getMileage()));
                map.put("deviceId", device.getId());
                Map<String, Object> data = dataHandler.queryLastData(device.getPsn(), pointCode, time);
                if (ObjectUtils.isNotNull(data)) {
                    map.put("value", data.get(pointCode));
                }
            }
            retMap.put(entry.getKey(), dataList);
        }
        return retMap;
    }
}
