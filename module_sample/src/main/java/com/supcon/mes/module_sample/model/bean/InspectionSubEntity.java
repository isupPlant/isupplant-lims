package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class InspectionSubEntity extends BaseEntity {
    private BaseLongIdNameEntity comId;
    private String comName;
    private BaseIdValueEntity comState;
    private InspectionSubEntity dispMap;
    private String dispValue;
    private Long id;
    private Boolean isReport;
    private String originValue;
    private Integer parallelNo;
    private String reportName;
    private String roundValue;
    private BaseLongIdNameEntity sampleId;
    private SampleTestIdEntity sampleTestId;
    private Integer sort;
    private StdVerIdEntity testId;
    private BaseLongIdNameEntity testStaffId;
    private String unitName;
    private BaseIdValueEntity valueKind;
    private Integer version;

    public BaseLongIdNameEntity getComId() {
        return comId;
    }

    public void setComId(BaseLongIdNameEntity comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public BaseIdValueEntity getComState() {
        return comState;
    }

    public void setComState(BaseIdValueEntity comState) {
        this.comState = comState;
    }

    public InspectionSubEntity getDispMap() {
        return dispMap;
    }

    public void setDispMap(InspectionSubEntity dispMap) {
        this.dispMap = dispMap;
    }

    public String getDispValue() {
        return dispValue;
    }

    public void setDispValue(String dispValue) {
        this.dispValue = dispValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getReport() {
        return isReport;
    }

    public void setReport(Boolean report) {
        isReport = report;
    }

    public String getOriginValue() {
        return originValue;
    }

    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    public Integer getParallelNo() {
        return parallelNo;
    }

    public void setParallelNo(Integer parallelNo) {
        this.parallelNo = parallelNo;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getRoundValue() {
        return roundValue;
    }

    public void setRoundValue(String roundValue) {
        this.roundValue = roundValue;
    }

    public BaseLongIdNameEntity getSampleId() {
        return sampleId;
    }

    public void setSampleId(BaseLongIdNameEntity sampleId) {
        this.sampleId = sampleId;
    }

    public SampleTestIdEntity getSampleTestId() {
        return sampleTestId;
    }

    public void setSampleTestId(SampleTestIdEntity sampleTestId) {
        this.sampleTestId = sampleTestId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public StdVerIdEntity getTestId() {
        return testId;
    }

    public void setTestId(StdVerIdEntity testId) {
        this.testId = testId;
    }

    public BaseLongIdNameEntity getTestStaffId() {
        return testStaffId;
    }

    public void setTestStaffId(BaseLongIdNameEntity testStaffId) {
        this.testStaffId = testStaffId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BaseIdValueEntity getValueKind() {
        return valueKind;
    }

    public void setValueKind(BaseIdValueEntity valueKind) {
        this.valueKind = valueKind;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
