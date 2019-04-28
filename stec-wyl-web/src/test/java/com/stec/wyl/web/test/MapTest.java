package com.stec.wyl.web.test;

import com.stec.masterdata.entity.wyl.WorkPlan;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.TimeUtil;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 20:34
 */
public class MapTest {

    @Test
    public void obj2Map() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        WorkPlan obj = new WorkPlan();
        obj.setName("计划");
        obj.setId(1L);
        obj.setUpdateDate(TimeUtil.now());
        Map<String, Object> map = MapUtils.toMapIgnoreNull(obj);
        System.out.println("map = " + map);
    }

    @Test
    public void testNull() {
        System.out.println(ObjectUtils.allIsNotNull(1, "LL", "PPP"));
    }
}
