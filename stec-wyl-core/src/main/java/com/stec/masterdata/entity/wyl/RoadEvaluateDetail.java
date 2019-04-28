package com.stec.masterdata.entity.wyl;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:31
 */
@Entity
@Table(name = "road_evaluate_detail")
public class RoadEvaluateDetail extends MasterEntity<String> {
    private static final long serialVersionUID = -7043017241945736713L;

    private Long itemId;

    private Long reportId;

    private Date startDate;

    private Date endDate;

    private Double start;

    private Double end;

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

    @Transient
    @TableField(exist = false)
    private String lineCode;

    @Transient
    @TableField(exist = false)
    private Double rd;

    @Transient
    @TableField(exist = false)
    private Double iri;

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public Double getRd() {
        return rd;
    }

    public void setRd(Double rd) {
        this.rd = rd;
    }

    public Double getIri() {
        return iri;
    }

    public void setIri(Double iri) {
        this.iri = iri;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
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

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
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
}