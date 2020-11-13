package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * author huodongsheng
 * on 2020/11/4
 * class name
 */
public class TestReportEditPtEntity extends BaseEntity {
    private String checkResult;
    private String dispValue;
    private Long id;
    private String nameEng;
    private BaseLongIdNameEntity reportId;
    private String reportName;
    private Integer sort;
    private String unitName;
    private BaseIdValueEntity valueKind;
    private Long valueSrcId;
    private Integer version;

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
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

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public BaseLongIdNameEntity getReportId() {
        return reportId;
    }

    public void setReportId(BaseLongIdNameEntity reportId) {
        this.reportId = reportId;
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

    public Long getValueSrcId() {
        return valueSrcId;
    }

    public void setValueSrcId(Long valueSrcId) {
        this.valueSrcId = valueSrcId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
