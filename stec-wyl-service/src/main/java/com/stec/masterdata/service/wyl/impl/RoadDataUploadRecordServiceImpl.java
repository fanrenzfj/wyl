package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.RoadDataUploadRecord;
import com.stec.masterdata.mapper.wyl.RoadDataUploadRecordMapper;
import com.stec.masterdata.service.wyl.RoadDataUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhuangrui
 * Date: 2018/12/7
 * Time: 9:44
 */
@Service
public class RoadDataUploadRecordServiceImpl extends AdvSqlDaoImpl<RoadDataUploadRecord, String> implements RoadDataUploadRecordService {

    @Autowired
    private RoadDataUploadRecordMapper roadDataUploadRecordMapper;


    @Override
    public void updateByDataDate(RoadDataUploadRecord roadDataUploadRecord) {
        roadDataUploadRecordMapper.updateByDataDate(roadDataUploadRecord);
    }
}
