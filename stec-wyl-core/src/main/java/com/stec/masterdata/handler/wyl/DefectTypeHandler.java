package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.DefectTypeUsage;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 13:34
 */
public interface DefectTypeHandler extends IAdvMySqlHandlerService<DefectType, Long> {

    boolean deleteEntity(Long id);

    List<DefectType> findByDeviceId(Long id);

    List<DefectType> findByStructureId(Long id);

    DefectType save(DefectType entity, List<DefectTypeUsage> usageList);
}
