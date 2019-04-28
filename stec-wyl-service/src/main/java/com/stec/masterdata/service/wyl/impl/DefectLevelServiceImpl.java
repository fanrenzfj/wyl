package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.DefectLevel;
import com.stec.masterdata.service.wyl.DefectLevelService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 13:32
 */
@Service
public class DefectLevelServiceImpl extends AdvSqlDaoImpl<DefectLevel, String> implements DefectLevelService {
}
