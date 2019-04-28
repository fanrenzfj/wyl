package com.stec.wyl.web.utils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.handler.basic.SysDicHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ExcelResponse {
    @Reference
    private SysDicHandler sysDicHandler;

    //封装响应流
    public static void responseHeader(HttpServletResponse response, String name) throws UnsupportedEncodingException {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(d);
        String fileName = name + date;
        String encodeName = java.net.URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Access-control-Expose-Headers", "attachment");
        response.setHeader("attachment", encodeName + ".xlsx");
    }


}
