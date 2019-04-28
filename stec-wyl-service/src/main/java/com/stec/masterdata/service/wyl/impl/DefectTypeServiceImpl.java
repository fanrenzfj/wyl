package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.entity.protocol.DeviceType;
import com.stec.masterdata.entity.wyl.DefectLevel;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.DefectTypeUsage;
import com.stec.masterdata.service.project.DeviceService;
import com.stec.masterdata.service.project.StructureService;
import com.stec.masterdata.service.protocol.DeviceTypeService;
import com.stec.masterdata.service.wyl.DefectLevelService;
import com.stec.masterdata.service.wyl.DefectTypeService;
import com.stec.masterdata.service.wyl.DefectTypeUsageService;
import com.stec.utils.MapUtils;
import com.stec.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 13:35
 */
@Service
public class DefectTypeServiceImpl extends AdvSqlDaoImpl<DefectType, String> implements DefectTypeService {
    @Autowired
    DefectLevelService defectLevelService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    StructureService structureService;

    @Autowired
    private DefectTypeUsageService defectTypeUsageService;

    @Override
    public boolean deleteEntity(Long id) {
        DefectLevel param = new DefectLevel();
        param.setDefectTypeId(id);
        List<DefectLevel> list = defectLevelService.selectEntities(param);
        for (DefectLevel defectLevel : list) {
            defectLevelService.delete(defectLevel);
        }
        return this.deleteByPrimaryKey(id);
    }

    @Override
    public List<DefectType> findByDeviceId(Long id) {
        Device device = deviceService.selectByPrimaryKey(id);
        Long deviceTypeId = device.getDeviceTypeId();
        DeviceType deviceType = deviceTypeService.selectByPrimaryKey(deviceTypeId);
        return defectTypeList(DefectType.DEVICE_TYPE_DEVICE, deviceType.getCode());
    }

    @Override
    public List<DefectType> findByStructureId(Long id) {
        String typeCode = null;
        Structure structure = this.structureService.selectByPrimaryKey(id);
        if (structure != null) {
            typeCode = structure.getType();
        }
        return defectTypeList(DefectType.DEVICE_TYPE_STRUCTURE, typeCode);
    }

    private List<DefectType> defectTypeList(String type, String typeCode) {
        Map<String, String> param = MapUtils.newHashMap("type", type);
        param.put("typeCode", typeCode);
        return this.selectMapperList("com.stec.masterdata.mapper.wyl.WylDefectMapper.defectTypeList", param);
    }

    @Override
    public DefectType save(DefectType entity, List<DefectTypeUsage> usageList) {
        entity = save(entity);
        DefectTypeUsage usage = new DefectTypeUsage();
        usage.setDefectTypeId(entity.getId());
        defectTypeUsageService.delete(usage);
        for (DefectTypeUsage defectTypeUsage : usageList) {
            defectTypeUsage.setDefectTypeId(entity.getId());
        }
        defectTypeUsageService.save(usageList);
        return entity;
    }

    private List<Map<String, Object>> transToMaps(List<DefectType> list) {
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (DefectType entity : list) {
            try {
                Map<String, Object> retMap = MapUtils.toMapIgnoreNull(entity);
                retList.add(retMap);
                DefectLevel defectLevel = new DefectLevel();
                defectLevel.setDefectTypeId(entity.getId());
                List<DefectLevel> defectLevelList = defectLevelService.selectEntities(defectLevel);
                retMap.put("levelList", defectLevelList);
            } catch (Exception e) {
                return null;
            }
        }
        return retList;
    }

}
