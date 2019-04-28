package com.stec.masterdata.service.wyl.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.AnchorPoint;
import com.stec.masterdata.service.wyl.AnchorPointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 19:32
 */
@Service
public class AnchorPointServiceImpl extends AdvSqlDaoImpl<AnchorPoint, String> implements AnchorPointService {
    @Override
    public List<AnchorPoint> queryPoints(String tag, Double startMileage, Double endMileage) throws ServiceException {

        AnchorPoint entity = new AnchorPoint();
        entity.setTag(tag);
        EntityWrapper<AnchorPoint> wrapper = new EntityWrapper<>(entity);
        wrapper.orderBy("mileage", startMileage < endMileage);
        wrapper.ge("mileage", Math.min(startMileage, endMileage));
        wrapper.le("mileage", Math.max(startMileage, endMileage));
        return this.selectEntities(wrapper);
    }
}
