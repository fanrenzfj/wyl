package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.BlockRoadOrder;
import com.stec.masterdata.handler.wyl.BlockRoadOrderHandler;
import com.stec.masterdata.service.wyl.BlockRoadOrderService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/24
 * Time: 11:01
 */
@Service
@Component
public class BlockRoadOrderHandlerImpl extends AdvMySqlHandlerService<BlockRoadOrderService, BlockRoadOrder, Long> implements BlockRoadOrderHandler {
}
