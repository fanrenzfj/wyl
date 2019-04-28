package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/17 0017
 * Time: 15:55
 */
@Entity
@Table(name = "defect_type_usage")
public class DefectTypeUsage extends MasterEntity<String> {
    
    private static final long serialVersionUID = 4282725493906829718L;

    private Long defectTypeId;

    public Long getDefectTypeId() {
        return defectTypeId;
    }

    public void setDefectTypeId(Long defectTypeId) {
        this.defectTypeId = defectTypeId;
    }
}