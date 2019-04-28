package com.stec.masterdata.entity.wyl;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stec.masterdata.entity.common.MasterTreeEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with STEC METADATA DESIGN.
 *
 * 评估模板 / 评估模板
 *
 * @author 谢娇
 * Date: 2019/04/15
 * Time: 20:10
 */
@Entity
@Table(name = "evaluate_template")
public class EvaluateTemplate extends MasterTreeEntity<String> {

    private static final long serialVersionUID = 89L;

    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_STRUCTURE_ID = "structure_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_EVALUATE_METHOD = "evaluate_method";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_TAG = "tag";

    public static final String ATTRIBUTE_PROJECT_ID = "projectId";
    public static final String ATTRIBUTE_STRUCTURE_ID = "structureId";
    public static final String ATTRIBUTE_WEIGHT = "weight";
    public static final String ATTRIBUTE_TYPE = "type";
    public static final String ATTRIBUTE_EVALUATE_METHOD = "evaluateMethod";
    public static final String ATTRIBUTE_REMARK = "remark";
    public static final String ATTRIBUTE_TAG = "tag";

    /**
     * 项目ID by 谢娇 on 2019/04/15 20:14
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
     * 结构ID by 谢娇 on 2019/04/15 20:16
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
     * 权重 by 谢娇 on 2019/04/16 10:04
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
     * 类型 by 谢娇 on 2019/04/16 10:04
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

    /**
     * 评估算法 by 谢娇 on 2019/04/16 10:05
     * 评估算法
     * 评估算法
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
     * 备注 by 谢娇 on 2019/04/16 10:05
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
     * 评估标签 by 谢娇 on 2019/04/16 14:13
     * 评估标签
     * 评估标签：
     */
    @Column(name = "tag")
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "EvaluateTemplate{" +
                "projectId=" + projectId +
                ", structureId=" + structureId +
                ", weight=" + weight +
                ", type='" + type + '\'' +
                ", evaluateMethod='" + evaluateMethod + '\'' +
                ", remark='" + remark + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}