package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.BlockRoad;
import com.stec.masterdata.handler.wyl.BlockRoadHandler;
import com.stec.masterdata.service.wyl.BlockRoadService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 16:03
 */
@Service
@Component
public class BlockRoadHandlerImpl extends AdvMySqlHandlerService<BlockRoadService, BlockRoad, Long> implements BlockRoadHandler {
    @Override
    public BlockRoad save(BlockRoad blockRoad, List<Long> blockRoadCarIds,List<Long> workOrderIds) {
        return this.privateService.save(blockRoad,blockRoadCarIds,workOrderIds);
    }
    @Override
    public boolean deleteEntity(Long id) {
        return this.privateService.deleteEntity(id);
    }
}
