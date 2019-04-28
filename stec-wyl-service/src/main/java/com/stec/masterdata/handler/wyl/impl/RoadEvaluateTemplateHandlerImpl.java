package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.TreeMySqlHandlerService;
import com.stec.masterdata.entity.wyl.RoadEvaluateTemplate;
import com.stec.masterdata.handler.wyl.RoadEvaluateTemplateHandler;
import com.stec.masterdata.service.wyl.RoadEvaluateTemplateService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:36
 */
@Service
@Component
public class RoadEvaluateTemplateHandlerImpl extends TreeMySqlHandlerService<RoadEvaluateTemplateService, RoadEvaluateTemplate> implements RoadEvaluateTemplateHandler {
}
