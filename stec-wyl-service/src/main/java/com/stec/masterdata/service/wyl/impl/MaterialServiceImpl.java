package com.stec.masterdata.service.wyl.impl;


import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.Material;
import com.stec.masterdata.service.basic.DocInfoService;
import com.stec.masterdata.service.wyl.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 20:58
 */
@Service
public class MaterialServiceImpl extends AdvSqlDaoImpl<Material, String> implements MaterialService {
    @Autowired
    DocInfoService docInfoService;
    @Override
    public boolean deleteEntity(Long id) {
        Material materials=this.selectByPrimaryKey(id);
        docInfoService.deleteByPrimaryKey(materials.getDocId());
        return this.deleteByPrimaryKey(id);
    }

    @Override
    public void removePrice(Long materialId) throws DataServiceException {
        this.updateMapperMap("com.stec.masterdata.mapper.wyl.WorkOrderMapper.removePrice",materialId);
    }
}
