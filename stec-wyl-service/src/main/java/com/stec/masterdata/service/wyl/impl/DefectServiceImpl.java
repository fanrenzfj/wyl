package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.Defect;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.WorkOrder;
import com.stec.masterdata.service.wyl.DefectService;
import com.stec.masterdata.service.wyl.DefectTypeService;
import com.stec.masterdata.service.wyl.WorkOrderService;
import com.stec.utils.DataTimeUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 14:13
 */
@Service
public class DefectServiceImpl extends AdvSqlDaoImpl<Defect, String> implements DefectService {

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private DefectTypeService defectTypeService;

    @Override
    public boolean matchStatus(Long defectId, String status) throws DataServiceException {
        Defect entity = selectByPrimaryKey(defectId);
        return StringUtils.equals(entity.getStatus(), status);
    }

    @Override
    public void createWorkOrder(Long defectId) throws DataServiceException {
        Defect entity = selectByPrimaryKey(defectId);
        WorkOrder workOrder = new WorkOrder();
        workOrder.setSource(WorkOrder.ORDER_SOURCE_DEFECT);
        workOrder.setCreateDate(TimeUtil.now());
        workOrder.setName(entity.getStructureName() + (StringUtils.isEmpty(entity.getDeviceName()) ? entity.getDeviceName() : "") + entity.getDefectTypeName());
        workOrder.setRemark(entity.getRemark());
        if(ObjectUtils.isNotNull(entity.getDefectTypeId())){
            DefectType defectType = defectTypeService.selectByPrimaryKey(entity.getDefectTypeId());
            workOrder.setType(defectType.getCode());
            workOrder.setTypeName(defectType.getName());
        }
        workOrder.setPlanStartDate(DataTimeUtils.ceilDate(workOrder.getCreateDate(), DataTimeUtils.DAY));
        workOrder.setPlanEndDate(new Date(workOrder.getPlanStartDate().getTime() + DataTimeUtils.ONE_DAY));
        List<OrderItem> orderItems = new ArrayList<>(1);
        List<List<Long>> deviceList = new ArrayList<>(1);

        OrderItem item = new OrderItem();
        item.setStructureId(entity.getStructureId());
        item.setStructureName(entity.getStructureName());
        item.setCreateDate(TimeUtil.now());
        orderItems.add(item);

        List<Long> deviceIds = new ArrayList<>(1);
        if (ObjectUtils.isNotNull(entity.getDeviceId())) {
            deviceIds.add(entity.getDeviceId());
        }
        deviceList.add(deviceIds);

        workOrder = workOrderService.save(workOrder, orderItems, deviceList);
        entity.setWorkOrderId(workOrder.getId());
        entity.setStatus(Defect.DEFECT_STATUS_PROCESS);
        save(entity);
    }

    @Override
    public void completeDefectFromOrder(Long orderId) throws DataServiceException {
        Defect entity = new Defect();
        entity.setWorkOrderId(orderId);
        entity = selectEntity(entity);
        if(ObjectUtils.isNotNull(entity)) {
            entity.setStatus(Defect.DEFECT_STATUS_COMPLETE);
            save(entity);
        }
    }
    @Override
    public void removeDeviceId(Long defectId) throws DataServiceException {
        this.updateMapperMap("com.stec.masterdata.mapper.wyl.WorkOrderMapper.removeDeviceId",defectId);
    }
}
