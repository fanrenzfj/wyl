package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.RoadDataUploadRecord;
import com.stec.masterdata.handler.wyl.RoadDataUploadRecordHandler;
import com.stec.masterdata.service.wyl.RoadDataUploadRecordService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhuangrui
 * Date: 2018/12/7
 * Time: 9:56
 */
@Service
@Component
public class RoadDataUploadRecordHandlerImpl extends AdvMySqlHandlerService<RoadDataUploadRecordService, RoadDataUploadRecord, Long> implements RoadDataUploadRecordHandler {
    @Override
    public void updateByDataDate(RoadDataUploadRecord roadDataUploadRecord) {
        this.privateService.updateByDataDate(roadDataUploadRecord);
    }
}
