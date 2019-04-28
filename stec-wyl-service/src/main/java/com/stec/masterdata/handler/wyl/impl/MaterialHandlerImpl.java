package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.Material;
import com.stec.masterdata.handler.wyl.MaterialHandler;
import com.stec.masterdata.service.wyl.MaterialService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 20:57
 */
@Service
@Component
public class MaterialHandlerImpl extends AdvMySqlHandlerService<MaterialService, Material, Long> implements MaterialHandler {
    @Override
    public boolean deleteEntity(Long id) {
        return this.privateService.deleteEntity(id);
    }

    @Override
    public void removePrice(Long materialId) throws DataServiceException {
        this.privateService.removePrice(materialId);
    }
}
