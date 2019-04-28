package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.masterdata.entity.project.Channel;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.handler.project.ChannelHandler;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.platform.monitor.api.DataHandler;
import com.stec.platform.monitor.usage.InfluxSearchForm;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.TimeUtil;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = {"监测数据查询接口"})
@RestController
@RequestMapping(value = "/rest/data", method = RequestMethod.POST)
public class DataRestController extends BaseController {

    @Reference
    private DataHandler dataHandler;


    @Reference
    private ChannelHandler channelHandler;

    @Reference
    private DeviceHandler deviceHandler;

    @ApiOperation("查询监测数据")
    @RequestMapping("/channelData")
    public ResultForm channelData(@ApiParam(value = "{id:id,start:start,end:end}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        Date start = jsonRequestBody.getDate("start");
        Date end = jsonRequestBody.getDate("end");
        Device device = null;
//        List<Map<String, Object>> dataList=null;
        if (ObjectUtils.isNull(id, start, end)) {
            return ResultForm.createSuccessResultForm(jsonRequestBody, "ChannelId和日期不能为空");
        }
        Channel channel = channelHandler.selectByPrimaryKey(id);

        if (channel != null) {
            device = deviceHandler.selectByPrimaryKey(channel.getDeviceId());
        }
        Map<Long, Object> retMap = new HashMap<>();
        if (device != null) {
            List<Map<String, Object>> dataList = dataHandler.query(device.getPsn(), channel.getCode(), start, end);
//            List<Map<String, Object>> dataList = new ArrayList<>();
            List<Map<String, Object>> retList = new ArrayList<>(dataList.size());
            for (Map<String, Object> obj : dataList) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("time", obj.get("time"));
                map.put("value", obj.get(channel.getCode()));
                retList.add(map);
            }
            retMap.put(id, retList);
        }
        return ResultForm.createSuccessResultForm(retMap, "查询成功！");
    }


    /**
     * 首页结构检测数据
     *
     * @return
     */
    @ApiOperation("查询监测数据")
    @GetMapping("/homepageData")
    public ResultForm homepageData() {

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("11003", "0");
        resultData.put("11004", "0");
        resultData.put("12001", "0");
        resultData.put("12002", "0");
        resultData.put("14001", "0");
        //29001	加速度
        resultData.put("29001", "0");

        List<String> pipes = new ArrayList<>();
        //11003	横径累计变化量
        //11004	纵径累计变化量
        pipes.add("11004");
        pipes.add("11003");
        List<Map<String, Object>> maps = findData(pipes, 16L);
        if (maps != null && maps.size() > 0) {
            resultData.put("11003", MapUtils.getString(maps.get(0), "11003"));
            resultData.put("11004", MapUtils.getString(maps.get(0), "11004"));
        }

        //12001	张开量
        //12002	错台量
        pipes = new ArrayList<>();
        pipes.add("12001");
        pipes.add("12002");
        maps = findData(pipes, 19L);
        if (maps != null && maps.size() > 0) {
            resultData.put("12001", MapUtils.getString(maps.get(0), "12001"));
            resultData.put("12002", MapUtils.getString(maps.get(0), "12002"));
        }

        //14001	累计沉降量
        pipes = new ArrayList<>();
        pipes.add("14001");
        maps = findData(pipes, 22L);
        if (maps != null && maps.size() > 0) {
            resultData.put("14001", MapUtils.getString(maps.get(0), "14001"));
        }


        //29001	加速度
        pipes = new ArrayList<>();
        pipes.add("29001");
        maps = findData(pipes, 75L);
        if (maps != null && maps.size() > 0) {
            resultData.put("29001", MapUtils.getString(maps.get(0), "29001"));
        }


        return ResultForm.createSuccessResultForm(resultData, "查询成功！");
    }


    List<Map<String, Object>> findData(List<String> pipes, Long measurePointId) {
        Channel channel = new Channel();
        channel.setMeasurePointId(measurePointId);
        List<Channel> channels = channelHandler.selectEntities(channel);
        Device device = null;
        List<Long> psns = new ArrayList<>();
        for (Channel c1 : channels) {
            device = deviceHandler.selectByPrimaryKey(c1.getDeviceId());
            if (device != null && device.getPsn() != null)
                psns.add(device.getPsn());
        }
        InfluxSearchForm influxSearchForm = new InfluxSearchForm();
        influxSearchForm.setBeginTime(TimeUtil.firstTimeOfDay(TimeUtil.calendar()).getTime());
        influxSearchForm.setEndTime(TimeUtil.lastTimeOfDay(TimeUtil.calendar()).getTime());
        influxSearchForm.setPsns(psns);
        influxSearchForm.setColumns(pipes);
        influxSearchForm.setStatisticMethod(InfluxSearchForm.STATISTIC_METHOD_MAX);
        List<Map<String, Object>> maps = dataHandler.selectMultiDeviceStatisticDatas(influxSearchForm);
        return maps;
    }


}
