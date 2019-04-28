package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.entity.wyl.DefectInspectUser;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.HistoricalDefect;
import com.stec.masterdata.entity.wyl.HistoricalDefectBetter;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.wyl.DefectInspectUserHandler;
import com.stec.masterdata.handler.wyl.DefectTypeHandler;
import com.stec.masterdata.handler.wyl.HistoricalDefectHandler;
import com.stec.masterdata.service.wyl.HistoricalDefectService;
import com.stec.utils.CollectionUtils;
import com.stec.utils.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/10/12
 * Time: 11:10
 */
@Service
@Component
public class HistoricalDefectHandlerImpl extends AdvMySqlHandlerService<HistoricalDefectService, HistoricalDefect, Long> implements HistoricalDefectHandler {
    @Reference
    private DefectTypeHandler defectTypeHandler;
    @Reference
    private DefectInspectUserHandler defectInspectUserHandler;
    @Reference
    private DeviceHandler deviceHandler;
    @Reference
    private StructureHandler structureHandler;

    @Override
    public boolean deleteEntity(Long id) {
        return this.privateService.deleteEntity(id);
    }

    @Override
    public DataPaging<HistoricalDefect> selectList(Map<String, Object> param, PageForm pageForm) {
        return this.privateService.selectList(param, pageForm);
    }

    @Override
    public List<HistoricalDefect> selectLists(HistoricalDefect historicalDefect) {
        return this.privateService.selectList(historicalDefect);
    }

}
