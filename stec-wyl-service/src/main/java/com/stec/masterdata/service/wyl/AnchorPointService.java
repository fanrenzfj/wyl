package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.AnchorPoint;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 19:31
 */
public interface AnchorPointService extends IAdvMySqlService<AnchorPoint, Long> {

    List<AnchorPoint> queryPoints(String tag, Double startMileage, Double endMileage) throws ServiceException;

}
