package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.RoadEvaluateDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:41
 */
public interface RoadEvaluateDetailService extends IAdvMySqlService<RoadEvaluateDetail, Long> {
    List<RoadEvaluateDetail> selectEvaDataReport(Long reportId);
}
