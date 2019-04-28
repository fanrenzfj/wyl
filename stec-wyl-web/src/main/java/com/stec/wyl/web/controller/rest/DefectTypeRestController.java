package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.wyl.DefectLevel;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.DefectTypeUsage;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.protocol.DeviceTypeHandler;
import com.stec.masterdata.handler.wyl.DefectLevelHandler;
import com.stec.masterdata.handler.wyl.DefectTypeHandler;
import com.stec.masterdata.handler.wyl.DefectTypeUsageHandler;
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

/**
 * 描述：缺陷类型管理
 *
 * @author Li.peng
 * @create 2018-08-27 13:35
 */
@Api(value = "缺陷类型管理controller", tags = {"缺陷类型管理"})
@RestController
@RequestMapping(value = "/rest/defectType", method = RequestMethod.POST)
public class DefectTypeRestController extends BaseController {
    @Reference
    private DefectTypeHandler defectTypeHandler;

    @Reference
    private DefectLevelHandler defectLevelHandler;

    @Reference
    private StructureHandler structureHandler;

    @Reference
    private DeviceTypeHandler deviceTypeHandler;

    @Reference
    private DefectTypeUsageHandler defectTypeUsageHandler;

    @ApiOperation("缺陷类型新增/修改")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{type:type,name:name,code:code,usages:[code;code,name;name]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("缺陷类型新增/修改", jsonRequestBody);
        DefectType defectType = jsonRequestBody.tryGet(DefectType.class);
        if (!ObjectUtils.allIsNotNull(defectType.getType(), defectType.getName(), defectType.getCode())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, " 类别、设施类别名称、缺陷类型名称、缺陷类型编号不能为空！");
        }
        defectType = defectTypeHandler.save(defectType, jsonRequestBody.getList("usages", DefectTypeUsage.class));
        return ResultForm.createSuccessResultForm(defectType, "新增/修改缺陷类型成功");
    }

    @ApiOperation(value = "缺陷类型列表,分页查询")
    @RequestMapping("/list")
    public ResultForm list(@ApiParam(value = "{type:type,name:name,[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        DefectType entity = jsonRequestBody.tryGet(DefectType.class);
        EntityWrapper<DefectType> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            wrapper.eq("type", entity.getType());
        }
        DataPaging<DefectType> dataPaging = defectTypeHandler.selectEntities(wrapper, pageForm);
        List<DefectType> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (DefectType defectType : list) {
            Map<String, Object> defectTypeMap;
            try {
                defectTypeMap = MapUtils.toMapIgnoreNull(defectType);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(defectTypeMap);
            defectTypeMap.put("defectLevel", defectLevelHandler.selectEntities(MapUtils.newHashMap("defectTypeId", defectType.getId())));

            defectTypeMap.put("usages", defectTypeUsageHandler.selectEntities(MapUtils.newHashMap("defectTypeId", defectType.getId())));
        }
        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "缺陷类型列表查询成功！");
    }

    @ApiOperation(value = "获取缺陷类型")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(value = "{id:id  缺陷类型id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "缺陷类型id不能为空！");
        }
        DefectType defectType = defectTypeHandler.selectByPrimaryKey(id);
        Map<String, Object> defectTypeMap;
        try {
            defectTypeMap = MapUtils.toMapIgnoreNull(defectType);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        if (ObjectUtils.isNotNull(defectType)) {
            List<DefectLevel> defectLevelList = defectLevelHandler.selectEntities(MapUtils.newHashMap("defectTypeId", defectType.getId()));
            defectTypeMap.put("defectLevel", defectLevelList);
            defectTypeMap.put("usages", defectTypeUsageHandler.selectEntities(MapUtils.newHashMap("defectTypeId", defectType.getId())));
            return ResultForm.createSuccessResultForm(defectTypeMap, "获取缺陷类型成功！");
        } else {
            return ResultForm.createErrorResultForm(jsonRequestBody, "没有缺陷类型！");
        }
    }

    @ApiOperation(value = "删除缺陷类型")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 缺陷类型id}") JsonRequestBody jsonRequestBody) {
        logger("删除缺陷类型", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "缺陷类型id不能为空！");
        }
        return ResultForm.createSuccessResultForm(defectTypeHandler.deleteEntity(id), "缺陷类型删除成功！");
    }

    @ApiOperation(value = "结构ID&&设备ID查询缺陷类型")
    @RequestMapping("/findByDeviceOrStructureId")
    public ResultForm findByDeviceOrStructureId(@RequestBody @ApiParam(value = "{id:id ,type:type device||structure}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        String type = jsonRequestBody.getString("type");
        if (!ObjectUtils.allIsNotNull(id, type)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备或者结构ID、缺陷类别不能为空！");
        }
        if (type.equals(DefectType.DEVICE_TYPE_DEVICE)) {
            return ResultForm.createSuccessResultForm(defectTypeHandler.findByDeviceId(id), "设备的缺陷类型查询成功！");
        } else if (type.equals(DefectType.DEVICE_TYPE_STRUCTURE)) {
            return ResultForm.createSuccessResultForm(defectTypeHandler.findByStructureId(id), "结构的缺陷类型查询成功！");
        } else {
            return ResultForm.createErrorResultForm(jsonRequestBody, "缺陷类型查询失败，没有该缺陷类别！");
        }
    }
}
