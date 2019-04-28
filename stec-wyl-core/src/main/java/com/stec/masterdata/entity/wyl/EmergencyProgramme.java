package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/29
 * Time: 16:32
 */
@Entity
@Table(name ="emergency_programme")
public class EmergencyProgramme extends MasterEntity<String> {
    private static final long serialVersionUID = 1066458283525411244L;
    private Date programmeTime;
    private String type;
    private String level;
    private Long pdfDocId;
    private Long mpDocId;

    public Date getProgrammeTime() {
        return programmeTime;
    }

    public void setProgrammeTime(Date programmeTime) {
        this.programmeTime = programmeTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getPdfDocId() {
        return pdfDocId;
    }

    public void setPdfDocId(Long pdfDocId) {
        this.pdfDocId = pdfDocId;
    }

    public Long getMpDocId() {
        return mpDocId;
    }

    public void setMpDocId(Long mpDocId) {
        this.mpDocId = mpDocId;
    }
}