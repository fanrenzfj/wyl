package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.Defect;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 14:12
 */
public interface DefectHandler extends IAdvMySqlHandlerService<Defect, Long> {

    boolean matchStatus(Long defectId, String status) throws DataServiceException;

    void createWorkOrder(Long defectId) throws DataServiceException;

    void removeDeviceId(Long defectId) throws  DataServiceException;

    //获取缺陷图片的信息
    List<Defect> getFilePath(List<Defect> list);
}
