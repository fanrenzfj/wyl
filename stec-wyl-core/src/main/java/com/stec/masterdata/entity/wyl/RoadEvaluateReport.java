package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:19
 */
@Entity
@Table(name = "road_evaluate_report")
public class RoadEvaluateReport extends MasterEntity<String> {
    private static final long serialVersionUID = -6892763346695146682L;
    //code为任务编号

    private Date startDate;

    private Date endDate;

    private Boolean download;

    //评估报告id
    private Long docId;

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

    public Boolean getDownload() {
        return download;
    }

    public void setDownload(Boolean download) {
        this.download = download;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}