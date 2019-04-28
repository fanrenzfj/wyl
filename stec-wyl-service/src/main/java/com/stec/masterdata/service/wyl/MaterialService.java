package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.Material;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 20:58
 */
public interface MaterialService extends IAdvMySqlService<Material, Long> {
    boolean deleteEntity(Long id);
    void removePrice(Long materialId) throws DataServiceException;
}
