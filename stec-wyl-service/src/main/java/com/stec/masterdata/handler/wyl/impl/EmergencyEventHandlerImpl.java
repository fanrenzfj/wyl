package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.EmergencyEvent;
import com.stec.masterdata.entity.wyl.EmergencyEventMaterial;
import com.stec.masterdata.handler.wyl.EmergencyEventHandler;
import com.stec.masterdata.service.wyl.EmergencyEventService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 15:26
 */
@Service
@Component
public class EmergencyEventHandlerImpl extends AdvMySqlHandlerService<EmergencyEventService, EmergencyEvent, Long> implements EmergencyEventHandler {
    @Override
    public boolean matchProcess(Long id, String status) throws DataServiceException {
        return this.privateService.matchProcess(id, status);
    }

    @Override
    public EmergencyEvent handle(Long id, List<EmergencyEventMaterial> materialIds, List<Long> workGroupIds, List<Long> carIds) {
        return this.privateService.handle(id,materialIds,workGroupIds,carIds);
    }
}
