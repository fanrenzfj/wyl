package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EvaluateReport;
import com.stec.masterdata.handler.wyl.EvaluateReportHandler;
import com.stec.masterdata.service.wyl.EvaluateReportService;
import org.springframework.stereotype.Component;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/16
 * Time: 10:30
 */
@Service
@Component
public class EvaluateReportHandlerImpl extends AdvMySqlHandlerService<EvaluateReportService, EvaluateReport, Long> implements EvaluateReportHandler {
}
