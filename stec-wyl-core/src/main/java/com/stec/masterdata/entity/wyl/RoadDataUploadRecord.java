package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * 道路分析数据的上传记录
 *
 * @author zhuangrui
 * Date: 2018/12/7
 * Time: 9:32
 */
@Entity
@Table(name = "road_data_upload_record")
public class RoadDataUploadRecord extends MasterEntity<String> {
    private static final long serialVersionUID = 929314603063922639L;

    private String code;

    //平整度是否上传
    private Boolean iriRecord;

    //病害记录是否上传
    private Boolean diseaseRecord;

    //是否已经发送去分析，一旦发送分析，数据将不可更改。
    private Boolean sendToAnalysis;

    //是否已经发送去分析，一旦发送分析，数据将不可更改。
    private Boolean startAnalysis;

    //数据采集时间
    private Date dataTime;

    //iri数据上传时间
    private Date iriDataUploadTime;
    //病害数据上传时间
    private Date diseaseDataUploadTime;

    //数据发送给上大的分析时间
    private Date dataAnalysisTime;

    private Long roadEvaluateReportId;

    public Long getRoadEvaluateReportId() {
        return roadEvaluateReportId;
    }

    public void setRoadEvaluateReportId(Long roadEvaluateReportId) {
        this.roadEvaluateReportId = roadEvaluateReportId;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIriRecord() {
        return iriRecord;
    }

    public void setIriRecord(Boolean iriRecord) {
        this.iriRecord = iriRecord;
    }

    public Boolean getDiseaseRecord() {
        return diseaseRecord;
    }

    public void setDiseaseRecord(Boolean diseaseRecord) {
        this.diseaseRecord = diseaseRecord;
    }

    public Boolean getSendToAnalysis() {
        return sendToAnalysis;
    }

    public void setSendToAnalysis(Boolean sendToAnalysis) {
        this.sendToAnalysis = sendToAnalysis;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Date getIriDataUploadTime() {
        return iriDataUploadTime;
    }

    public void setIriDataUploadTime(Date iriDataUploadTime) {
        this.iriDataUploadTime = iriDataUploadTime;
    }

    public Date getDiseaseDataUploadTime() {
        return diseaseDataUploadTime;
    }

    public void setDiseaseDataUploadTime(Date diseaseDataUploadTime) {
        this.diseaseDataUploadTime = diseaseDataUploadTime;
    }

    public Date getDataAnalysisTime() {
        return dataAnalysisTime;
    }

    public void setDataAnalysisTime(Date dataAnalysisTime) {
        this.dataAnalysisTime = dataAnalysisTime;
    }

    public Boolean getStartAnalysis() {
        return startAnalysis;
    }

    public void setStartAnalysis(Boolean startAnalysis) {
        this.startAnalysis = startAnalysis;
    }
}