package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.masterdata.entity.wyl.RoadEvaluateReport;
import com.stec.masterdata.handler.wyl.RoadEvaluateReportHandler;
import com.stec.masterdata.service.wyl.RoadEvaluateReportService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:37
 */
@Service
@Component
public class RoadEvaluateReportHandlerImpl extends AdvMySqlHandlerService<RoadEvaluateReportService, RoadEvaluateReport, Long> implements RoadEvaluateReportHandler {
    @Override
    public List<Map<String, Object>> evaluateDetails(Long id) throws ServiceException {
        return this.privateService.evaluateDetails(id);
    }

    @Override
    public List<Map<String, Object>> indexState(Long id, String index) throws ServiceException {
        return this.privateService.indexState(id, index);
    }

    @Override
    public void sdEvaluate(RoadEvaluateReport report, JSONObject evaluateJson) throws ServiceException {
        this.privateService.sdEvaluate(report, evaluateJson);
    }
}
