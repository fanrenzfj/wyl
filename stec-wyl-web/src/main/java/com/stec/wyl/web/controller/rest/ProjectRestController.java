package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.bean.TreeNode;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.protocol.DeviceType;
import com.stec.masterdata.handler.protocol.DeviceTypeHandler;
import com.stec.utils.*;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import com.stec.wyl.web.service.SessionUtils;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.project.Channel;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.project.Project;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.handler.project.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Date: 2018/7/26 0026
 * Time: 11:59
 */
@Api(value = "项目管理controller", tags = {"项目及项目结构管理"})
@RestController
@RequestMapping(value = "/rest/project", method = RequestMethod.POST)
public class ProjectRestController extends BaseController {

    @Autowired
    private SessionUtils sessionUtils;

    @Reference
    private ProjectDiyHandler projectDiyHandler;

    @Reference
    private ProjectHandler projectHandler;

    @Reference
    private StructureHandler structureHandler;

    @Reference
    private DeviceTypeHandler deviceTypeHandler;

    @Reference
    private DeviceHandler deviceHandler;

    @Reference
    private ChannelHandler channelHandler;

    @Autowired
    private DocumentService documentService;

    @ApiOperation("项目列表，分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(value = "{code:code,name:name,[pageForm properties]}") JsonRequestBody jsonRequestBody) {
        Project entity = jsonRequestBody.tryGet(Project.class);
        PageForm pageForm = jsonRequestBody.getPageForm();
        if(StringUtils.isBlank(entity.getName())){
            entity.setName(null);
        }
        return ResultForm.createSuccessResultForm(projectDiyHandler.projectList(entity, pageForm), "项目查询成功！");
    }

