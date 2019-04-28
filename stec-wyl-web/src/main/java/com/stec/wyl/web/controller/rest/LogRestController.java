package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysLog;
import com.stec.masterdata.handler.auth.SysLogHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/18 0018
 * Time: 12:24
 */
@Api(value = "日志管理", tags = {"日志管理"})
@RestController
@RequestMapping(value = "/rest/log", method = RequestMethod.POST)
public class LogRestController extends BaseController {

    @Reference
    private SysLogHandler sysLogHandler;

    @ApiOperation("日志查询，分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();

        String name = jsonRequestBody.getString("name");
        Date startTime = jsonRequestBody.getDate("startTime");
        Date endTime = jsonRequestBody.getDate("endTime");
        String client = jsonRequestBody.getString("client");
        EntityWrapper<SysLog> wrapper = new EntityWrapper<>();

        if(!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if(ObjectUtils.allIsNotNull(startTime, endTime)) {
            wrapper.between("log_date", startTime, endTime);
        }
        if(StringUtils.isNotEmpty(client)) {
            wrapper.like("client", client);
        }

        wrapper.orderBy("log_date", false);
        return ResultForm.createSuccessResultForm(sysLogHandler.selectEntities(wrapper, pageForm), "查询成功！");
    }
}
