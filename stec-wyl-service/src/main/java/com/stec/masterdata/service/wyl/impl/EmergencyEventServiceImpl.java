package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.*;
import com.stec.masterdata.service.wyl.*;
import com.stec.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 15:27
 */
@Service
public class EmergencyEventServiceImpl extends AdvSqlDaoImpl<EmergencyEvent, String> implements EmergencyEventService {
    @Autowired
    private EmergencyEventWorkGroupService emergencyEventWorkGroupService;
    @Autowired
    private EmergencyEventCarService emergencyEventCarService;
    @Autowired
    private EmergencyEventMaterialService emergencyEventMaterialService;
    @Override
    public boolean matchProcess(Long id, String status) throws DataServiceException {
        EmergencyEvent entity = selectByPrimaryKey(id);
        return StringUtils.equals(entity.getStatus(), status);
    }

    @Override
    public EmergencyEvent handle(Long id, List<EmergencyEventMaterial> materialIds, List<Long> workGroupIds, List<Long> carIds) {
        List<EmergencyEventMaterial> materialList=new ArrayList<>();
        if(materialIds!=null)
        {
            for (EmergencyEventMaterial material : materialIds) {
                EmergencyEventMaterial emergencyEventMaterial=new EmergencyEventMaterial();
                emergencyEventMaterial.setEmergencyEventId(id);
                emergencyEventMaterial.setMaterialId(material.getMaterialId());
                emergencyEventMaterial.setAmount(material.getAmount());
                emergencyEventMaterial.setUnit(material.getUnit());
                materialList.add(emergencyEventMaterial);
            }
            emergencyEventMaterialService.save(materialList);
        }

        List<EmergencyEventWorkGroup> workGroupList=new ArrayList<>();
        if(workGroupIds!=null)
        {
            for (Long workGroupId : workGroupIds) {
                EmergencyEventWorkGroup emergencyEventWorkGroup=new EmergencyEventWorkGroup();
                emergencyEventWorkGroup.setEmergencyEventId(id);
                emergencyEventWorkGroup.setWorkGroupId(workGroupId);
                workGroupList.add(emergencyEventWorkGroup);
            }
            emergencyEventWorkGroupService.save(workGroupList);
        }

        List<EmergencyEventCar> carList=new ArrayList<>();
        if(carIds!=null)
        {
            for (Long carId : carIds) {
                EmergencyEventCar emergencyEventCar=new EmergencyEventCar();
                emergencyEventCar.setEmergencyEventId(id);
                emergencyEventCar.setCarId(carId);
                carList.add(emergencyEventCar);
            }
            emergencyEventCarService.save(carList);
        }
        EmergencyEvent emergencyEvent=new EmergencyEvent();
        emergencyEvent.setId(id);
        emergencyEvent.setStatus(EmergencyEvent.EMERGENCY_EVENT_STATUS_ASSESSMENT);
        return this.save(emergencyEvent);
    }
}
