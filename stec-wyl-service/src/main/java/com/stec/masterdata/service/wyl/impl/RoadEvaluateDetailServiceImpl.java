package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.RoadEvaluateDetail;
import com.stec.masterdata.service.wyl.RoadEvaluateDetailService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:41
 */
@Service
public class RoadEvaluateDetailServiceImpl extends AdvSqlDaoImpl<RoadEvaluateDetail, String> implements RoadEvaluateDetailService {
    @Override
    public List<RoadEvaluateDetail> selectEvaDataReport(Long reportId) {
        Map<String,Object> param = new HashMap<>();
        param.put("reportId",reportId);
        List<RoadEvaluateDetail> list = this.selectMapperList("com.stec.masterdata.mapper.wyl.RoadEvaluateMapper.selectEvaDataReport",param);
        return list;
    }
}
