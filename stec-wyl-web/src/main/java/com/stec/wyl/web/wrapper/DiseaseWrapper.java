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
 * 病害
 */
public class DiseaseWrapper {

    @ExcelField(title = "id")
    private Long id;

    @ExcelField(title = "date")
    private String date;
    @ExcelField(title = "line_no")
    private String lineNo;
    @ExcelField(title = "lane_no")
    private String laneNo;
    @ExcelField(title = "dis")
    private Double dis;
    @ExcelField(title = "pnt")
    private Double pnt;
    @ExcelField(title = "catalog")
    private String catalog;
    @ExcelField(title = "name")
    private String name;
    @ExcelField(title = "severity")
    private String severity;
    @ExcelField(title = "location" )
    private String location;
    @ExcelField(title = "depth" )
    private Double depth;
    @ExcelField(title = "length" )
    private Double length;
    @ExcelField(title = "width" )
    private Double width;

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

    public Double getDis() {
        return dis;
    }

    public void setDis(Double dis) {
        this.dis = dis;
    }

    public Double getPnt() {
        return pnt;
    }

    public void setPnt(Double pnt) {
        this.pnt = pnt;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
}
