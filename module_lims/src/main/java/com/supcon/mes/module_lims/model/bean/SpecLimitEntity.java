package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class SpecLimitEntity extends BaseEntity {
    private String dispValue;
    private Long id;
    private String judgeCond;
    private String judgeNames;
    private String judgeValues;
    private Boolean maxValInclude;
    private String maxValue;
    private Boolean minValInclude;
    private String minValue;
    private String resultKey;
    private String resultValue;
    private BaseLimsIdValueEntity standardGrade;
    private BaseLongIdNameEntity stdVerComId;
    private String unQualifiedValue;

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

    public String getJudgeCond() {
        return judgeCond;
    }

    public void setJudgeCond(String judgeCond) {
        this.judgeCond = judgeCond;
    }

    public String getJudgeNames() {
        return judgeNames;
    }

    public void setJudgeNames(String judgeNames) {
        this.judgeNames = judgeNames;
    }

    public String getJudgeValues() {
        return judgeValues;
    }

    public void setJudgeValues(String judgeValues) {
        this.judgeValues = judgeValues;
    }

    public Boolean getMaxValInclude() {
        return maxValInclude;
    }

    public void setMaxValInclude(Boolean maxValInclude) {
        this.maxValInclude = maxValInclude;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public Boolean getMinValInclude() {
        return minValInclude;
    }

    public void setMinValInclude(Boolean minValInclude) {
        this.minValInclude = minValInclude;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public BaseLimsIdValueEntity getStandardGrade() {
        return standardGrade;
    }

    public void setStandardGrade(BaseLimsIdValueEntity standardGrade) {
        this.standardGrade = standardGrade;
    }

    public BaseLongIdNameEntity getStdVerComId() {
        return stdVerComId;
    }

    public void setStdVerComId(BaseLongIdNameEntity stdVerComId) {
        this.stdVerComId = stdVerComId;
    }

    public String getUnQualifiedValue() {
        return unQualifiedValue;
    }

    public void setUnQualifiedValue(String unQualifiedValue) {
        this.unQualifiedValue = unQualifiedValue;
    }
}
