package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.HistoricalDefect;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/10/12
 * Time: 11:10
 */
public interface HistoricalDefectService extends IAdvMySqlService<HistoricalDefect, Long> {
    boolean deleteEntity(Long id);

    DataPaging<HistoricalDefect> selectList(Map<String, Object> param, PageForm pageForm);

    List<HistoricalDefect> selectList(HistoricalDefect historicalDefect);
}
