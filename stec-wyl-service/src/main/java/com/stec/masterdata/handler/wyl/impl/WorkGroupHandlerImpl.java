package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.WorkGroup;
import com.stec.masterdata.handler.wyl.WorkGroupHandler;
import com.stec.masterdata.service.wyl.WorkGroupService;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/20
 * Time: 19:33
 */
@Service
@Component
public class WorkGroupHandlerImpl extends AdvMySqlHandlerService<WorkGroupService, WorkGroup, Long> implements WorkGroupHandler {
    @Override
    public WorkGroup save(WorkGroup workGroup, List<Long> userIds) {
        return this.privateService.save(workGroup,userIds);
    }

    @Override
    public boolean deleteEntity(Long id) {
        return this.privateService.deleteEntity(id);
    }

    @Override
    public boolean containMembers(Long workGroupId, Long userId) {return this.privateService.containMembers(workGroupId,userId);}
}
