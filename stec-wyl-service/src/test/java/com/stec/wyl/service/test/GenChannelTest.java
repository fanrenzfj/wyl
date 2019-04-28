package com.stec.wyl.service.test;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.masterdata.entity.project.Channel;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.protocol.MeasurePoint;
import com.stec.masterdata.service.project.ChannelService;
import com.stec.masterdata.service.project.DeviceService;
import com.stec.masterdata.service.protocol.MeasurePointService;
import com.stec.utils.*;
import com.stec.wyl.ServiceApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/9/13
 * Time: 18:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class GenChannelTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MeasurePointService measurePointService;

    @Test
    public void genChannels(){

        Device param = new Device();
        param.setMonitor(true);
        EntityWrapper<Device> wrapper = new EntityWrapper<>(param);
        wrapper.isNotNull("structure_id");
        List<Device> deviceList = deviceService.selectEntities(wrapper);
        List<Channel> saveList = new ArrayList<>();
        List<Device> saveDevices = new ArrayList<>();
        for (Device device : deviceList) {
            List<MeasurePoint> points = measurePointService.selectEntities(MapUtils.newHashMap("deviceTypeId", device.getDeviceTypeId()));
            if(ObjectUtils.isNull(device.getPsn())) {
                device.setPsn(SequenceUtils.getMillisUID());
                saveDevices.add(device);
            }
            for (MeasurePoint point : points) {
                Channel channel = new Channel();
                channel.setDeviceId(device.getId());
                channel.setCode(point.getCode());
                channel = channelService.selectEntity(channel);
                if(ObjectUtils.isNull(channel)) {
                    channel = new Channel();
                    channel.setDeviceId(device.getId());
                    channel.setCode(point.getCode());
                    channel.setName(point.getName());
                    channel.setMeasurePointId(point.getId());
                    channel.setUnit(point.getUnit());
                    channel.setType(point.getType());
                    saveList.add(channel);
                }
            }
            System.out.println();
        }
        if(CollectionUtils.isNotEmpty(saveList)){
            channelService.save(saveList);
        }
        if(CollectionUtils.isNotEmpty(saveDevices)) {
            deviceService.save(saveDevices);
        }
        System.out.println();
    }
}
