package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.BlockRoad;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 16:03
 */
public interface BlockRoadService extends IAdvMySqlService<BlockRoad, Long> {
    BlockRoad save(BlockRoad blockRoad, List<Long> blockRoadCarIds,List<Long> workOrderIds);
    boolean deleteEntity(Long id);
}
