package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.handler.service.TreeMySqlHandlerService;
import com.stec.masterdata.entity.wyl.EvaluateTemplate;
import com.stec.masterdata.handler.wyl.EvaluateTemplateHandler;
import com.stec.masterdata.service.wyl.EvaluateTemplateService;
import org.springframework.stereotype.Component;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/15
 * Time: 20:10
 */
@Service
@Component
public class EvaluateTemplateHandlerImpl extends TreeMySqlHandlerService<EvaluateTemplateService, EvaluateTemplate> implements EvaluateTemplateHandler {
}