    @ApiOperation("保存项目")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody @ApiParam(value = "Project.java object, name,orgId not null") JsonRequestBody jsonRequestBody) {
        logger("保存项目", jsonRequestBody);
        Project entity = jsonRequestBody.tryGet(Project.class);
        if(!ObjectUtils.allIsNotNull(entity.getName(), entity.getOrgId())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "项目名称和所属组织不能为空！");
        }
        if(ObjectUtils.isNull(entity.getId())) {
            SysUser currUser = sessionUtils.getCurrentUser();
            if(ObjectUtils.isNotNull(currUser) && currUser.getId() != null) {
                entity.setCreateUserId(currUser.getId());
                entity.setCreateDate(TimeUtil.now());
            }
            else {
                return ResultForm.createErrorResultForm(jsonRequestBody, "当前用户未登陆！");
            }
            entity.setCode(SequenceUtils.getSecondUID("PM"));
        }
        return ResultForm.createSuccessResultForm(projectHandler.save(entity), "保存项目成功！");
    }

    @ApiOperation("删除项目")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id*}") JsonRequestBody jsonRequestBody) {
        logger("删除项目", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "项目ID不能为空！");
        }
        if(projectHandler.deleteByPrimaryKey(id)) {
            return ResultForm.createSuccessResultForm(jsonRequestBody, "项目删除成功！");
        }
        return ResultForm.createErrorResultForm(jsonRequestBody, "项目删除失败！");
    }

    @ApiOperation("项目结构树数据，异步树加载")
    @RequestMapping("/structureTreeData")
    public ResultForm structureTreeData(@RequestBody @ApiParam(value = "{id:id*,type:type*}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        String type = jsonRequestBody.getString("type");

        if(!ObjectUtils.allIsNotNull(id, type)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "节点ID和类型type都不能为空！");
        }
        Structure param = new Structure();
        List<TreeNode> treeList = new ArrayList<>();
        List<Structure> list = new ArrayList<>();
        if(StringUtils.equals("project", type)) {
            param.setProjectId(id);
            list = structureHandler.selectRootList(param);
        }
        else if(StringUtils.equals("structure", type)) {
            param.setParentId(id);
            list = structureHandler.selectEntities(param);
        }
        for (Structure node : list) {
            treeList.add(new TreeNode<>(node.getId(), node.getName(), "structure", node.getLeaf(), node));
        }

        return ResultForm.createSuccessResultForm(treeList, "节点加载成功！");
    }

    @ApiOperation("保存项目结构信息")
    @RequestMapping("/saveStructure")
    public ResultForm saveStructure(@RequestBody @ApiParam(value = "{projectId:projectId*,parentId:parentId,name:name*}") JsonRequestBody jsonRequestBody) {
        logger("保存项目结构信息", jsonRequestBody);
        Structure entity = jsonRequestBody.tryGet(Structure.class);
        if(ObjectUtils.isNull(entity.getName()) || ObjectUtils.allIsNull(entity.getProjectId(), entity.getParentId())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "项目ID或者父ID、结构名称不能为空！");
        }
        if(ObjectUtils.isNull(entity.getProjectId())) {
            Structure parent = structureHandler.selectByPrimaryKey(entity.getParentId());
            entity.setParentId(parent.getProjectId());
        }
        if(ObjectUtils.isNull(entity.getId())) {
            entity.setCode(SequenceUtils.getSecondUID("PS"));
        }
        return ResultForm.createSuccessResultForm(structureHandler.save(entity), "项目结构保存成功！");
    }

    @ApiOperation("项目结构列表")
    @RequestMapping("/structureList")
    public ResultForm structureList(@RequestBody @ApiParam(value = "{projectId:projectId}") JsonRequestBody jsonRequestBody) {
        Long projectId = jsonRequestBody.getLong("projectId");
        if(ObjectUtils.isNull(projectId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "项目ID不能为空！");
        }
        Structure entity = new Structure();
        entity.setProjectId(projectId);
        return ResultForm.createSuccessResultForm(structureHandler.selectEntities(entity), "项目结构列表获取成功！");
    }

    @ApiOperation("删除项目结构")
    @RequestMapping("/deleteStructure")
    public ResultForm deleteStructure(@RequestBody @ApiParam(value = "{id:id*}") JsonRequestBody jsonRequestBody) {
        logger("删除项目结构", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "结构ID不能为空！");
        }
        if(structureHandler.deleteByPrimaryKey(id)) {
            return ResultForm.createSuccessResultForm(jsonRequestBody, "项目结构删除成功！");
        }
        return ResultForm.createErrorResultForm(jsonRequestBody, "项目结构删除失败！");
    }

    @ApiOperation("获取结构信息")
    @RequestMapping("/getStructure")
    public ResultForm getStructure(@RequestBody @ApiParam(value = "{id:id*}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "结构ID不能为空！");
        }
        return ResultForm.createSuccessResultForm(structureHandler.selectByPrimaryKey(id), "获取结构信息成功！");
    }

    @ApiOperation("通过CODE获取结构信息")
    @RequestMapping("/getStructureWithCode")
    public ResultForm getStructureWithCode(@RequestBody @ApiParam(value = "{code:code*}") JsonRequestBody jsonRequestBody) {
        String code = jsonRequestBody.getString("code");
        if(StringUtils.isEmpty(code)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "结构编码不能为空！");
        }
        return ResultForm.createSuccessResultForm(structureHandler.selectEntity(MapUtils.newHashMap("code", code)), "获取结构信息成功！");
    }

    @ApiOperation("获取结构以及结构下的设备信息")
    @RequestMapping("/getStructureInfo")
    public ResultForm getStructureInfo(@RequestBody @ApiParam(value = "{code:structureCode*}") JsonRequestBody jsonRequestBody) {
        String code = jsonRequestBody.getString("code");
        if(StringUtils.isEmpty(code)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "结构编码不能为空！");
        }
        Map<String, Object> retMap = new HashMap<>();
        Structure structure = structureHandler.selectEntity(MapUtils.newHashMap("code", code));
        retMap.put("structure", structure);

        List<Structure> structures = structureHandler.selectTreeByParentId(structure.getId());
        Map<Long, List<Device>> typeMap = new HashMap<>();
        for (Structure st : structures) {
            List<Device> devices = deviceHandler.selectEntities(MapUtils.newHashMap("structureId", st.getId()));
            for (Device device : devices) {
                List<Device> list = typeMap.get(device.getDeviceTypeId());
                if(ObjectUtils.isNull(list)) {
                    list = new ArrayList<>();
                    typeMap.put(device.getDeviceTypeId(), list);
                }
                list.add(device);
            }
        }

        List<Map<String, Object>> retList = new ArrayList<>(typeMap.size());
        for (Map.Entry<Long, List<Device>> entry : typeMap.entrySet()) {
            Long deviceTypeId = entry.getKey();
            DeviceType deviceType = deviceTypeHandler.selectByPrimaryKey(deviceTypeId);
            Map<String, Object> map = new HashMap<>(2);
            map.put("devcieType", deviceType);
            map.put("devices", entry.getValue());
            retList.add(map);
        }
        retMap.put("list", retList);
        return ResultForm.createSuccessResultForm(retMap, "获取结构信息成功！");
    }

    @ApiOperation("项目异步结构树数据，异步树加载")
    @RequestMapping("/getAsynTreeStructure")
    public ResultForm getAsynTreeStructure(@RequestBody @ApiParam(value = "{id:id*,type:type*}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        String type = jsonRequestBody.getString("type");
        List<TreeNode> treeList = new ArrayList<>();
        List<Project> listProject=null;
        if(StringUtils.equals("root",type)){
            EntityWrapper<Project> wrapper = new EntityWrapper<>();
            listProject=  projectHandler.selectEntities(wrapper);
            for(Project node : listProject){
                treeList.add(new TreeNode(node.getId(), node.getName(), "project", false));
            }
        }else if(StringUtils.equals("project", type)) {
            EntityWrapper<Structure> wrapper = new EntityWrapper<>();
            wrapper.eq("project_id", id);
            wrapper.isNull("parent_id");
            List<Structure> structures = structureHandler.selectEntities(wrapper);
            for (Structure structure : structures) {
                if(structure.getLeaf()) {
                    Device device = new Device();
                    device.setStructureId(structure.getId());
                    Long count = deviceHandler.count(device);
                    if(count > 0) {
                        structure.setLeaf(false);
                    }
                }
                treeList.add(new TreeNode(structure.getId(), structure.getName(), "structure", structure.getLeaf(), structure));
            }

            EntityWrapper<Device> deviceWrapper = new EntityWrapper<>();
            deviceWrapper.eq("project_id", id);
            deviceWrapper.isNull("structure_id");
            List<Device> devices = deviceHandler.selectEntities(deviceWrapper);

            for(Device device : devices){
                Channel param = new Channel();
                param.setDeviceId(device.getId());
                Long count= channelHandler.count(param);
                if(count>0){
                    treeList.add(new TreeNode(device.getId(), device.getName(), "device", false));
                }else{
                    treeList.add(new TreeNode(device.getId(), device.getName(), "device", true));
                }

            }
        }else if(StringUtils.equals("structure",type)){

            EntityWrapper<Structure> wrapper = new EntityWrapper<>();
            wrapper.eq("parent_id",id);
            List<Structure> structures = structureHandler.selectEntities(wrapper);
            for(Structure s : structures){
                if(s.getLeaf()){
                    Device device = new Device();
                    device.setStructureId(s.getId());
                    Long count = deviceHandler.count(device);
                    if(count > 0) {
                        s.setLeaf(false);
                    }
                }
                treeList.add(new TreeNode(s.getId(), s.getName(), "structure", s.getLeaf(), s));
            }
            Device device = new Device();
            device.setStructureId(id);
            List<Device> devices = deviceHandler.selectEntities(device);
            for(Device d : devices){
                Channel param = new Channel();
                param.setDeviceId(d.getId());
                Long count= channelHandler.count(param);
                if(count>0){
                    treeList.add(new TreeNode(d.getId(), d.getName(), "device", false));
                }else{
                    treeList.add(new TreeNode(d.getId(), d.getName(), "device", true));
                }

            }

        }else if(ObjectUtils.equals("device",type)){
            Channel param = new Channel();
            param.setDeviceId(id);
            List<Channel> channels =  channelHandler.selectEntities(param);
            for (Channel channel : channels) {
                treeList.add(new TreeNode(channel.getId(), channel.getName(), "channel", true, channel));
            }
        }
        return ResultForm.createSuccessResultForm(treeList, "节点加载成功！");
    }

    @ApiOperation("项目结构的文档新增/修改")
    @RequestMapping("/saveStructureDoc")
    public ResultForm saveStructureDoc(MultipartRequest multipartRequest, Long[] delIds, Long docId,Long structureId) {
        logger("项目结构的文档新增/修改", JSONObject.toJSONString("docId:"+docId+" "+"structureId:"+structureId));
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", docId, delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        Structure structure=new Structure();
        structure.setId(structureId);
        structure.setDocId(docInfo.getId());
        structure = structureHandler.save(structure);
        return ResultForm.createSuccessResultForm(structure, "文档保存成功！");
    }

    @ApiOperation("项目结构的图片新增/修改")
    @RequestMapping("/saveStructureImageDoc")
    public ResultForm saveStructureImageDoc(MultipartRequest multipartRequest, Long[] delIds, Long imageDocId,Long structureId) {
        logger("项目结构的图片新增/修改", JSONObject.toJSONString("imageDocId:"+imageDocId+" "+"structureId:"+structureId));
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", imageDocId, delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        Structure structure=new Structure();
        structure.setId(structureId);
        structure.setImageDocId(docInfo.getId());
        structure = structureHandler.save(structure);
        return ResultForm.createSuccessResultForm(structure, "文档保存成功！");
    }

    @ApiOperation("配置设施结构视口")
    @RequestMapping("/configStructureView")
    public ResultForm configStructureView(@ApiParam(value = "{id,viewScript}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        String viewScript = jsonRequestBody.getString("viewScript");
        if(!ObjectUtils.allIsNotNull(id, viewScript)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "结构ID与脚本信息均不能为空");
        }
        Structure entity = structureHandler.selectByPrimaryKey(id);
        entity.setModelViewScript(viewScript);
        structureHandler.save(entity);

        return ResultForm.createSuccessResultForm(jsonRequestBody, "设施视口设置成功！");
    }

}
