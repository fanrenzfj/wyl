package com.stec.wyl.web.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stec.framework.form.JsonRequestBody;
import com.stec.masterdata.entity.wyl.WorkOrder;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/24 0024
 * Time: 15:45
 */
public class JsonTest {

    @Test
    public void test() {
        String json = "{\"cars\":\"\",\"process\":\"0\",\"name\":\"新建工单\",\"remark\":\"新建工单描述\",\"id\":\"\",\"source\":\"临时工单\",\"planStartDate\":\"\",\"type\":\"-\",\"planEndDate\":\"\",\"items\":[{\"showDeviceList\":[{\"deviceTypeId\":5,\"code\":\"ST20180809095617004\",\"productId\":6,\"installDate\":1533744000000,\"manufacturerId\":4,\"name\":\"南坡1号GNSS\",\"structureId\":80,\"psn\":\"20180809095617004\",\"id\":3,\"projectId\":5},{\"deviceTypeId\":5,\"code\":\"ST20180809100136006\",\"productId\":6,\"installDate\":1533744000000,\"manufacturerId\":4,\"name\":\"南坡2号GNSS\",\"structureId\":80,\"psn\":\"20180809100136006\",\"id\":4,\"projectId\":5},{\"deviceTypeId\":5,\"code\":\"ST20180809100227007\",\"productId\":6,\"installDate\":1533744000000,\"manufacturerId\":4,\"name\":\"南坡3号GNSS\",\"structureId\":80,\"psn\":\"20180809100227007\",\"id\":5,\"projectId\":5}],\"structureName\":\"中段\",\"deviceList\":[3,4,5],\"structureId\":80,\"remark\":\"123\",\"id\":\"\"}]}";
        JSONObject body = JSONObject.parseObject(json);

        WorkOrder order = JSON.toJavaObject(body, WorkOrder.class);

        System.out.println();

    }
}
