package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/11 0011
 * Time: 14:50
 */
@Entity
@Table(name = "defect_inspect_user")
public class DefectInspectUser extends MasterEntity<String> {

    private static final long serialVersionUID = -8304989021507409735L;

    private Long defectId;

    private Long userId;

    public Long getDefectId() {
        return defectId;
    }

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}