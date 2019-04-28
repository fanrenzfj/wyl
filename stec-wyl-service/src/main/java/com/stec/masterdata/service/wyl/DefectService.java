package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.Defect;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 14:12
 */
public interface DefectService extends IAdvMySqlService<Defect, Long> {

    boolean matchStatus(Long defectId, String status) throws DataServiceException;

    void createWorkOrder(Long defectId) throws DataServiceException;

    void completeDefectFromOrder(Long orderId) throws DataServiceException;

    void removeDeviceId(Long defectId) throws  DataServiceException;
}
