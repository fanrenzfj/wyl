package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.wyl.EvaluateReportItem;
import com.stec.masterdata.handler.wyl.EvaluateReportItemHandler;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Api(value = "评估报告分数", tags = {"评估报告分数"})
@RestController
@RequestMapping(value = "/rest/evaluateReportItem", method = RequestMethod.POST)
public class EvaluateReportItemController extends BaseController {
    @Reference
    private EvaluateReportItemHandler evaluateReportItemHandler;

    @ApiOperation("获取性能分数排行")
    @RequestMapping("/rank")
    public ResultForm rank(@ApiParam(value = "{evaluateReportId:evaluateReportId*,name:name}") @RequestBody JsonRequestBody jsonRequestBody) {
        //获取前台点的是什么 整体/主体/附属
        String name = jsonRequestBody.getString("name");

        Long evaluateReportId = jsonRequestBody.getLong("evaluateReportId");
        if (ObjectUtils.isEmpty(evaluateReportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "参数不能为空");
        }
        EntityWrapper<EvaluateReportItem> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderBy(EvaluateReportItem.COLUMN_VALUE, true);
        entityWrapper.eq(EvaluateReportItem.COLUMN_EVALUATE_REPORT_ID, evaluateReportId);
        //创建装取rank集合的容器
        ArrayList<EvaluateReportItem> arrayList = new ArrayList<>();
        if (StringUtils.isEmpty(name) || name.equals("整体")) {
            List<EvaluateReportItem> reportItemList = evaluateReportItemHandler.selectEntities(entityWrapper);
            for (EvaluateReportItem evaluateReportItem : reportItemList) {
                String treeId = evaluateReportItem.getTreeId();
                if (treeId.length() <= 11 && treeId.length()>5) {
                    arrayList.add(evaluateReportItem);
                }
            }
            return ResultForm.createSuccessResultForm(arrayList, "rank成功！");
            //默认插整体
        } else {
            entityWrapper.like("name", name);
            return ResultForm.createSuccessResultForm(evaluateReportItemHandler.selectEntities(entityWrapper), "rank成功！");
        }

    }

    @ApiOperation("通过四级id获取最基本单元评价分数排行")
    @RequestMapping("/unitRankByID")
    public ResultForm unitRankByID(@ApiParam(value = "{id:id*,evaluateReportId:evaluateReportId}") @RequestBody JsonRequestBody jsonRequestBody) {
        //此方法参数 四级id
        PageForm pageForm = jsonRequestBody.getPageForm();
        Long evaluateReportId = jsonRequestBody.getLong("evaluateReportId");
        if (ObjectUtils.isEmpty(evaluateReportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "evaluateReportId不能为空");
        }
        //获取四级id  如盾彻结构id
        Long id = jsonRequestBody.getLong("id");
        //查询基层单元rank
        EntityWrapper<EvaluateReportItem> entityWrapper = findWrapper(id, evaluateReportId);
        entityWrapper.orderBy(EvaluateReportItem.COLUMN_VALUE, true);
        DataPaging<EvaluateReportItem> dataPaging = evaluateReportItemHandler.selectEntities(entityWrapper, pageForm);
        return ResultForm.createSuccessResultForm(dataPaging, "rank成功！");
    }

    @ApiOperation("通过四级id获取综合评价")
    @RequestMapping("/findTotalBlock")
    public ResultForm findTotalBlock(@ApiParam(value = "{id:id*,evaluateReportId:evaluateReportId}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long evaluateReportId = jsonRequestBody.getLong("evaluateReportId");
        //此方法参数 四级id
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isEmpty(evaluateReportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "evaluateReportId不能为空");
        } else if (ObjectUtils.isEmpty(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "id不能为空");
        }

        EntityWrapper<EvaluateReportItem> entityWrapper = findWrapper(id, evaluateReportId);
        List<EvaluateReportItem> items = evaluateReportItemHandler.selectEntities(entityWrapper);
        HashMap<String, List<EvaluateReportItem>> hashMap = new HashMap();
        for (EvaluateReportItem evaluateReportItem : items) {
            String tag = evaluateReportItem.getTag();
            if (hashMap.containsKey(tag)) {
                hashMap.get(tag).add(evaluateReportItem);
            } else {
                List<EvaluateReportItem> itemList = new ArrayList<>();
                itemList.add(evaluateReportItem);
                hashMap.put(tag, itemList);
            }
        }
        return ResultForm.createSuccessResultForm(hashMap, "成功！");
    }

    @ApiOperation("通过四级id获取评价占比")
    @RequestMapping("/findLevelNum")
    public ResultForm findLevelNum(@ApiParam(value = "{id:id*,evaluateReportId:evaluateReportId}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long evaluateReportId = jsonRequestBody.getLong("evaluateReportId");
        //此方法参数 四级id
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isEmpty(evaluateReportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "evaluateReportId不能为空");
        } else if (ObjectUtils.isEmpty(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "id不能为空");
        }
        HashMap hashMap = new HashMap();
        hashMap.put("reportId", evaluateReportId);
        hashMap.put("parentId", id);
        List nums = evaluateReportItemHandler.findNum(hashMap);
        return ResultForm.createSuccessResultForm(nums, "num成功！");
    }

    @ApiOperation("显示四级id的分数以及上面二级信息")
    @RequestMapping("/findParent")
    public ResultForm  findParent(@ApiParam(value = "{id:id*,evaluateReportId:evaluateReportId*}") @RequestBody JsonRequestBody jsonRequestBody){
        Long evaluateReportId = jsonRequestBody.getLong("evaluateReportId");
        //此方法参数 四级id
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isEmpty(evaluateReportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "evaluateReportId不能为空");
        } else if (ObjectUtils.isEmpty(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "id不能为空");
        }
        EvaluateReportItem item=new EvaluateReportItem();
        item.setId(id);
        item.setEvaluateReportId(evaluateReportId);
        //查询四级本身
        EvaluateReportItem dbFouritem=evaluateReportItemHandler.selectEntity(item);
        List<EvaluateReportItem> list=findParent(dbFouritem,new ArrayList<>());
        return ResultForm.createSuccessResultForm(list,"查询成功！");
    }

    //封装wrapper
    private EntityWrapper<EvaluateReportItem> findWrapper(Long id, Long evaluateReportId) {
        EntityWrapper<EvaluateReportItem> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(EvaluateReportItem.COLUMN_EVALUATE_REPORT_ID, evaluateReportId);
        //设置父id
        entityWrapper.eq(EvaluateReportItem.TREE_COLUMN_PARENT_ID, id);
        return entityWrapper;
    }
    //递归到二级树
    public List<EvaluateReportItem> findParent(EvaluateReportItem evaluateReportItem,List<EvaluateReportItem> items){
        if (evaluateReportItem.getTreeId().length()<6){
            return items;
        }else{
            items.add(evaluateReportItem);
            EvaluateReportItem item=new EvaluateReportItem();
            item.setId(evaluateReportItem.getParentId());
            EvaluateReportItem dbitem=evaluateReportItemHandler.selectEntity(item);
            findParent(dbitem,items);
        }
        return items;
    }
}
