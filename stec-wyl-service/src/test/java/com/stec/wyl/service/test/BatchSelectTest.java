package com.stec.wyl.service.test;

import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.service.project.DeviceService;
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
 * Date: 2018/8/22 0022
 * Time: 10:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class BatchSelectTest {

    @Autowired
    private DeviceService deviceService;

    @Test
    public void test(){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(6L);
        ids.add(7L);
        List<Device> list = deviceService.selectBatchByIds(ids);
        System.out.println("list = " + list);
    }
}
