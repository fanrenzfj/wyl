package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.Material;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 20:57
 */
public interface MaterialHandler extends IAdvMySqlHandlerService<Material, Long> {
    boolean deleteEntity(Long id);
    void removePrice(Long materialId) throws DataServiceException;
}
