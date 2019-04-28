package com.stec.wyl.web.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/18 0018
 * Time: 16:55
 */
public class TokenTest {

    private HttpClient httpClient = HttpClients.createDefault();

    @Test
    public void getToken() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "root");
        jsonObject.put("password", "123456");
        StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        entity.setContentType("application/json");

        HttpPost post = new HttpPost("http://192.168.99.26/wyl-web/login");
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);

        HttpEntity en = response.getEntity();
        String res = EntityUtils.toString(en);
        // {"success":false,"message":"Bad credentials","status":0}
        // {"message":"登录成功","result":"***auth token code***","status":1,"success":true}
        JSONObject ret = JSONObject.parseObject(res);
        System.out.println("ret.toJSONString() = " + ret.toJSONString());
        if (ret.getBoolean("success")) {
            String token = ret.getString("result");
            System.out.println("token = " + token);
        }
        post.releaseConnection();
    }

    @Test
    public void send() throws IOException {
        StringEntity entity = new StringEntity("{'from':'stec-cloud', 'data': 'you data string or object'}", Charset.forName("UTF-8"));
        entity.setContentType("application/json");

        HttpPost post = new HttpPost("http://localhost:5026/demo-web/rest/receive");
        post.setHeader("x-auth-token", "b53ea720-d475-4fef-b6cc-d1a61d6947ff");
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        JSONObject ret = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

        System.out.println("ret.toJSONString() = " + ret.toJSONString());
        // {"success":false,"message":"您没有登录","status":1001}
        // {"result":"","success":true,"message":"接收成功！","status":1}
    }


}
