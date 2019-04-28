package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.EmergencyEventMaterial;
import com.stec.masterdata.service.wyl.EmergencyEventMaterialService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 9:02
 */
@Service
public class EmergencyEventMaterialServiceImpl extends AdvSqlDaoImpl<EmergencyEventMaterial, String> implements EmergencyEventMaterialService {
}
