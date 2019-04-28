package com.stec.wyl.web.wrapper;

import stec.framework.excel.annotation.ExcelField;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/7 0007
 * Time: 14:14
 */

/**
 * 平整度
 */
public class RoadWrapper {

    @ExcelField(title = "id")
    private Long id;

    @ExcelField(title = "date")
    private String date;
    @ExcelField(title = "line_no")
    private String lineNo;
    @ExcelField(title = "lane_no")
    private String laneNo;
    @ExcelField(title = "start_mileage")
    private Double start;
    @ExcelField(title = "end_mileage")
    private Double end;
    @ExcelField(title = "iri")
    private Double iri;
    @ExcelField(title = "rd")
    private Double rd;
    @ExcelField(title = "bpn")
    private Double bpn;
    @ExcelField(title = "l")
    private Double LO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLaneNo() {
        return laneNo;
    }

    public void setLaneNo(String laneNo) {
        this.laneNo = laneNo;
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

    public Double getIri() {
        return iri;
    }

    public void setIri(Double iri) {
        this.iri = iri;
    }

    public Double getRd() {
        return rd;
    }

    public void setRd(Double rd) {
        this.rd = rd;
    }

    public Double getBpn() {
        return bpn;
    }

    public void setBpn(Double bpn) {
        this.bpn = bpn;
    }

    public Double getLO() {
        return LO;
    }

    public void setLO(Double LO) {
        this.LO = LO;
    }
}
