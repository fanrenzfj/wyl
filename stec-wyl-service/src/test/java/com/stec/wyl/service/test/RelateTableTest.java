package com.stec.wyl.service.test;

import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.service.wyl.OrderCarService;
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
 * Date: 2018/8/24 0024
 * Time: 16:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class RelateTableTest {

    @Autowired
    private OrderCarService orderCarService;

    @Test
    public void test() {
        List<Long> carList = new ArrayList<>();
        carList.add(1L);
        carList.add(2L);

        orderCarService.saveCars(7L, carList);
    }

    @Test
    public void testSelect(){
        List<Car> cars = orderCarService.selectCars(8L);
        System.out.println("cars = " + cars);
    }
}
