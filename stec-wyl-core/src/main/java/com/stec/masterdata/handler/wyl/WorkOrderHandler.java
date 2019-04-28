package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.wyl.OrderItem;
import com.stec.masterdata.entity.wyl.OrderMaterial;
import com.stec.masterdata.entity.wyl.WorkOrder;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 9:56
 */
public interface WorkOrderHandler extends IAdvMySqlHandlerService<WorkOrder, Long> {

    WorkOrder save(WorkOrder entity, List<OrderItem> items, List<List<Long>> deviceLists) throws DataServiceException;

    void assign(WorkOrder entity, List<Long> carList) throws DataServiceException;

    boolean matchProcess(Long id, String process) throws DataServiceException;

    List<OrderMaterial> addMaterials(Long id, List<OrderMaterial> materials) throws DataServiceException;

    Map<String, Object> countBySource(Map<String,Object> map) throws DataServiceException;

    Map<String, Object> countByProcess(Map<String,Object> map) throws DataServiceException;

    /**
     * 本周工单完成率
     * @return
     * @throws DataServiceException
     */
    Map<String, Object> weekOrderComplete() throws DataServiceException;

}
