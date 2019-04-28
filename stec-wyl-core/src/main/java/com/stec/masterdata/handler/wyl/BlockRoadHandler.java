package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.BlockRoad;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 16:02
 */
public interface BlockRoadHandler extends IAdvMySqlHandlerService<BlockRoad, Long> {
    BlockRoad save(BlockRoad blockRoad, List<Long> blockRoadCarIds,List<Long> workOrderIds);
    boolean deleteEntity(Long id);
}
