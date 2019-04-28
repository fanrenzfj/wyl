package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.MileageInfo;
import com.stec.masterdata.handler.wyl.MileageInfoHandler;
import com.stec.masterdata.service.wyl.MileageInfoService;
import org.springframework.stereotype.Component;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/24
 * Time: 13:57
 */
@Service
@Component
public class MileageInfoHandlerImpl extends AdvMySqlHandlerService<MileageInfoService, MileageInfo, Long> implements MileageInfoHandler {
}
