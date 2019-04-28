package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.TreeMySqlHandlerService;
import com.stec.masterdata.entity.wyl.RoadEvaluateItem;
import com.stec.masterdata.handler.wyl.RoadEvaluateItemHandler;
import com.stec.masterdata.service.wyl.RoadEvaluateItemService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:40
 */
@Service
@Component
public class RoadEvaluateItemHandlerImpl extends TreeMySqlHandlerService<RoadEvaluateItemService, RoadEvaluateItem> implements RoadEvaluateItemHandler {
}
