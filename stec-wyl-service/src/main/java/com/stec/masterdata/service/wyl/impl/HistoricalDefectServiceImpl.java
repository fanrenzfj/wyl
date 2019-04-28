package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.HistoricalDefect;
import com.stec.masterdata.service.basic.DocInfoService;
import com.stec.masterdata.service.wyl.HistoricalDefectService;
import com.stec.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class HistoricalDefectServiceImpl extends AdvSqlDaoImpl<HistoricalDefect, String> implements HistoricalDefectService {
    
    @Autowired
    DocInfoService docInfoService;

    @Autowired
    HistoricalDefectService historicalDefectService;

    @Override
    public boolean deleteEntity(Long id) {
        HistoricalDefect historicalDefect=this.selectByPrimaryKey(id);
        docInfoService.deleteByPrimaryKey(historicalDefect.getReportDocId());
        docInfoService.deleteByPrimaryKey(historicalDefect.getDealDocId());
        return this.deleteByPrimaryKey(id);
    }

    @Override
    public DataPaging<HistoricalDefect> selectList(Map<String, Object> param, PageForm pageForm) {
        return this.selectMapperPaging("com.stec.masterdata.mapper.wyl.WylDefectMapper.hisPageCount",
                "com.stec.masterdata.mapper.wyl.WylDefectMapper.hisPageList",
                param, pageForm);
    }

    @Override
    public List<HistoricalDefect> selectList(HistoricalDefect historicalDefect) {
        return this.selectEntities(historicalDefect);
    }
}
