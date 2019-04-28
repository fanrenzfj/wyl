package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.OrderItemRecord;
import com.stec.masterdata.service.wyl.OrderItemRecordService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 10:08
 */
@Service
public class OrderItemRecordServiceImpl extends AdvSqlDaoImpl<OrderItemRecord, String> implements OrderItemRecordService {
}
