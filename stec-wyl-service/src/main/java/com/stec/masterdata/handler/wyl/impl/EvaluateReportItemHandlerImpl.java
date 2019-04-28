package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.handler.service.TreeMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EvaluateReportItem;
import com.stec.masterdata.handler.wyl.EvaluateReportItemHandler;
import com.stec.masterdata.service.wyl.EvaluateReportItemService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/16
 * Time: 11:20
 */
@Service
@Component
public class EvaluateReportItemHandlerImpl extends TreeMySqlHandlerService<EvaluateReportItemService, EvaluateReportItem> implements EvaluateReportItemHandler {

    @Override
    public List<EvaluateReportItem> selectList(EntityWrapper<EvaluateReportItem> entityWrapper) {
        return this.privateService.selectList(entityWrapper);
    }

    @Override
    public List findNum(HashMap hashMap) {
        return this.privateService.findNum(hashMap);
    }
}
