package com.stec.wyl.web.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stec.masterdata.entity.wyl.RoadEvaluateDetail;
import com.stec.masterdata.entity.wyl.RoadEvaluateItem;
import com.stec.masterdata.entity.wyl.RoadEvaluateReport;
import com.stec.masterdata.entity.wyl.RoadEvaluateTemplate;
import com.stec.masterdata.handler.wyl.RoadEvaluateDetailHandler;
import com.stec.masterdata.handler.wyl.RoadEvaluateItemHandler;
import com.stec.masterdata.handler.wyl.RoadEvaluateReportHandler;
import com.stec.masterdata.handler.wyl.RoadEvaluateTemplateHandler;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.wyl.web.WebApp;
import com.stec.wyl.web.service.RoadEvaluateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stec.framework.excel.ExcelTemplate;
import stec.framework.excel.handler.ExcelHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/5 0005
 * Time: 16:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApp.class)
public class RoadEvaluateTest {

    @Autowired
    private RoadEvaluateService roadEvaluateService;

    @Reference
    private RoadEvaluateReportHandler roadEvaluateReportHandler;

    @Reference
    private RoadEvaluateTemplateHandler roadEvaluateTemplateHandler;

    @Reference
    private RoadEvaluateItemHandler roadEvaluateItemHandler;

    @Reference
    private RoadEvaluateDetailHandler roadEvaluateDetailHandler;

    @Autowired
    private ExcelTemplate excelTemplate;

    @Test
    public void testGetReport() {
        List<RoadEvaluateReport> reports = roadEvaluateReportHandler.selectEntities(MapUtils.newHashMap("download", false));
        for (RoadEvaluateReport report : reports) {
            JSONObject result = roadEvaluateService.acceptResult(report.getCode());
            roadEvaluateReportHandler.sdEvaluate(report, result);
        }
    }

    @Test
    public void testDownReport() throws Exception {
        List<RoadEvaluateItem> items = roadEvaluateItemHandler.selectEntities(MapUtils.newHashMap("reportId", 4));

        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("结构", "name", String.class));
        headers.add(new ExcelHeader("评价时间",1, "startDate", Date.class, "yyyy年MM月dd日"));
        headers.add(new ExcelHeader("RDI", "rdi", Double.class));
        headers.add(new ExcelHeader("RQI", "rqi", Double.class));
        headers.add(new ExcelHeader("PCI", "pci", Double.class));
        headers.add(new ExcelHeader("SRI", "sri", Double.class));
        headers.add(new ExcelHeader("PSSI", "pssi", Double.class));
        headers.add(new ExcelHeader("MQI", "mqi", Double.class));
        headers.add(new ExcelHeader("SCI", "sci", Double.class));
        headers.add(new ExcelHeader("PQI", "pqi", Double.class));

        excelTemplate.exportObjects2Excel(items, headers, "路面评估", true, "D:/road_export.xlsx");

    }
}
