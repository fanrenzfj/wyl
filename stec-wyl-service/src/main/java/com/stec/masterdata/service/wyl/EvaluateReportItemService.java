package com.stec.masterdata.service.wyl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.service.ITreeSqlService;
import com.stec.masterdata.entity.wyl.EvaluateReportItem;

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
public interface EvaluateReportItemService extends ITreeSqlService<EvaluateReportItem> {
    List<EvaluateReportItem> selectList(EntityWrapper<EvaluateReportItem> entityWrapper );
    //根据四级id和reportid查询数量 等级
    List<Map> findNum(HashMap hashMap);
}
