package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.WorkGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/20
 * Time: 19:31
 */
public interface WorkGroupHandler extends IAdvMySqlHandlerService<WorkGroup, Long> {
    WorkGroup save(WorkGroup workGroup, List<Long> userIds);
    boolean deleteEntity(Long id);
    boolean containMembers(Long workGroupId,Long userId);
}
