package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.stec.masterdata.entity.basic.DocAttach;
import com.stec.masterdata.handler.basic.DocAttachHandler;
import com.stec.utils.ObjectUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.bean.FileInfoBean;
import com.stec.framework.metadata.usage.bean.ImageInfoBean;
import com.stec.wyl.web.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/2 0002
 * Time: 10:24
 */
@Api(tags = {"文档管理"})
@RestController
@RequestMapping(value = "/rest/doc", method = RequestMethod.POST)
public class DocRestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DocumentService documentService;

    @Reference
    private DocAttachHandler docAttachHandler;

    @ApiOperation("获取系统文档服务器的地址")
    @RequestMapping("/serverUrl")
    public ResultForm docServerUrl() {
        return ResultForm.createSuccessResultForm(documentService.getServerUrl(), "success!");
    }

    @ApiOperation("上传文档")
    @RequestMapping("/uploadDoc")
    public ResultForm uploadDoc(MultipartRequest multipartRequest) {
        logger("上传文档");
        ResultForm<?> result;
        try {
            FileInfoBean bean = documentService.uploadImage(multipartRequest, "attach");
            String url = documentService.getFileUrl(bean);
            result = ResultForm.createSuccessResultForm(url, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result = ResultForm.createErrorResultForm(null, "");
        }
        return result;
    }

    @ApiOperation("上传图片")
    @RequestMapping(value = "/uploadImg", produces = {"application/json;", "text/html;charset=UTF-8;"})
    public Object uploadImg(MultipartRequest multipartRequest) {
        logger("上传图片");
        ResultForm<?> result = null;
        try {
            ImageInfoBean bean = documentService.uploadImage(multipartRequest, "file[0]");
            String url = documentService.getFileUrl(bean);
            result = ResultForm.createSuccessResultForm(url, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result = ResultForm.createErrorResultForm(null, "");
        }
        return result;
    }

    @RequestMapping("/commitExcelData")
    public ResultForm commitExcelData(@RequestBody JsonRequestBody jsonRequestBody) {
        String extractedCode = UUID.randomUUID().toString();
        LinkedHashMap<String, Object> titleMap = new LinkedHashMap<>();
        List<Map<String, Object>> valueMaps = new ArrayList<>();
        JSONArray titles = jsonRequestBody.getJSONArray("titles");
        JSONArray content = jsonRequestBody.getJSONArray("values");
        int size = titles.size();
        for (int i = 0; i < size; i++) {
            titleMap.put("column" + i, titles.getString(i));
        }

        size = content.size();
        for (int i = 0; i < size; i++) {
            JSONArray array = content.getJSONArray(i);
            int columns = array.size();
            Map<String, Object> map = new HashMap<>();
            valueMaps.add(map);
            for (int j = 0; j < columns; j++) {
                map.put("column" + j, array.get(j));
            }
        }

        Map<String, Object> excelData = new HashMap<>();
        excelData.put("titles", titleMap);
        excelData.put("values", valueMaps);
        getCache(extractedCode, excelData, null);
        return ResultForm.createSuccessResultForm(extractedCode, "success");
    }

    @ApiOperation("获取文档列表")
    @RequestMapping("/attachList")
    public ResultForm attachList(@ApiParam(value = "{docId:docId}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long docId = jsonRequestBody.getLong("docId");
        if (ObjectUtils.isNull(docId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "文档ID不能为空！");
        }
        DocAttach attach = new DocAttach();
        attach.setDocInfoId(docId);
        return ResultForm.createSuccessResultForm(docAttachHandler.selectEntities(attach), "获取文档列表成功！");
    }
}
