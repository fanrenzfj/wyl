package com.stec.wyl.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stec.framework.metadata.bean.ResultForm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class RoadEvaluateService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据任务code获取道路分析结果
     * @param taskCode
     * @return
     */
    public JSONObject acceptResult(String taskCode) {
        Date now = new Date();
        String md5txt = taskCode + now.getTime() + "fkgjheroiureofsa37thsdfn30486ksldfkejy40956tjemlkfgjsdktg039483orfjmsldktgu3409i3p4rfmjselwkrfj3049u3ngt";
        String sign = DigestUtils.md5Hex(md5txt);
        ResponseEntity<String> resultStr = restTemplate.getForEntity("http://121.41.10.16:9199/api/getResult?TaskNo="+taskCode+"&TimeStamp=" + now.getTime() + "&Sign=" + sign, String.class);
        return JSON.parseObject(resultStr.getBody());
    }
}
