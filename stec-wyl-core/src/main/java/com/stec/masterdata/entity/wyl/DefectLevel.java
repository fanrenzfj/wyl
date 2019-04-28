package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/27
 * Time: 11:23
 */
@Entity
@Table(name ="defect_level")
public class DefectLevel extends MasterEntity<String> {
    private static final long serialVersionUID = 5467420447340284751L;
    private String levelCode;
    private String remark;
    private Double  hours;
    private Long defectTypeId;
    private Long docId;

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Long getDefectTypeId() {
        return defectTypeId;
    }

    public void setDefectTypeId(Long defectTypeId) {
        this.defectTypeId = defectTypeId;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}