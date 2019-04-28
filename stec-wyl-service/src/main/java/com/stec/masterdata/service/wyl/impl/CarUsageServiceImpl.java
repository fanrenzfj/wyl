package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.CarUsage;
import com.stec.masterdata.service.wyl.CarUsageService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/30
 * Time: 15:53
 */
@Service
public class CarUsageServiceImpl extends AdvSqlDaoImpl<CarUsage, String> implements CarUsageService {
}
