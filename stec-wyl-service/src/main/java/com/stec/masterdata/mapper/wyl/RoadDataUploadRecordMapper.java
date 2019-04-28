package com.stec.masterdata.mapper.wyl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stec.masterdata.entity.wyl.RoadDataUploadRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhuangrui
 * Date: 2018/12/7
 * Time: 10:00
 */
@Mapper
public interface RoadDataUploadRecordMapper extends BaseMapper<RoadDataUploadRecord> {

    void updateByDataDate(RoadDataUploadRecord roadDataUploadRecord);

}
