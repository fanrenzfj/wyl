package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.PlanItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:00
 */
public interface PlanItemService extends IAdvMySqlService<PlanItem, Long> {

    void save(PlanItem entity, List<Long> deviceIds);
}
