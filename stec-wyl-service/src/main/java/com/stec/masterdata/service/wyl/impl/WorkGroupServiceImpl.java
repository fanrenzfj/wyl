package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;

import com.stec.masterdata.entity.wyl.WorkGroup;
import com.stec.masterdata.entity.wyl.WorkGroupMember;
import com.stec.masterdata.service.wyl.WorkGroupMemberService;
import com.stec.masterdata.service.wyl.WorkGroupService;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/20
 * Time: 19:32
 */
@Service
public class WorkGroupServiceImpl extends AdvSqlDaoImpl<WorkGroup, String> implements WorkGroupService {

    @Autowired
    private WorkGroupMemberService workGroupMemberService;
    
    @Override
    public WorkGroup save(WorkGroup workGroup, List<Long> userIds) {
        if (ObjectUtils.isNull(workGroup.getId())) {
            workGroup = this.save(workGroup);
        } else {
            workGroup = this.save(workGroup);
            WorkGroupMember param = new WorkGroupMember();
            param.setGroupId(workGroup.getId());
            workGroupMemberService.delete(param);
        }
        List<WorkGroupMember> list=new ArrayList<>();
        if(userIds!=null)
        {
            for (Long userId : userIds) {
                WorkGroupMember workGroupMember=new WorkGroupMember();
                workGroupMember.setGroupId(workGroup.getId());
                workGroupMember.setUserId(userId);
                list.add(workGroupMember);
            }
            workGroupMemberService.save(list);
        }
        return workGroup;
    }

    @Override
    public boolean deleteEntity(Long id) {
        WorkGroupMember param = new WorkGroupMember();
        param.setGroupId(id);
        List<WorkGroupMember> list=workGroupMemberService.selectEntities(param);
        for (WorkGroupMember workGroupMember : list) {
            workGroupMemberService.delete(workGroupMember);
        }
        return this.deleteByPrimaryKey(id);

    }

    @Override
    public boolean containMembers(Long workGroupId, Long userId) {
        WorkGroup workGroup=this.selectByPrimaryKey(workGroupId);
        Long leaderId=workGroup.getLeaderId();
        if(leaderId.equals(userId)) {
            return true;
        }
        WorkGroupMember workGroupMember=new WorkGroupMember();
        workGroupMember.setGroupId(workGroupId);
        workGroupMember.setUserId(userId);
        workGroupMember = workGroupMemberService.selectEntity(workGroupMember);
        return ObjectUtils.isNotNull(workGroupMember);
    }
}
