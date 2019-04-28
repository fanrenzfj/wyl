package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.PlanItem;
import com.stec.masterdata.entity.wyl.PlanItemDevice;
import com.stec.masterdata.service.wyl.PlanItemDeviceService;
import com.stec.masterdata.service.wyl.PlanItemService;
import com.stec.utils.CollectionUtils;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:00
 */
@Service
public class PlanItemServiceImpl extends AdvSqlDaoImpl<PlanItem, String> implements PlanItemService {

    @Autowired
    private PlanItemDeviceService planItemDeviceService;

    @Override
    public void save(PlanItem entity, List<Long> deviceIds) {
        if(CollectionUtils.isEmpty(deviceIds)){
            entity.setRelatedDevice(false);
        }
        else {
            entity.setRelatedDevice(true);
        }
        if(ObjectUtils.isNull(entity.getId())) {
            entity = save(entity);
        }
        else {
            entity = save(entity);
            PlanItemDevice delParam = new PlanItemDevice();
            delParam.setPlanItemId(entity.getId());
            planItemDeviceService.delete(delParam);
        }
        if(CollectionUtils.isNotEmpty(deviceIds)){
            List<PlanItemDevice> saveList = new ArrayList<>();
            for (Long deviceId : deviceIds) {
                PlanItemDevice item = new PlanItemDevice();
                item.setPlanItemId(entity.getId());
                item.setDeviceId(deviceId);
                saveList.add(item);
            }
            planItemDeviceService.save(saveList);
        }
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        PlanItemDevice delParam = new PlanItemDevice();
        delParam.setPlanItemId(id);
        planItemDeviceService.delete(delParam);
        return super.deleteByPrimaryKey(id);
    }
}
