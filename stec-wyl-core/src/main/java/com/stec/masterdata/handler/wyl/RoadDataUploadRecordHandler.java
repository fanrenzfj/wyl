package com.stec.masterdata.handler.wyl;

import com.stec.framework.handler.IAdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.RoadDataUploadRecord;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhuangrui
 * Date: 2018/12/7
 * Time: 9:55
 */
public interface RoadDataUploadRecordHandler extends IAdvMySqlHandlerService<RoadDataUploadRecord, Long> {

    /**
     * 根据DataDate更新RoadDataUploadRecord
     *
     * @param roadDataUploadRecord
     */
    void updateByDataDate(RoadDataUploadRecord roadDataUploadRecord);
}
