package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.Traction;
import com.stec.masterdata.service.wyl.TractionService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 18:49
 */
@Service
public class TractionServiceImpl extends AdvSqlDaoImpl<Traction, String> implements TractionService {
}
