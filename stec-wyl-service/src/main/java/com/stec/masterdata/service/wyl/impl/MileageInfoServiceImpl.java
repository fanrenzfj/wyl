package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.MileageInfo;
import com.stec.masterdata.service.wyl.MileageInfoService;
import org.springframework.stereotype.Service;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/24
 * Time: 13:57
 */
@Service
public class MileageInfoServiceImpl extends AdvSqlDaoImpl<MileageInfo, String> implements MileageInfoService {
}
