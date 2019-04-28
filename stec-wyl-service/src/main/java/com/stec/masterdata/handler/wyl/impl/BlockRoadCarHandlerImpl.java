package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.BlockRoadCar;
import com.stec.masterdata.handler.wyl.BlockRoadCarHandler;
import com.stec.masterdata.service.wyl.BlockRoadCarService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/23
 * Time: 16:05
 */
@Service
@Component
public class BlockRoadCarHandlerImpl extends AdvMySqlHandlerService<BlockRoadCarService, BlockRoadCar, Long> implements BlockRoadCarHandler {
}
