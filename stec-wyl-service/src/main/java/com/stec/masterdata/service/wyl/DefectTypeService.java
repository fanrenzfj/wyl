package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.DefectTypeUsage;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 13:35
 */
public interface DefectTypeService extends IAdvMySqlService<DefectType, Long> {
    boolean deleteEntity(Long id);
    List<DefectType> findByDeviceId(Long id);
    List<DefectType> findByStructureId(Long id);
    DefectType save(DefectType entity, List<DefectTypeUsage> usageList);
}
