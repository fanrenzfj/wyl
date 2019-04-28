package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.WorkGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/20
 * Time: 19:32
 */
public interface WorkGroupService extends IAdvMySqlService<WorkGroup, Long> {
    WorkGroup save(WorkGroup workGroup,List<Long> userIds);
    boolean deleteEntity(Long id);
    boolean containMembers(Long workGroupId,Long userId);
}
