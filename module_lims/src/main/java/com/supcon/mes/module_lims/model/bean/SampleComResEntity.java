package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/9/9
 * class name
 */
public class SampleComResEntity extends BaseEntity {
    private Long id;
    private String resultKey;
    private BaseLongIdNameEntity sampleComId;
    private BaseLongIdNameEntity sampleId;
    private Integer sort;
    private BaseLongIdNameEntity stdVerId;
    private String testResult;

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

    public BaseLongIdNameEntity getSampleComId() {
        return sampleComId;
    }

    public void setSampleComId(BaseLongIdNameEntity sampleComId) {
        this.sampleComId = sampleComId;
    }

    public BaseLongIdNameEntity getSampleId() {
        return sampleId;
    }

    public void setSampleId(BaseLongIdNameEntity sampleId) {
        this.sampleId = sampleId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public BaseLongIdNameEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(BaseLongIdNameEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
