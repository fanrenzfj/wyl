package com.stec.masterdata.entity.wyl;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with STEC METADATA DESIGN.
 *
 * 评估报告 / 评估报告
 *
 * @author 谢娇
 * Date: 2019/04/16
 * Time: 10:30
 */
@Entity
@Table(name = "evaluate_report")
public class EvaluateReport extends MasterEntity<String> {

    private static final long serialVersionUID = 101L;

    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_EVALUATE_TEMPLATE_ID = "evaluate_template_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_EVALUATE_DATE = "evaluate_date";

    public static final String ATTRIBUTE_PROJECT_ID = "projectId";
    public static final String ATTRIBUTE_EVALUATE_TEMPLATE_ID = "evaluateTemplateId";
    public static final String ATTRIBUTE_START_DATE = "startDate";
    public static final String ATTRIBUTE_END_DATE = "endDate";
    public static final String ATTRIBUTE_STATUS = "status";
    public static final String ATTRIBUTE_EVALUATE_DATE = "evaluateDate";

    /**
     * 项目ID by 谢娇 on 2019/04/16 10:31
     * 项目ID
     * 项目ID
     */
    @Column(name = "project_id")
    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * 评估模板ID by 谢娇 on 2019/04/16 10:57
     * 评估模板ID
     * 评估模板ID，只关联评估模板的根节点
     */
    @Column(name = "evaluate_template_id")
    private Long evaluateTemplateId;

    public Long getEvaluateTemplateId() {
        return evaluateTemplateId;
    }

    public void setEvaluateTemplateId(Long evaluateTemplateId) {
        this.evaluateTemplateId = evaluateTemplateId;
    }

    /**
     * 开始时间 by 谢娇 on 2019/04/16 10:46
     * 开始时间
     * 开始时间
     */
    @Column(name = "start_date")
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 结束时间 by 谢娇 on 2019/04/16 10:46
     * 结束时间
     * 结束时间
     */
    @Column(name = "end_date")
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 状态 by 谢娇 on 2019/04/16 10:56
     * 状态
     * 状态：true：已从上大完成下载，false：未下载
     */
    @Column(name = "status")
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 评估时间 by 谢娇 on 2019/04/16 10:58
     * 评估时间
     * 评估时间
     */
    @Column(name = "evaluate_date")
    private Date evaluateDate;

    public Date getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(Date evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    @Override
    public String toString() {
        return "EvaluateReport{" +
                "projectId=" + projectId +
                ", evaluateTemplateId=" + evaluateTemplateId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", evaluateDate=" + evaluateDate +
                '}';
    }
}