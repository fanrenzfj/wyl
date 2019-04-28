package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.metadata.bean.TreeNode;
import com.stec.masterdata.entity.auth.SysOrg;
import com.stec.masterdata.handler.auth.SysOrgHandler;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.bean.ImageInfoBean;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.wyl.web.service.DocumentService;
import com.stec.wyl.web.service.SessionUtils;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.handler.auth.AuthHandler;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/4 0004
 * Time: 16:52
 */
@Api(value = "用户管理controller", tags = {"用户管理"})
@RestController
@RequestMapping(value = "/rest/user", method = RequestMethod.POST)
public class UserRestController extends BaseController {

    @Reference
    private SysUserHandler sysUserHandler;

    @Reference
    private AuthHandler authHandler;

    @Reference
    private SysOrgHandler sysOrgHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SessionUtils sessionUtils;

    @ApiOperation("用户清单")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody JsonRequestBody jsonRequestBody){
        PageForm pageForm = jsonRequestBody.getPageForm();
        SysUser entity = jsonRequestBody.tryGet(SysUser.class);
        return ResultForm.createSuccessResultForm(authHandler.userList(entity, pageForm), "用户列表查询成功！");
    }

    @ApiOperation("修改密码")
    @RequestMapping("/updatePassword")
    public ResultForm updatePassword(@RequestBody @ApiParam(value = "{originPassword:,currentPassword:}") JsonRequestBody jsonRequestBody) {
        logger("修改密码", jsonRequestBody);
        SysUser loginUser = sessionUtils.getSyncCurrentUser();
        if(loginUser == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "您尚未登录！");
        }
        String originPassword = jsonRequestBody.getString("originPassword");
        String currentPassword = jsonRequestBody.getString("currentPassword");
        if(StringUtils.isAllBlank(originPassword, currentPassword)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "密码不能为空");
        }
        if(!passwordEncoder.matches(originPassword, loginUser.getPassword())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "原密码错误！");
        }
        loginUser.setPassword(passwordEncoder.encode(currentPassword));
        sysUserHandler.save(loginUser);

        return ResultForm.createSuccessResultForm(null, "密码修改成功！");
    }

    @ApiOperation("用户角色清单")
    @RequestMapping("/roles")
    public ResultForm roles(@RequestBody @ApiParam(value = "{userId:userId}") JsonRequestBody jsonRequestBody) {
        Long userId = jsonRequestBody.getLong("userId");
        if(userId == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "用户ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(authHandler.selectUserRoles(userId), "用户角色查询成功！");
    }

    @ApiOperation("用户拥有权限清单，包含从角色继承得到的权限")
    @RequestMapping("/privileges")
    public ResultForm privileges(@RequestBody @ApiParam(value = "{userId:userId}") JsonRequestBody jsonRequestBody) {
        Long userId = jsonRequestBody.getLong("userId");
        if(userId == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "用户ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(authHandler.selectUserPrivileges(userId), "用户权限查询成功！");
    }

    @ApiOperation("用户角色配置")
    @RequestMapping("/configRoles")
    public ResultForm configRoles(@RequestBody @ApiParam(value = "{userId:userId,roleIds:[...]}") JsonRequestBody jsonRequestBody) {
        logger("用户角色配置", jsonRequestBody);
        Long userId = jsonRequestBody.getLong("userId");
        if(userId == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "用户ID不能为空！");
        }
        List<Long> roleIds = jsonRequestBody.getList("roleIds", Long.class);
        return ResultForm.createSuccessResultForm(authHandler.saveUserRoles(userId, roleIds), "用户角色配置成功！");
    }

    @ApiOperation("用户权限配置，配合用户角色进行微调")
    @RequestMapping("/configPrivileges")
    public ResultForm configPrivileges(@RequestBody @ApiParam(value = "{userId:userId,privilegeIds:[...]}") JsonRequestBody jsonRequestBody) {
        logger("用户权限配置", jsonRequestBody);
        Long userId = jsonRequestBody.getLong("userId");
        if(userId == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "用户ID不能为空！");
        }
        List<Long> privilegeIds = jsonRequestBody.getList("privilegeIds", Long.class);
        return ResultForm.createSuccessResultForm(authHandler.saveUserPrivileges(userId, privilegeIds), "用户权限配置成功！");
    }

    @ApiOperation("用户状态修改")
    @RequestMapping("/enabled")
    public ResultForm enabled(@RequestBody @ApiParam(value = "{userId:userId,enabled:boolean}") JsonRequestBody jsonRequestBody) {
        logger("用户状态修改", jsonRequestBody);
        Long userId = jsonRequestBody.getLong("userId");
        Boolean enabled = jsonRequestBody.getBoolean("enabled");
        if(ObjectUtils.isNull(userId, enabled)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "用户ID和修改状态不能为空！");
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setEnabled(enabled);
        sysUserHandler.save(sysUser);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "用户状态修改成功！");
    }


    @ApiOperation("重置密码")
    @RequestMapping("/resetPassword")
    public ResultForm resetPassword(@RequestBody @ApiParam(value="{userId:userId,password:password}") JsonRequestBody jsonRequestBody) {
        logger("重置密码", jsonRequestBody);
        Long userId = jsonRequestBody.getLong("userId");
        if(userId == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "用户为空！");
        }
        String password = jsonRequestBody.getString("password");
        if(StringUtils.isEmpty(password)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "密码不能为空！");
        }
        SysUser user = sysUserHandler.selectByPrimaryKey(userId);
        if(user == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "无法获取用户！");
        }
        user.setPassword(passwordEncoder.encode(password));
        sysUserHandler.save(user);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "密码重置成功！");
    }

    @ApiOperation("上传头像")
    @RequestMapping(value = "/uploadPhoto")
    public ResultForm uploadImg(MultipartRequest multipartRequest){
        logger("上传头像");
        ResultForm<?> result;
        try {
            ImageInfoBean bean  = documentService.uploadImage(multipartRequest, "attach");
            String url = documentService.getFileUrl(bean);

            SysUser loginUser = sessionUtils.getSyncCurrentUser();
            loginUser.setPhoto(url);
            sysUserHandler.save(loginUser);
            result = ResultForm.createSuccessResultForm(url, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return result;
    }

    @ApiOperation("创建或更新用户信息")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody JsonRequestBody jsonRequestBody) {
        logger("创建或更新用户信息", jsonRequestBody);
        SysUser user = jsonRequestBody.tryGet(SysUser.class);

        if(StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        try{
            user = sysUserHandler.save(user);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(user, "添加用户成功!");
    }

    @ApiOperation("组织人员选择树形结构")
    @RequestMapping("/userTreeData")
    public ResultForm userTreeData(@RequestBody JsonRequestBody jsonRequestBody) {
        List<SysOrg> orgList = sysOrgHandler.selectEntities(new SysOrg());
        List<SysUser> userList = sysUserHandler.selectEntities(new SysUser());
        List<TreeNode> treeList = new ArrayList<>(orgList.size() + userList.size());
        for (SysOrg sysOrg : orgList) {
            treeList.add(new TreeNode<>(sysOrg.getId(), sysOrg.getName(), "org", false, null, sysOrg.getParentId()));
        }
        for (SysUser sysUser : userList) {
            if(ObjectUtils.isNotNull(sysUser.getOrgId())){
                treeList.add(new TreeNode<>(sysUser.getId(), sysUser.getName(), "user", true, null, sysUser.getOrgId()));
            }
        }
        return ResultForm.createSuccessResultForm(treeList, "组织人员数据查询成功！");
    }

    @ApiOperation("根据组织机构查成员")
    @RequestMapping("/orgUsers")
    public ResultForm orgUsers(@RequestBody @ApiParam(value = "{orgId:orgId}") JsonRequestBody jsonRequestBody) {
        Long orgId = jsonRequestBody.getLong("orgId");
        if(ObjectUtils.isNull(orgId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "组织机构不能为空！");
        }
        List<SysOrg> orgList = sysOrgHandler.selectTreeByParentId(orgId);
        List<Long> orgIds = new ArrayList<>(orgList.size());
        for (SysOrg sysOrg : orgList) {
            orgIds.add(sysOrg.getId());
        }
        EntityWrapper<SysUser> wrapper = new EntityWrapper<>();
        wrapper.in("org_id", orgIds);
        return ResultForm.createSuccessResultForm(sysUserHandler.selectEntities(wrapper), "组织人员查询成功！");
    }

    @ApiOperation("根据组织机构查子机构及成员")
    @RequestMapping("/orgTreeUsers")
    public ResultForm orgTreeUsers(@RequestBody @ApiParam(value = "{orgId:orgId}") JsonRequestBody jsonRequestBody) {
        Long orgId = jsonRequestBody.getLong("orgId");
        Map<String, Object> retMap = new HashMap<>(2);
        List<SysOrg> orgList;
        if(ObjectUtils.isNull(orgId)) {
            orgList = sysOrgHandler.selectRootList(new SysOrg());
        }
        else {
            SysOrg parent = new SysOrg();
            parent.setParentId(orgId);
            orgList = sysOrgHandler.selectChildList(parent);
            
            List<SysOrg> orgs = sysOrgHandler.selectTreeByParentId(orgId);
            List<Long> orgIds = new ArrayList<>(orgs.size());
            for (SysOrg sysOrg : orgs) {
                orgIds.add(sysOrg.getId());
            }
            EntityWrapper<SysUser> wrapper = new EntityWrapper<>();
            wrapper.in("org_id", orgIds);
            retMap.put("userList", sysUserHandler.selectEntities(wrapper));
        }
        retMap.put("orgList", orgList);

        return ResultForm.createSuccessResultForm(retMap, "组织人员查询成功！");
    }
}
