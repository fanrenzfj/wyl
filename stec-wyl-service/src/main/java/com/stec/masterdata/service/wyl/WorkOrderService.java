package com.stec.masterdata.service.wyl;

import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderMaterial;
import com.stec.masterdata.entity.wyl.WorkOrder;
import com.stec.masterdata.entity.wyl.WorkPlan;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:03
 */
public interface WorkOrderService extends IAdvMySqlService<WorkOrder, Long> {

    WorkOrder save(WorkOrder entity, List<OrderItem> items, List<List<Long>> deviceLists) throws DataServiceException;

    void assign(WorkOrder entity, List<Long> carList) throws DataServiceException;

    boolean matchProcess(Long id, String process) throws DataServiceException;

    List<OrderMaterial> addMaterials(Long id, List<OrderMaterial> materials) throws DataServiceException;

    /**
     * 根据计划产生工单
     * @param workPlan
     * @return
     * @throws DataServiceException
     */
    WorkOrder createFromWorkPlan(WorkPlan workPlan) throws DataServiceException;

    Map<String, Object> countBySource(Map<String, Object> map) throws DataServiceException;

    Map<String, Object> countByProcess(Map<String, Object> map) throws DataServiceException;

}
