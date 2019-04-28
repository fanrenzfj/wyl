package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.EmergencyEventCar;
import com.stec.masterdata.service.wyl.EmergencyEventCarService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 9:08
 */
@Service
public class EmergencyEventCarServiceImpl extends AdvSqlDaoImpl<EmergencyEventCar, String> implements EmergencyEventCarService {
}
