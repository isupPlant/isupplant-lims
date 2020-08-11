package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class DispMapEntity extends BaseEntity {
    private String sampleComRes;
    private List<String> specLimit; // 值
    private List<ConclusionEntity> conclusionList; //列名称

    public String getSampleComRes() {
        return sampleComRes;
    }

    public void setSampleComRes(String sampleComRes) {
        this.sampleComRes = sampleComRes;
    }

    public List<String> getSpecLimit() {
        return specLimit;
    }

    public void setSpecLimit(List<String> specLimit) {
        this.specLimit = specLimit;
    }

    public List<ConclusionEntity> getConclusionList() {
        return conclusionList;
    }

    public void setConclusionList(List<ConclusionEntity> conclusionList) {
        this.conclusionList = conclusionList;
    }
}
