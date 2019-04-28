package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/21 0021
 * Time: 8:38
 */
@Entity
@Table(name = "work_order")
public class WorkOrder extends MasterEntity<String> {
    private static final long serialVersionUID = 84981498422280595L;

    //计划工单
    public static final String ORDER_SOURCE_PLAN = "plan_type";

    //缺陷工单
    public static final String ORDER_SOURCE_DEFECT = "defect_type";

    public static final String ORDER_EMERGENCY_SUDDEN = "emergency_type";

    //临时工单
    public static final String ORDER_SOURCE_TEMPORARY = "temporary_type";

    //创建  待分配
    public static final String ORDER_PROCESS_CREATE = "0";
    //已分配 待执行
    public static final String ORDER_PROCESS_ASSIGN = "10";
    //已执行 待验收
    public static final String ORDER_PROCESS_COMPLETE = "20";
    //已验收 已完成
    public static final String ORDER_PROCESS_CONFIRM = "30";
    //已结算工单
    public static final String ORDER_PROCESS_AMOUNT = "40";

    private Long projectId;

    private String source;

    private String type;

    private String typeName;

    private Long workPlanId;

    private Long createUserId;

    private Date createDate;

    private Long assignUserId;

    private Date assignDate;

    private Long workGroupId;

    private String workGroupName;

    private String remark;

    private Date planStartDate;

    private Date planEndDate;

    private Date realStartDate;

    private Date realEndDate;

    private String process;

    private Long confirmUserId;

    private Date confirmDate;

    private String confirmMsg;

    private Double hours;

    private Long amountUserId;

    private Date amountDate;

    private Double amount;

    private Long docId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getWorkPlanId() {
        return workPlanId;
    }

    public void setWorkPlanId(Long workPlanId) {
        this.workPlanId = workPlanId;
    }

    public Long getWorkGroupId() {
        return workGroupId;
    }

    public void setWorkGroupId(Long workGroupId) {
        this.workGroupId = workGroupId;
    }

    public String getWorkGroupName() {
        return workGroupName;
    }

    public void setWorkGroupName(String workGroupName) {
        this.workGroupName = workGroupName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(Date realStartDate) {
        this.realStartDate = realStartDate;
    }

    public Date getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(Date realEndDate) {
        this.realEndDate = realEndDate;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Long assignUserId) {
        this.assignUserId = assignUserId;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmMsg() {
        return confirmMsg;
    }

    public void setConfirmMsg(String confirmMsg) {
        this.confirmMsg = confirmMsg;
    }

    public Long getAmountUserId() {
        return amountUserId;
    }

    public void setAmountUserId(Long amountUserId) {
        this.amountUserId = amountUserId;
    }

    public Date getAmountDate() {
        return amountDate;
    }

    public void setAmountDate(Date amountDate) {
        this.amountDate = amountDate;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}