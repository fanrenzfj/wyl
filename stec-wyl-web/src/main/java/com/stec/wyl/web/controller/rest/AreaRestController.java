package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.wyl.web.controller.BaseController;
import com.stec.masterdata.entity.basic.SysArea;
import com.stec.masterdata.handler.basic.SysAreaHandler;
import com.stec.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author liweigao
 * Date: 2018-07-23
 * Time: 9:55
 */
@Api(value = "行政区划controller", tags = "行政区划管理")
@RestController
@RequestMapping(value = "/rest/area", method = RequestMethod.POST)
public class AreaRestController extends BaseController {

    @Reference
    private SysAreaHandler sysAreaHandler;

    @ApiOperation("获取根节点")
    @RequestMapping(value = "/rootList")
    public ResultForm rootList() {
        return ResultForm.createSuccessResultForm(sysAreaHandler.selectRootList(new SysArea()), "获取根节点成功！");
    }

    @ApiOperation("获取子节点")
    @RequestMapping(value = "/getChildren")
    public ResultForm getChildren(@RequestBody @ApiParam(name = "获取节点", value = "{id:id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "节点id为空！");
        }
        return ResultForm.createSuccessResultForm(sysAreaHandler.selectListByParentId(id), "子节点获取成功！");
    }
}
