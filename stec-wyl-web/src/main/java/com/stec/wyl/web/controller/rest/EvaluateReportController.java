package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.wyl.EvaluateReport;
import com.stec.masterdata.entity.wyl.EvaluateReportItem;
import com.stec.masterdata.handler.wyl.EvaluateReportHandler;
import com.stec.masterdata.handler.wyl.EvaluateReportItemHandler;
import com.stec.utils.CollectionUtils;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(value = "全寿命结构报告", tags = {"全寿命结构报告"})
@RestController
@RequestMapping(value = "/rest/evaluateReport", method = RequestMethod.POST)
public class EvaluateReportController extends BaseController {

    @Reference
    private EvaluateReportHandler evaluateReportHandler;
    @Reference
    private EvaluateReportItemHandler evaluateReportItemHandler;

    @ApiOperation("获取结构报告分页list集合")
    @RequestMapping("/findReportList")
    public ResultForm findReportList(@ApiParam(value = "{beginDate:beginDate,endDate:endDate,name:name}")@RequestBody JsonRequestBody jsonRequestBody) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //获取pageForm参数
        PageForm pageForm = jsonRequestBody.getPageForm();
        //封装wrapper对象
        EntityWrapper<EvaluateReport> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderBy(EvaluateReport.COLUMN_START_DATE, false);
        //封装时间段
        Date startDate = jsonRequestBody.getDate("beginDate");
        Date endDate = jsonRequestBody.getDate("endDate");
        if (ObjectUtils.allIsNotNull(startDate, endDate)) {
            entityWrapper.between(EvaluateReport.COLUMN_EVALUATE_DATE, startDate, endDate);
        }
        //封装name模糊查询
        String name = jsonRequestBody.getString("name");
        if (StringUtils.isNotEmpty(name)) {
            entityWrapper.like("name", name);
        }
        DataPaging<EvaluateReport> dataPaging = evaluateReportHandler.selectEntities(entityWrapper, pageForm);
        return ResultForm.createSuccessResultForm(dataPaging, "评估列表查询成功！");

    }

    @ApiOperation("根据reportID或者最新一次时间获取评估结果")
    @RequestMapping("/evaluateReportItemByID")
    public ResultForm evaluateReportItemByID(@ApiParam(value = "{id:id}")@RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        //判断从前台获取reportid是否为空，如果为空获取最新的评估结果
        if (ObjectUtils.isNull(id)) {
            //说明没有reportId获取最新时间的评估结果
            EntityWrapper<EvaluateReport> entityWrapper = new EntityWrapper<>();
            entityWrapper.orderBy(EvaluateReport.COLUMN_START_DATE, false);
            List<EvaluateReport> reportLists = evaluateReportHandler.selectEntities(entityWrapper);
            //判断获取的集合非空
            if (CollectionUtils.isNotEmpty(reportLists)) {
                id = reportLists.get(0).getId();
            }
        }
        if (ObjectUtils.isNotNull(id)) {
            return ResultForm.createSuccessResultForm(evaluateReportItemHandler.selectEntities(MapUtils.newHashMap(EvaluateReportItem.ATTRIBUTE_EVALUATE_REPORT_ID, id)), "查询成功！");
        } else {
            return ResultForm.createErrorResultForm(null, "没有评估数据");
        }
    }

    @ApiOperation("获取性能趋势对比 首次 上次 本次")
    @RequestMapping("/findThreeRankEvaluateReport")
    public ResultForm findThreeRankEvaluateReport(@ApiParam(value = "{id:id*}")@RequestBody JsonRequestBody jsonRequestBody) {
        //获取本次评估报告的id
        Long evaluateReportId = jsonRequestBody.getLong("id");
        if (org.springframework.util.ObjectUtils.isEmpty(evaluateReportId)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "id不能为空");
        }
        //获取上次评估报告的id和首次的
        EntityWrapper<EvaluateReport> reportEntityWrapper = new EntityWrapper<>();
        reportEntityWrapper.orderBy(EvaluateReport.COLUMN_START_DATE, false);
        List<EvaluateReport> reports = evaluateReportHandler.selectEntities(reportEntityWrapper);
        if (CollectionUtils.isNotEmpty(reports)) {
            HashMap hashMap = new HashMap();
            EvaluateReport thisTime = evaluateReportHandler.selectByPrimaryKey(evaluateReportId);

            if (ObjectUtils.isNotNull(thisTime)) {
                //格式化日期
                String thisDate = findDate(thisTime.getStartDate());
                if (reports.size() >= 2) {
                    EvaluateReport lastTime = findListById(reports, evaluateReportId);
                    EvaluateReport firstTime = reports.get(reports.size() - 1);
                    //格式化日期
                    String firstDate = findDate(firstTime.getStartDate());
                    String lastDate = findDate(lastTime.getStartDate());
                    hashMap.put("本次" + thisDate, findRepotList(evaluateReportId));
                    hashMap.put("上次" + lastDate, findRepotList(firstTime.getId()));
                    hashMap.put("首次" + firstDate, findRepotList(lastTime.getId()));
                    return ResultForm.createSuccessResultForm(hashMap, "查询成功！");
                } else {
                    hashMap.put("本次" + thisDate, findRepotList(evaluateReportId));
                    return ResultForm.createSuccessResultForm(hashMap, "查询成功！");
                }
            }

        }
        return ResultForm.createErrorResultForm(null, "无评估报告");

    }

    //根据reportID获取整体的评估结果 用于本次 上次 首次比较
    private EvaluateReport findListById(List<EvaluateReport> reports, Long evaluateReportId) {
        Integer i = null;
        for (EvaluateReport evaluateReport : reports) {
            Long id = evaluateReport.getId();
            if (id == evaluateReportId) {
                i = reports.indexOf(evaluateReport);
            }
        }
        if (i+1 == reports.size()) {
            return reports.get(i);
        } else {
            return reports.get(i + 1);
        }
    }

    private List<EvaluateReportItem> findRepotList(Long reportID) {
        EntityWrapper<EvaluateReportItem> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(EvaluateReportItem.COLUMN_EVALUATE_REPORT_ID, reportID);
        return evaluateReportItemHandler.selectList(entityWrapper);
    }

    //时间格式
    private String findDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String d = sdf.format(date);
        return  d;
    }
}
