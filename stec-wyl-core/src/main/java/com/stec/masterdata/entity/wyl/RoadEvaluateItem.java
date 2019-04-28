package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterTreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:21
 */
@Entity
@Table(name = "road_evaluate_item")
public class RoadEvaluateItem extends MasterTreeEntity<String> {
    
    private static final long serialVersionUID = 3171940723953110567L;

    private Long templateId;

    private Long reportId;
    
    private Date startDate;

    private Date endDate;

    private Double rqi;

    private Double rdi;

    private Double pci;

    private Double sri;

    private Double pssi;

    private Double pqi;

    private Double sci;

    private Double mqi;

    private String rqiLevel;

    private String rdiLevel;

    private String pciLevel;

    private String sriLevel;

    private String pssiLevel;

    private String pqiLevel;

    private String sciLevel;

    private String mqiLevel;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Double getRqi() {
        return rqi;
    }

    public void setRqi(Double rqi) {
        this.rqi = rqi;
    }

    public Double getRdi() {
        return rdi;
    }

    public void setRdi(Double rdi) {
        this.rdi = rdi;
    }

    public Double getPci() {
        return pci;
    }

    public void setPci(Double pci) {
        this.pci = pci;
    }

    public Double getSri() {
        return sri;
    }

    public void setSri(Double sri) {
        this.sri = sri;
    }

    public Double getPssi() {
        return pssi;
    }

    public void setPssi(Double pssi) {
        this.pssi = pssi;
    }

    public Double getPqi() {
        return pqi;
    }

    public void setPqi(Double pqi) {
        this.pqi = pqi;
    }

    public Double getSci() {
        return sci;
    }

    public void setSci(Double sci) {
        this.sci = sci;
    }

    public Double getMqi() {
        return mqi;
    }

    public void setMqi(Double mqi) {
        this.mqi = mqi;
    }

    public String getRqiLevel() {
        return rqiLevel;
    }

    public void setRqiLevel(String rqiLevel) {
        this.rqiLevel = rqiLevel;
    }

    public String getRdiLevel() {
        return rdiLevel;
    }

    public void setRdiLevel(String rdiLevel) {
        this.rdiLevel = rdiLevel;
    }

    public String getPciLevel() {
        return pciLevel;
    }

    public void setPciLevel(String pciLevel) {
        this.pciLevel = pciLevel;
    }

    public String getSriLevel() {
        return sriLevel;
    }

    public void setSriLevel(String sriLevel) {
        this.sriLevel = sriLevel;
    }

    public String getPssiLevel() {
        return pssiLevel;
    }

    public void setPssiLevel(String pssiLevel) {
        this.pssiLevel = pssiLevel;
    }

    public String getPqiLevel() {
        return pqiLevel;
    }

    public void setPqiLevel(String pqiLevel) {
        this.pqiLevel = pqiLevel;
    }

    public String getSciLevel() {
        return sciLevel;
    }

    public void setSciLevel(String sciLevel) {
        this.sciLevel = sciLevel;
    }

    public String getMqiLevel() {
        return mqiLevel;
    }

    public void setMqiLevel(String mqiLevel) {
        this.mqiLevel = mqiLevel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}