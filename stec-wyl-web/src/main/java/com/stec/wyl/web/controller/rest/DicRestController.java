package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.handler.basic.SysDicHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author liweigao
 * Date: 2018-07-20
 * Time: 8:43
 */
@Api(value = "数据字典controller", tags = {"数据字典查询和树形结构"})
@RestController
@RequestMapping(value = "/rest/dic", method = RequestMethod.POST)
public class DicRestController extends BaseController {
    @Reference
    private SysDicHandler sysDicHandler;


    @ApiOperation("保存或修改数据字典")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody JsonRequestBody jsonRequestBody) {
        logger("保存数据字典结构", jsonRequestBody);
        SysDic entity = jsonRequestBody.tryGet(SysDic.class);
        if (!StringUtils.isAllNotBlank(entity.getName(), entity.getCode())) {
            return ResultForm.createErrorResultForm(entity, "字典名称，编码不能为空！");
        }
        return ResultForm.createSuccessResultForm(sysDicHandler.save(entity), "保存成功！");
    }

    @ApiOperation("数据字典根节点清单")
    @RequestMapping("/rootList")
    public ResultForm rootList() {
        return ResultForm.createSuccessResultForm(sysDicHandler.selectRootList(new SysDic()), "获取数据字典列表成功！");
    }

    @ApiOperation("数据字典结构树")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(name = "数据节点", value = "{id:id}") JsonRequestBody jsonRequestBody) {
        return ResultForm.createSuccessResultForm(sysDicHandler.selectTreeByParentId(jsonRequestBody.getLong("id")), "查询成功！");
    }

    @ApiOperation("删除字典节点")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(name = "删除节点", value = "{id:id}") JsonRequestBody jsonRequestBody) {
        logger("删除节点", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "节点id不能为空！");
        }
        sysDicHandler.deleteByPrimaryKey(id);
        return ResultForm.createSuccessResultForm(jsonRequestBody, "节点删除成功！");
    }

    @ApiOperation("获取节点")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(name = "获取节点", value = "{id:id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "节点id为空！");
        }
        return ResultForm.createSuccessResultForm(sysDicHandler.selectByPrimaryKey(id), "节点获取成功！");
    }

    @ApiOperation("获取子节点")
    @RequestMapping("/getChildren")
    public ResultForm getChildren(@RequestBody @ApiParam(name = "获取子节点", value = "{id:id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "节点id为空！");
        }
        return ResultForm.createSuccessResultForm(sysDicHandler.selectListByParentId(id), "节点获取成功！");
    }

    @ApiOperation("获取Map")
    @RequestMapping("/getDicMap")
    public ResultForm getDicMap() {
        List<SysDic> listRoot = sysDicHandler.selectRootList(new SysDic());
        int size = listRoot.size();
        Map<String, List<SysDic>> map = new LinkedMap();
        for (int i = 0; i < size; i++) {
            String code = listRoot.get(i).getCode();
            Long id = listRoot.get(i).getId();
            List<SysDic> listDic = sysDicHandler.selectListByParentId(id);
            map.put(code, listDic);
        }
        return ResultForm.createSuccessResultForm(map);
    }
}
