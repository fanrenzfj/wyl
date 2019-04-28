package com.stec.masterdata.handler.wyl;

import com.alibaba.fastjson.JSONObject;
import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.masterdata.entity.wyl.RoadEvaluateReport;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:36
 */
public interface RoadEvaluateReportHandler extends IAdvMySqlHandlerService<RoadEvaluateReport, Long> {

    List<Map<String, Object>> evaluateDetails(Long id) throws ServiceException;

    List<Map<String, Object>> indexState(Long id, String index) throws ServiceException;

    void sdEvaluate(RoadEvaluateReport report, JSONObject evaluateJson) throws ServiceException;
}
