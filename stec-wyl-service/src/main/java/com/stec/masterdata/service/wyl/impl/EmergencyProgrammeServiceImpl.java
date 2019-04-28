package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.EmergencyProgramme;
import com.stec.masterdata.service.wyl.EmergencyProgrammeService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 17:34
 */
@Service
public class EmergencyProgrammeServiceImpl extends AdvSqlDaoImpl<EmergencyProgramme, String> implements EmergencyProgrammeService {
}
