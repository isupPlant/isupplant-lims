package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * author huodongsheng
 * on 2020/8/19
 * class name
 */
public class CalcParamInfoEntity extends BaseEntity {
    private BaseIdValueEntity dealFunc;
    private BaseIdValueEntity incomeType;
    private BaseIdValueEntity outcomeType;
    private String paramName;
    private BaseIdValueEntity paramType;
    private String testComName;
    private String testItemName;

    public BaseIdValueEntity getDealFunc() {
        return dealFunc;
    }

    public void setDealFunc(BaseIdValueEntity dealFunc) {
        this.dealFunc = dealFunc;
    }

    public BaseIdValueEntity getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(BaseIdValueEntity incomeType) {
        this.incomeType = incomeType;
    }

    public BaseIdValueEntity getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(BaseIdValueEntity outcomeType) {
        this.outcomeType = outcomeType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public BaseIdValueEntity getParamType() {
        return paramType;
    }

    public void setParamType(BaseIdValueEntity paramType) {
        this.paramType = paramType;
    }

    public String getTestComName() {
        return testComName;
    }

    public void setTestComName(String testComName) {
        this.testComName = testComName;
    }

    public String getTestItemName() {
        return testItemName;
    }

    public void setTestItemName(String testItemName) {
        this.testItemName = testItemName;
    }
}
