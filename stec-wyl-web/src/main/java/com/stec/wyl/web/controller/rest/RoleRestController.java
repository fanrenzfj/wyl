package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysRole;
import com.stec.masterdata.handler.auth.AuthHandler;
import com.stec.masterdata.handler.auth.SysRoleHandler;
import com.stec.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/16 0016
 * Time: 11:04
 */
@Api(value = "角色管理controller", tags = {"角色管理"})
@RestController
@RequestMapping(value = "/rest/role", method = RequestMethod.POST)
public class RoleRestController extends BaseController {

    @Reference
    private AuthHandler authHandler;

    @Reference
    private SysRoleHandler sysRoleHandler;

    @ApiOperation("角色列表，分页显示")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody){
        PageForm pageForm = jsonRequestBody.getPageForm();
        SysRole sysRole = jsonRequestBody.tryGet(SysRole.class);
        return ResultForm.createSuccessResultForm(authHandler.roleList(sysRole, pageForm), "角色查询成功！");
    }

    @ApiOperation("保存角色")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody @ApiParam(value = "{id:id,orgId:orgId,name:name,code:code}") JsonRequestBody jsonRequestBody) {
        logger("保存角色", jsonRequestBody);
        SysRole entity = jsonRequestBody.tryGet(SysRole.class);
        if(ObjectUtils.isNull(entity.getOrgId(), entity.getCode(), entity.getName())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织ID，角色编码，角色名称均不能为空！");
        }
        return ResultForm.createSuccessResultForm(sysRoleHandler.save(entity), "角色保存成功！");
    }

    @ApiOperation("角色权限清单")
    @RequestMapping("/privileges")
    public ResultForm privileges(@RequestBody @ApiParam(value = "{roleId:roleId}") JsonRequestBody jsonRequestBody) {
        Long roleId = jsonRequestBody.getLong("roleId");
        if(ObjectUtils.isNull(roleId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "角色ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(authHandler.selectRolePrivileges(roleId), "角色权限查询成功！");
    }

    @ApiOperation("角色权限配置")
    @RequestMapping("/config")
    public ResultForm config(@RequestBody @ApiParam(value = "{roleId:roleId,privilegeIds:[...]}") JsonRequestBody jsonRequestBody) {
        logger("角色权限配置", jsonRequestBody);
        Long roleId = jsonRequestBody.getLong("roleId");
        if(ObjectUtils.isNull(roleId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "角色ID不能为空！");
        }
        List<Long> privilegeIds = jsonRequestBody.getList("privilegeIds", Long.class);
        return ResultForm.createSuccessResultForm(authHandler.saveRolePrivileges(roleId, privilegeIds), "角色权限配置成功！");
    }

    @ApiOperation("角色删除")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody) {
        logger("角色删除", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "角色ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(sysRoleHandler.deleteByPrimaryKey(id), "角色删除成功！");
    }

}
