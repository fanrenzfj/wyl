package com.stec.masterdata.handler.wyl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.handler.ITreeSqlHandlerService;
import com.stec.masterdata.entity.wyl.EvaluateReportItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/16
 * Time: 11:20
 */
public interface EvaluateReportItemHandler extends ITreeSqlHandlerService<EvaluateReportItem> {
    List<EvaluateReportItem> selectList(EntityWrapper<EvaluateReportItem> entityWrapper );
    //根据四级id和reportid查询数量 等级
    List findNum(HashMap hashMap);
}
