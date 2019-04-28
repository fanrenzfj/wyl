package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.wyl.web.controller.BaseController;
import com.stec.masterdata.entity.auth.SysOrg;
import com.stec.masterdata.handler.auth.AuthHandler;
import com.stec.masterdata.handler.auth.SysOrgHandler;
import com.stec.masterdata.handler.auth.SysRoleHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
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
 * Date: 2018/7/13 0013
 * Time: 10:59
 */
@Api(value = "组织管理controller", tags = {"组织管理及组织权限相关配置"})
@RestController
@RequestMapping(value = "/rest/org", method = RequestMethod.POST)
public class OrgRestController extends BaseController {

    @Reference
    private AuthHandler authHandler;

    @Reference
    private SysOrgHandler sysOrgHandler;

    @Reference
    private SysRoleHandler sysRoleHandler;

    @ApiOperation("根组织清单")
    @RequestMapping("/rootList")
    public ResultForm rootList(){
        return ResultForm.createSuccessResultForm(sysOrgHandler.selectRootList(new SysOrg()), "获取组织列表成功！");
    }

    @ApiOperation("组织结构树")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(name = "组织ID,选填", value = "{id:id}")JsonRequestBody jsonRequestBody) {
        return ResultForm.createSuccessResultForm(sysOrgHandler.selectTreeByParentId(jsonRequestBody.getLong("id")), "查询成功");
    }

    @ApiOperation("保存或修改组织信息")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody JsonRequestBody jsonRequestBody){
        logger("保存或修改组织信息", jsonRequestBody);
        SysOrg entity = jsonRequestBody.tryGet(SysOrg.class);
        if(!StringUtils.isAllNotBlank(entity.getName(), entity.getCode(), entity.getType())) {
            return ResultForm.createErrorResultForm(entity, "组织名称、编码、类型均不能为空！");
        }
        return ResultForm.createSuccessResultForm(sysOrgHandler.save(entity), "保存成功！");
    }

    @ApiOperation("获取组织权限清单")
    @RequestMapping("/privileges")
    public ResultForm privileges(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(authHandler.selectOrgPrivileges(id), "组织权限查询成功！");
    }


    @ApiOperation("获取组织角色清单")
    @RequestMapping("/roles")
    public ResultForm roles(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(authHandler.selectOrgRoles(id), "组织权限查询成功！");
    }

    @ApiOperation("组织权限配置")
    @RequestMapping("/configPrivileges")
    public ResultForm configPrivileges(@RequestBody @ApiParam(value = "{orgId:orgId,privilegeIds:[...]}") JsonRequestBody jsonRequestBody) {
        logger("组织权限配置", jsonRequestBody);
        Long orgId = jsonRequestBody.getLong("orgId");
        List<Long> privilegeIds = jsonRequestBody.getList("privilegeIds", Long.class);
        if(ObjectUtils.isNull(orgId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织不能为空！");
        }
        authHandler.saveOrgPrivileges(orgId, privilegeIds);
        return ResultForm.createSuccessResultForm(null, "组织配置权限成功！");
    }

    @ApiOperation("删除组织")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody) {
        logger("删除组织", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织ID为空！");
        }
        sysOrgHandler.deleteByPrimaryKey(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "组织删除成功！");
    }
    
    @ApiOperation("获取组织")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(value = "{id:id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织ID为空！");
        }
        return ResultForm.createSuccessResultForm(sysOrgHandler.selectByPrimaryKey(id), "组织获取成功！");
    }

}
