package com.stec.masterdata.service.wyl;

import com.stec.framework.service.IAdvMySqlService;
import com.stec.masterdata.entity.wyl.RoadDataUploadRecord;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhuangrui
 * Date: 2018/12/7
 * Time: 9:44
 */
public interface RoadDataUploadRecordService extends IAdvMySqlService<RoadDataUploadRecord, Long> {

    /**
     * 根据DataDate更新RoadDataUploadRecord
     *
     * @param roadDataUploadRecord
     */
    void updateByDataDate(RoadDataUploadRecord roadDataUploadRecord);


}
