package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
public class StdVerComIdEntity extends BaseEntity {
    private String carryFormula;
    private String carryRule;
    private String carrySpace;
    private BaseIdValueEntity carryType;
    private String code;
    private String defaultValue;
    private BaseIdValueEntity digitType;
    private Long id;
    private Boolean isReport;
    private String memoField;
    private String nameEng;
    private Integer parallelTimes;
    private String reportName;
    private String reportSort;
    private Integer sort;
    private StdVerIdEntity stdVerId;
    private BaseLongIdNameEntity stdVerTestId;
    private BaseLongIdNameEntity testId;
    private String unitName;
    private BaseLongIdNameEntity comId;

    public String getCarryRule() {
        return carryRule;
    }

    public void setCarryRule(String carryRule) {
        this.carryRule = carryRule;
    }

    public String getCarrySpace() {
        return carrySpace;
    }

    public void setCarrySpace(String carrySpace) {
        this.carrySpace = carrySpace;
    }

    public BaseIdValueEntity getCarryType() {
        return carryType;
    }

    public void setCarryType(BaseIdValueEntity carryType) {
        this.carryType = carryType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BaseIdValueEntity getDigitType() {
        return digitType;
    }

    public void setDigitType(BaseIdValueEntity digitType) {
        this.digitType = digitType;
    }

    public Boolean getReport() {
        return isReport;
    }

    public void setReport(Boolean report) {
        isReport = report;
    }

    public Integer getParallelTimes() {
        return parallelTimes;
    }

    public void setParallelTimes(Integer parallelTimes) {
        this.parallelTimes = parallelTimes;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public BaseLongIdNameEntity getStdVerTestId() {
        return stdVerTestId;
    }

    public void setStdVerTestId(BaseLongIdNameEntity stdVerTestId) {
        this.stdVerTestId = stdVerTestId;
    }

    public BaseLongIdNameEntity getTestId() {
        return testId;
    }

    public void setTestId(BaseLongIdNameEntity testId) {
        this.testId = testId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BaseLongIdNameEntity getComId() {
        return comId;
    }

    public void setComId(BaseLongIdNameEntity comId) {
        this.comId = comId;
    }

    public String getCarryFormula() {
        return carryFormula;
    }

    public void setCarryFormula(String carryFormula) {
        this.carryFormula = carryFormula;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMemoField() {
        return memoField;
    }

    public void setMemoField(String memoField) {
        this.memoField = memoField;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getReportSort() {
        return reportSort;
    }

    public void setReportSort(String reportSort) {
        this.reportSort = reportSort;
    }
}
