package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.DefectTypeUsage;
import com.stec.masterdata.handler.wyl.DefectTypeHandler;
import com.stec.masterdata.service.wyl.DefectTypeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 13:34
 */
@Service
@Component
public class DefectTypeHandlerImpl extends AdvMySqlHandlerService<DefectTypeService, DefectType, Long> implements DefectTypeHandler {
    @Override
    public boolean deleteEntity(Long id) {
        return this.privateService.deleteEntity(id);
    }

    @Override
    public List<DefectType> findByDeviceId(Long id) {
        return  this.privateService.findByDeviceId(id);
    }

    @Override
    public List<DefectType> findByStructureId(Long id) {
        return this.privateService.findByStructureId(id);
    }

    @Override
    public DefectType save(DefectType entity, List<DefectTypeUsage> usageList) {
        return this.privateService.save(entity, usageList);
    }
}
