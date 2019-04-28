package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.masterdata.entity.wyl.AnchorPoint;
import com.stec.masterdata.handler.wyl.AnchorPointHandler;
import com.stec.masterdata.service.wyl.AnchorPointService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 19:32
 */
@Service
@Component
public class AnchorPointHandlerImpl extends AdvMySqlHandlerService<AnchorPointService, AnchorPoint, Long> implements AnchorPointHandler {
    @Override
    public List<AnchorPoint> queryPoints(String tag, Double startMileage, Double endMileage) throws ServiceException {
        return this.privateService.queryPoints(tag, startMileage, endMileage);
    }
}
