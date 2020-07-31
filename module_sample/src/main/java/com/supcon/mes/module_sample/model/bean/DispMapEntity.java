package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class DispMapEntity extends BaseEntity {
    private String sampleComRes;
    private String stdVer1008_result;

    public String getSampleComRes() {
        return sampleComRes;
    }

    public void setSampleComRes(String sampleComRes) {
        this.sampleComRes = sampleComRes;
    }

    public String getStdVer1008_result() {
        return stdVer1008_result;
    }

    public void setStdVer1008_result(String stdVer1008_result) {
        this.stdVer1008_result = stdVer1008_result;
    }
}
