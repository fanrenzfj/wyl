package com.stec.wyl.web.controller.rest;
/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 21:01
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.wyl.Material;
import com.stec.masterdata.handler.wyl.MaterialHandler;

import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;


@Api(value = "物资管理controller", tags = {"物资管理"})
@RestController
@RequestMapping(value = "/rest/material", method = RequestMethod.POST)
public class MaterialRestController extends BaseController {
    @Reference
    private MaterialHandler materialHandler;

    @Autowired
    private DocumentService documentService;

    @ApiOperation("物资信息新增/修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] delIds, Material entity) {
        logger("物资信息新增/修改", JSONObject.toJSONString(entity));
        if(!ObjectUtils.allIsNotNull(entity.getName(),entity.getMainType(),entity.getStore(),entity.getAmount(),entity.getUnit())){
            return ResultForm.createErrorResultForm(entity,"物资名称、物资类别、物资仓库、库存数量、物资单位不能为空");
        }
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", entity.getDocId(), delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        entity.setDocId(docInfo.getId());
        materialHandler.save(entity);
        if(ObjectUtils.isNull(entity.getPrice())){
            materialHandler.removePrice(entity.getId());
        }
        return ResultForm.createSuccessResultForm(entity, "保存成功！");
    }

    @ApiOperation("物资分页列表查询")
    @RequestMapping(value = "/list")
    public ResultForm list(@ApiParam(value = "{name:name,mainType:mainType,[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        Material entity = jsonRequestBody.tryGet(Material.class);
        EntityWrapper<Material> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        if (StringUtils.isNotEmpty(entity.getMainType())) {
            wrapper.eq("main_type", entity.getMainType());
        }
        return ResultForm.createSuccessResultForm(materialHandler.selectEntities(wrapper, pageForm), "物资列表查询成功！");
    }

    @ApiOperation("获取物资")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(value = "{id:id 物资id}") JsonRequestBody jsonRequestBody){
        Long id=jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"物资ID不能为空");
        }
        return ResultForm.createSuccessResultForm(materialHandler.selectByPrimaryKey(id),"物资信息获取成功");
    }

    @ApiOperation("删除物资")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 物资id}") JsonRequestBody jsonRequestBody){
        logger("删除物资",jsonRequestBody);
        Long id=jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"物资id不能为空");
        }
        return ResultForm.createSuccessResultForm(materialHandler.deleteEntity(id));
    }

}
