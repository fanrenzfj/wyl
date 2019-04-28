package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.wyl.EmergencyProgramme;
import com.stec.masterdata.handler.wyl.EmergencyProgrammeHandler;
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
import com.stec.utils.StringUtils;

/**
 *描述：应急预案管理
 * @author Li.peng
 *@create 2018-08-29 17:34
 */
@Api(value = "应急预案controller", tags = {"应急预案"})
@RestController
@RequestMapping(value = "/rest/emergencyProgramme", method = RequestMethod.POST)
public class EmergencyProgrammeRestController extends BaseController {
    @Reference
    private EmergencyProgrammeHandler emergencyProgrammeHandler;

    @Autowired
    private DocumentService documentService;

    @ApiOperation("应急预案新增/修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] pdfDelIds,Long[] mpDelIds, EmergencyProgramme entity) {
        logger("应急预案新增/修改", JSONObject.toJSONString(entity));
        if(!ObjectUtils.allIsNotNull(entity.getName(),entity.getProgrammeTime(),entity.getType(),entity.getLevel())){
            return ResultForm.createErrorResultForm(entity,"应急预案名称、预案制定时间、预案类别、级别不能为空");
        }
        DocInfo pdfDocInfo ;
        DocInfo mpDocInfo;
        try {
            pdfDocInfo = documentService.uploadFiles(multipartRequest, "pdfFile", entity.getPdfDocId(), pdfDelIds);
            mpDocInfo = documentService.uploadFiles(multipartRequest, "mpFile", entity.getMpDocId(), mpDelIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        entity.setPdfDocId(pdfDocInfo.getId());
        entity.setMpDocId(mpDocInfo.getId());
        entity = emergencyProgrammeHandler.save(entity);
        return ResultForm.createSuccessResultForm(entity, "保存成功！");
    }

    @ApiOperation("应急预案分页列表查询")
    @RequestMapping(value = "/list")
    public ResultForm list(@ApiParam(value = "{name:name,[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        String name = jsonRequestBody.getString("name");
        EntityWrapper<EmergencyProgramme> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("name", name);
        }
        return ResultForm.createSuccessResultForm(emergencyProgrammeHandler.selectEntities(wrapper, pageForm), "应急预案列表查询成功！");
    }

    @ApiOperation("获取应急预案")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id 应急预案id}") @RequestBody  JsonRequestBody jsonRequestBody){
        Long id=jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"应急预案ID不能为空");
        }
        return ResultForm.createSuccessResultForm(emergencyProgrammeHandler.selectByPrimaryKey(id),"应急预案信息获取成功");
    }

    @ApiOperation("删除应急预案")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 应急预案id}") JsonRequestBody jsonRequestBody){
        logger("删除应急预案",jsonRequestBody);
        Long id=jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"应急预案id不能为空");
        }
        return ResultForm.createSuccessResultForm(emergencyProgrammeHandler.deleteByPrimaryKey(id));
    }
}
