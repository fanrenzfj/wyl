package com.stec.masterdata.service.wyl.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.service.mybatis.TreeSqlDaoImpl;
import com.stec.masterdata.entity.wyl.EvaluateReportItem;
import com.stec.masterdata.service.wyl.EvaluateReportItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/16
 * Time: 11:20
 */
@Service
public class EvaluateReportItemServiceImpl extends TreeSqlDaoImpl<EvaluateReportItem> implements EvaluateReportItemService {

    @Override
    public List<EvaluateReportItem> selectList(EntityWrapper<EvaluateReportItem> entityWrapper) {
        ArrayList<EvaluateReportItem> arrayList = new ArrayList<>();
        List<EvaluateReportItem> reportItemList = this.selectEntities(entityWrapper);
        for (EvaluateReportItem evaluateReportItem : reportItemList) {
            String treeId = evaluateReportItem.getTreeId();
            if (treeId.length() <= 11 && treeId.length()>5) {
                arrayList.add(evaluateReportItem);
            }
        }
        return arrayList;
    }

    @Override
    public List<Map> findNum(HashMap hashMap) {
        return this.selectMapperList("com.stec.masterdata.mapper.wyl.EvaluateReportItemMyMapper.findNum",hashMap);
    }
}
