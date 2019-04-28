package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.masterdata.entity.auth.SysPrivilege;
import com.stec.masterdata.handler.auth.SysPrivilegeHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
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
 * @author joe.xie
 * Date: 2018/7/12 0012
 * Time: 13:47
 */
@Api(value = "系统权限controller", tags = {"权限管理"})
@RestController
@RequestMapping(value = "/rest/privilege", method = RequestMethod.POST)
public class PrivilegeRestController extends BaseController {

    @Reference
    private SysPrivilegeHandler sysPrivilegeHandler;

    @ApiOperation("查询系统所有权限，无需参数")
    @RequestMapping("/list")
    public ResultForm list(){
        return ResultForm.createSuccessResultForm(sysPrivilegeHandler.selectEntities(new SysPrivilege()), "权限列表查询成功！");
    }

    @ApiOperation("根据ID获取单个权限")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(name = "权限ID", value = "{id:id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(null, "ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(sysPrivilegeHandler.selectByPrimaryKey(id), "获取权限成功！");
    }

    @ApiOperation("保存或修改权限")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody @ApiParam(name = "权限对象", value="{id:id,name:name,code:code,\nparentId:parentId,type:type,definition:definition,\ndescription:description}")
                                       JsonRequestBody jsonRequestBody){
        logger("保存或修改权限", jsonRequestBody);
        SysPrivilege entity = jsonRequestBody.tryGet(SysPrivilege.class);
        if(StringUtils.isAllBlank(entity.getName(), entity.getCode())) {
            return ResultForm.createErrorResultForm(entity, "权限名称和编码不能为空！");
        }
        return ResultForm.createSuccessResultForm(sysPrivilegeHandler.save(entity), "权限保存成功！");
    }

    @ApiOperation("删除权限")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(name = "权限ID", value = "{id:id}") JsonRequestBody jsonRequestBody){
        logger("删除权限", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(null, "错误的删除主键！");
        }
        return ResultForm.createSuccessResultForm(sysPrivilegeHandler.deleteByPrimaryKey(id), "权限删除成功！");
    }
}
