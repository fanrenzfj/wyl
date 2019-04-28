package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/24
 * Time: 10:58
 */
@Entity
@Table(name ="block_road_order")
public class BlockRoadOrder extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411247L;
    private Long blockRoadId;
    private Long workOrderId;

    public Long getBlockRoadId() {
        return blockRoadId;
    }

    public void setBlockRoadId(Long blockRoadId) {
        this.blockRoadId = blockRoadId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }
}