package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.OrderMaterial;
import com.stec.masterdata.service.wyl.OrderMaterialService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 19:47
 */
@Service
public class OrderMaterialServiceImpl extends AdvSqlDaoImpl<OrderMaterial, String> implements OrderMaterialService {
}
