package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.OrderMaterial;
import com.stec.masterdata.handler.wyl.OrderMaterialHandler;
import com.stec.masterdata.service.wyl.OrderMaterialService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 19:47
 */
@Service
@Component
public class OrderMaterialHandlerImpl extends AdvMySqlHandlerService<OrderMaterialService, OrderMaterial, Long> implements OrderMaterialHandler {
}
