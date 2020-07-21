package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 检验报告实体
 */
public class SurveyReportEntity extends BaseEntity {
    private Map attrMap;
    private String batchCode;
    private BaseIntIdNameEntity busiTypeId;
    private BaseIntIdNameEntity checkDeptId;
    private BaseIdValueEntity checkResOption;
    public BaseIntIdNameEntity checkStaffId;
    private String checkResult;
    private Long checkTime;
    private Long cid;
    private BaseIntIdNameEntity createStaff;
    private Long createTime;
    private Long id;
    public PendingEntity pending;
    private SampleIdEntity sampleId;
    private InspectIdEntity inspectId;
    private ProdIdEntity prodId;
    private Integer status;
    private StdVerIdEntity stdVerId;
    private Long tableInfoId;
    private String tableNo;
    private Boolean unQlfDealFlag;
    private String testResult;
    private Boolean valid;
    private Integer version;
    public String memoField;
    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BaseIntIdNameEntity getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(BaseIntIdNameEntity busiTypeId) {
        this.busiTypeId = busiTypeId;
    }

    public BaseIntIdNameEntity getCheckDeptId() {
        return checkDeptId;
    }

    public void setCheckDeptId(BaseIntIdNameEntity checkDeptId) {
        this.checkDeptId = checkDeptId;
    }

    public BaseIdValueEntity getCheckResOption() {
        return checkResOption;
    }

    public void setCheckResOption(BaseIdValueEntity checkResOption) {
        this.checkResOption = checkResOption;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public BaseIntIdNameEntity getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(BaseIntIdNameEntity createStaff) {
        this.createStaff = createStaff;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public SampleIdEntity getSampleId() {
        return sampleId;
    }

    public void setSampleId(SampleIdEntity sampleId) {
        this.sampleId = sampleId;
    }

    public InspectIdEntity getInspectId() {
        return inspectId;
    }

    public void setInspectId(InspectIdEntity inspectId) {
        this.inspectId = inspectId;
    }

    public ProdIdEntity getProdId() {
        return prodId;
    }

    public void setProdId(ProdIdEntity prodId) {
        this.prodId = prodId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public Boolean getUnQlfDealFlag() {
        return unQlfDealFlag;
    }

    public void setUnQlfDealFlag(Boolean unQlfDealFlag) {
        this.unQlfDealFlag = unQlfDealFlag;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
