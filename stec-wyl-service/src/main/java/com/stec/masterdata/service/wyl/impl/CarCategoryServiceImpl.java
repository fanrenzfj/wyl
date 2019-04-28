package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.CarCategory;
import com.stec.masterdata.service.wyl.CarCategoryService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/30
 * Time: 15:54
 */
@Service
public class CarCategoryServiceImpl extends AdvSqlDaoImpl<CarCategory, String> implements CarCategoryService {
}
