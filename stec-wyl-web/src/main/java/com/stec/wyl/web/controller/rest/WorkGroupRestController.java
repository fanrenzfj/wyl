package com.stec.wyl.web.controller.rest;
/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/20
 * Time: 19:48
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysOrg;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.wyl.WorkGroup;
import com.stec.masterdata.entity.wyl.WorkGroupMember;
import com.stec.masterdata.handler.auth.SysOrgHandler;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.wyl.WorkGroupHandler;
import com.stec.masterdata.handler.wyl.WorkGroupMemberHandler;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "班组管理controller", tags = {"班组管理"})
@RestController
@RequestMapping(value = "/rest/workGroup", method = RequestMethod.POST)
public class WorkGroupRestController extends BaseController {
    @Reference
    private WorkGroupHandler workGroupHandler;
    @Reference
    private WorkGroupMemberHandler workGroupMemberHandler;
    @Reference
    private SysUserHandler sysUserHandler;
    @Reference
    private SysOrgHandler sysOrgHandler;

    @ApiOperation("班组信息新增/修改")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{name:name,type:type,orgId:orgId,leaderId:leaderId,mobile:mobile,projectId:projectId,leaderName:leaderName,workGroupMember:[...]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("班组信息新增/修改", jsonRequestBody);
        WorkGroup entity = jsonRequestBody.tryGet(WorkGroup.class);
        List<Long> userIds=null;
        String workGroupMember = "workGroupMember";
        if(jsonRequestBody.containsKey(workGroupMember))
        {
            userIds=jsonRequestBody.getList("workGroupMember",Long.class);
        }
        if(!ObjectUtils.allIsNotNull(entity.getName(), entity.getType(), entity.getOrgId(), entity.getLeaderId(),entity.getLeaderName())){
            return ResultForm.createErrorResultForm(jsonRequestBody, " 班组名称、班组类别、所属部门、班组负责人、手机号码、项目名称、班组成员不能为空！");
        }
        return ResultForm.createSuccessResultForm(workGroupHandler.save(entity,userIds), "新增班组成功！");
    }

    @ApiOperation(value = "班组列表,分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(value = "{name:name,[pageForm properties]}") JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();

        WorkGroup entity = jsonRequestBody.tryGet(WorkGroup.class);
        EntityWrapper<WorkGroup> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        DataPaging<WorkGroup> dataPaging = workGroupHandler.selectEntities(wrapper, pageForm);
        List<WorkGroup> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (WorkGroup workGroup : list) {
            SysUser sysUser=sysUserHandler.selectByPrimaryKey(workGroup.getLeaderId());
            SysOrg sysOrg=sysOrgHandler.selectByPrimaryKey(workGroup.getOrgId());
            workGroup.setOrgName(sysOrg.getName());
            workGroup.setLeaderName(sysUser.getName());
            Map<String, Object> group;
            try {
                group = MapUtils.toMapIgnoreNull(workGroup);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(group);
            List<WorkGroupMember> workGroupMemberList = workGroupMemberHandler.selectEntities(MapUtils.newHashMap("groupId", workGroup.getId()));
            List<Long> userIds = new ArrayList<>(workGroupMemberList.size());
            for (WorkGroupMember workGroupMember : workGroupMemberList) {
                userIds.add(workGroupMember.getUserId());
            }
            List<SysUser> members = sysUserHandler.selectBatchByIds(userIds);
            group.put("workGroupMember", members);
        }

        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "班组列表查询成功！");
    }

    @ApiOperation(value = "获取班组")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(value = "{id:id  班组id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"班组id不能为空！");
        }
        WorkGroup workGroup =workGroupHandler.selectByPrimaryKey(id);
        SysUser sysUser=sysUserHandler.selectByPrimaryKey(workGroup.getLeaderId());
        SysOrg sysOrg=sysOrgHandler.selectByPrimaryKey(workGroup.getOrgId());
        workGroup.setOrgName(sysOrg.getName());
        workGroup.setLeaderName(sysUser.getName());
        Map<String, Object> group;
        try {
            group = MapUtils.toMapIgnoreNull(workGroup);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        if(ObjectUtils.isNotNull(workGroup)){
            List<WorkGroupMember> workGroupMemberList = workGroupMemberHandler.selectEntities(MapUtils.newHashMap("groupId", workGroup.getId()));
            List<Long> userIds = new ArrayList<>(workGroupMemberList.size());
            for (WorkGroupMember workGroupMember : workGroupMemberList) {
                userIds.add(workGroupMember.getUserId());
            }
            List<SysUser> members = sysUserHandler.selectBatchByIds(userIds);
            group.put("workGroupMember", members);
            return ResultForm.createSuccessResultForm(group,"获取班组成功！");
        }else{
            return ResultForm.createErrorResultForm(jsonRequestBody,"没有班组！");
        }
    }

    @ApiOperation(value = "删除班组")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 班组id}") JsonRequestBody jsonRequestBody) {
        logger("删除班组", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "班组id不能为空！");
        }
        return ResultForm.createSuccessResultForm(workGroupHandler.deleteEntity(id), "班组删除成功！");
    }
}
