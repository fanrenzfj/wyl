package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.RoadEvaluateDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:40
 */
public interface RoadEvaluateDetailHandler extends IAdvMySqlHandlerService<RoadEvaluateDetail, Long> {
    List<RoadEvaluateDetail> selectEvaDataReport(Long reportId);
}
