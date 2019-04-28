package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.masterdata.entity.wyl.AnchorPoint;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 19:30
 */
public interface AnchorPointHandler extends IAdvMySqlHandlerService<AnchorPoint, Long> {

    List<AnchorPoint> queryPoints(String tag, Double startMileage, Double endMileage) throws ServiceException;
}
