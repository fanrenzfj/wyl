package com.stec.masterdata.entity.wyl;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stec.masterdata.entity.common.MasterTreeEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/16
 * Time: 11:20
 *
 * 评估报告项 
 * 评估报告项，将模板结构保存到报告中，以此来支持模板版本管理
 */
@Entity
@Table(name = "evaluate_report_item")
public class EvaluateReportItem extends MasterTreeEntity<String> {

    private static final long serialVersionUID = 102L;

    public static final String COLUMN_EVALUATE_REPORT_ID = "evaluate_report_id";
    public static final String COLUMN_EVALUATE_TEMPLATE_ID = "evaluate_template_id";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_STRUCTURE_ID = "structure_id";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_EVALUATE_METHOD = "evaluate_method";
    public static final String COLUMN_TYPE = "type";

    public static final String ATTRIBUTE_EVALUATE_REPORT_ID = "evaluateReportId";
    public static final String ATTRIBUTE_EVALUATE_TEMPLATE_ID = "evaluateTemplateId";
    public static final String ATTRIBUTE_VALUE = "value";
    public static final String ATTRIBUTE_LEVEL = "level";
    public static final String ATTRIBUTE_STRUCTURE_ID = "structureId";
    public static final String ATTRIBUTE_REMARK = "remark";
    public static final String ATTRIBUTE_TAG = "tag";
    public static final String ATTRIBUTE_START_DATE = "startDate";
    public static final String ATTRIBUTE_END_DATE = "endDate";
    public static final String ATTRIBUTE_WEIGHT = "weight";
    public static final String ATTRIBUTE_EVALUATE_METHOD = "evaluateMethod";
    public static final String ATTRIBUTE_TYPE = "type";

    /**
     * 评估报告ID by 谢娇 on 2019/04/16 11:08
     * 评估报告项
     * 评估报告项
     */
    @Column(name = "evaluate_report_id")
    private Long evaluateReportId;

    public Long getEvaluateReportId() {
        return evaluateReportId;
    }

    public void setEvaluateReportId(Long evaluateReportId) {
        this.evaluateReportId = evaluateReportId;
    }

    /**
     * 评估项ID by 谢娇 on 2019/04/16 11:14
     * 评估项ID
     * 关联评估模板中的具体评估项
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
     * 评分结果 by 谢娇 on 2019/04/16 11:17
     * 评分结果
     * 评分结果
     */
    @Column(name = "value")
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * 评估等级 by 谢娇 on 2019/04/16 11:18
     * 评估等级
     * 评估等级
     */
    @Column(name = "level")
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 结构ID by 谢娇 on 2019/04/16 11:19
     * 结构ID
     * 结构ID
     */
    @Column(name = "structure_id")
    private Long structureId;

    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {
        this.structureId = structureId;
    }

    /**
     * 备注 by 谢娇 on 2019/04/16 11:19
     * 备注
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 评估标签 by 谢娇 on 2019/04/16 14:15
     * 评估标签
     * 评估标签
     */
    @Column(name = "tag")
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 开始时间 by 谢娇 on 2019/04/17 10:03
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
     * 结束时间 by 谢娇 on 2019/04/17 10:03
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
     * 权重 by 谢娇 on 2019/04/17 10:04
     * 权重
     * 权重
     */
    @Column(name = "weight")
    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 评估方法 by 谢娇 on 2019/04/17 10:05
     * 评估方法
     * 评估方法
     */
    @Column(name = "evaluate_method")
    private String evaluateMethod;

    public String getEvaluateMethod() {
        return evaluateMethod;
    }

    public void setEvaluateMethod(String evaluateMethod) {
        this.evaluateMethod = evaluateMethod;
    }

    /**
     * 类型 by 谢娇 on 2019/04/17 10:05
     * 类型
     * 类型
     */
    @Column(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}