package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.RoadEvaluateDetail;
import com.stec.masterdata.handler.wyl.RoadEvaluateDetailHandler;
import com.stec.masterdata.service.wyl.RoadEvaluateDetailService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:41
 */
@Service
@Component
public class RoadEvaluateDetailHandlerImpl extends AdvMySqlHandlerService<RoadEvaluateDetailService, RoadEvaluateDetail, Long> implements RoadEvaluateDetailHandler {
    @Override
    public List<RoadEvaluateDetail> selectEvaDataReport(Long reportId) {
        return this.privateService.selectEvaDataReport(reportId);
    }
}
