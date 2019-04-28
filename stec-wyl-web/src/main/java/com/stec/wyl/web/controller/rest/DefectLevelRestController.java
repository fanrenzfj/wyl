package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.wyl.DefectLevel;
import com.stec.masterdata.handler.wyl.DefectLevelHandler;
import com.stec.utils.ObjectUtils;
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

/**
 *描述：缺陷等级管理
 * @author Li.peng
 *@create 2018-08-27 13:33
 */
@Api(value = "缺陷等级管理controller",tags = {"缺陷等级管理"})
@RestController
@RequestMapping(value = "/rest/defectLevel",method = RequestMethod.POST)
public class DefectLevelRestController extends BaseController {
    @Autowired
    private DocumentService documentService;
    @Reference
    private DefectLevelHandler defectLevelHandler;
    @ApiOperation("缺陷等级新增/修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] delIds, DefectLevel entity){
        logger("缺陷等级新增/修改",JSONObject.toJSONString(entity));
        if(!ObjectUtils.allIsNotNull(entity.getLevelCode(),entity.getHours(),entity.getDefectTypeId())){
            return ResultForm.createErrorResultForm(entity,"缺陷等级、维修时长、缺陷类型不能为空");
        }
        DocInfo docInfo ;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", entity.getDocId(), delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        entity.setDocId(docInfo.getId());
        entity = defectLevelHandler.save(entity);
        return ResultForm.createSuccessResultForm(entity, "保存成功！");
    }

    @ApiOperation(value = "删除缺陷等级")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 缺陷等级id}") JsonRequestBody jsonRequestBody) {
        logger("删除缺陷等级", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "缺陷等级id不能为空！");
        }
        return ResultForm.createSuccessResultForm(defectLevelHandler.deleteByPrimaryKey(id), "缺陷等级删除成功！");
    }
}
