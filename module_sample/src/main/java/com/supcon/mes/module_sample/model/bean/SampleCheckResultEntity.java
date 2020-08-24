package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * Created by wanghaidong on 2020/8/17
 * Email:wanghaidong1@supcon.com
 */
public class SampleCheckResultEntity extends BaseEntity {
    private Long id;
    private String resultKey;
    private BaseIdValueEntity sampleComId;
    private BaseIdValueEntity sampleId;
    private BaseIdValueEntity stdVerId;
    private String testResult;
    private boolean isExpand;
    private boolean firstExpand=true;//是否是第一次展开的，默认是第一次




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public BaseIdValueEntity getSampleComId() {
        return sampleComId;
    }

    public void setSampleComId(BaseIdValueEntity sampleComId) {
        this.sampleComId = sampleComId;
    }

    public BaseIdValueEntity getSampleId() {
        return sampleId;
    }

    public void setSampleId(BaseIdValueEntity sampleId) {
        this.sampleId = sampleId;
    }

    public BaseIdValueEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(BaseIdValueEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }


    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isFirstExpand() {
        return firstExpand;
    }

    public void setFirstExpand(boolean firstExpand) {
        this.firstExpand = firstExpand;
    }
}
