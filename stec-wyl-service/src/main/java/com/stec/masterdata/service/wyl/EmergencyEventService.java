package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.EmergencyEvent;
import com.stec.masterdata.entity.wyl.EmergencyEventMaterial;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 15:27
 */
public interface EmergencyEventService extends IAdvMySqlService<EmergencyEvent, Long> {
    boolean matchProcess(Long id, String status) throws DataServiceException;
    EmergencyEvent handle(Long id, List<EmergencyEventMaterial> materialIds, List<Long> workGroupIds, List<Long> carIds);
}
