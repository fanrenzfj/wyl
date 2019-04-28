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
@Table(name ="defect_type")
public class DefectType extends MasterEntity<String> {
    private static final long serialVersionUID = 8525690085883316218L;

    public static final String DEVICE_TYPE_DEVICE = "device";

    public static final String DEVICE_TYPE_STRUCTURE = "structure";

    private String type;

//    private String typeCode;
//
//    private String typeName;

    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getTypeCode() {
//        return typeCode;
//    }
//
//    public void setTypeCode(String typeCode) {
//        this.typeCode = typeCode;
//    }
//
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(String typeName) {
//        this.typeName = typeName;
//    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}