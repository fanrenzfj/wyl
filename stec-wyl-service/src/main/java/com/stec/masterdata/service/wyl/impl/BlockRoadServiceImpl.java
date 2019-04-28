package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.BlockRoad;
import com.stec.masterdata.entity.wyl.BlockRoadCar;
import com.stec.masterdata.entity.wyl.BlockRoadOrder;
import com.stec.masterdata.service.wyl.BlockRoadCarService;
import com.stec.masterdata.service.wyl.BlockRoadOrderService;
import com.stec.masterdata.service.wyl.BlockRoadService;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 16:03
 */
@Service
public class BlockRoadServiceImpl extends AdvSqlDaoImpl<BlockRoad, String> implements BlockRoadService {
    @Autowired
    BlockRoadCarService blockRoadCarService;
    @Autowired
    BlockRoadOrderService blockRoadOrderService;
    @Override
    public BlockRoad save(BlockRoad blockRoad, List<Long> blockRoadCarIds,List<Long> workOrderIds) {

        if (ObjectUtils.isNull(blockRoad.getId())) {
            blockRoad=this.save(blockRoad);
        } else {
            blockRoad=this.save(blockRoad);
            BlockRoadCar param = new BlockRoadCar();
            param.setBlockRoadId(blockRoad.getId());
            blockRoadCarService.delete(param);
            BlockRoadOrder blockRoadOrder=new BlockRoadOrder();
            blockRoadOrder.setBlockRoadId(blockRoad.getId());
            blockRoadOrderService.delete(blockRoadOrder);
        }
        if(blockRoadCarIds!=null)
        {
            List<BlockRoadCar> list=new ArrayList<>();
            for (Long blockRoadCarId : blockRoadCarIds) {
                BlockRoadCar blockRoadCar=new BlockRoadCar();
                blockRoadCar.setBlockRoadId(blockRoad.getId());
                blockRoadCar.setCarId(blockRoadCarId);
                list.add(blockRoadCar);
            }
            blockRoadCarService.save(list);
        }
        if(workOrderIds!=null)
        {
            List<BlockRoadOrder> blockRoadOrderList=new ArrayList<>();
            for (Long workOrderId : workOrderIds) {
                BlockRoadOrder blockRoadOrder=new BlockRoadOrder();
                blockRoadOrder.setBlockRoadId(blockRoad.getId());
                blockRoadOrder.setWorkOrderId(workOrderId);
                blockRoadOrderList.add(blockRoadOrder);
            }
            blockRoadOrderService.save(blockRoadOrderList);
        }
        return blockRoad;
    }

    @Override
    public boolean deleteEntity(Long id) {
        BlockRoadCar blockRoadCar=new BlockRoadCar();
        blockRoadCar.setBlockRoadId(id);
        blockRoadCarService.delete(blockRoadCar);
        BlockRoadOrder blockRoadOrder=new BlockRoadOrder();
        blockRoadOrder.setBlockRoadId(id);
        blockRoadOrderService.delete(blockRoadOrder);
        return this.deleteByPrimaryKey(id);
    }
}
