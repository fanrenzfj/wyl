package com.stec.masterdata.handler.wyl;

import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.wyl.HistoricalDefect;
import com.stec.masterdata.entity.wyl.HistoricalDefectBetter;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/10/12
 * Time: 11:10
 */
public interface HistoricalDefectHandler extends IAdvMySqlHandlerService<HistoricalDefect, Long> {
    boolean deleteEntity(Long id);

    DataPaging<HistoricalDefect> selectList(Map<String, Object> param, PageForm pageForm);

    List<HistoricalDefect> selectLists(HistoricalDefect historicalDefect);
}
