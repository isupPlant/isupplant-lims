package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class SampleRecordResultSubmitEntity extends BaseEntity {
    //testDeviceListJson: []
    //testMaterialListJson: []
    //testDeviceDeleteIds:
    //testMaterialDeleteIds:
    private String dealMode;//保存或提交
    private Long sampleId;
    private List sampleComListJson;
    private List testDeviceListJson;
    private List testMaterialListJson;
    private String testDeviceDeleteIds;
    private String testMaterialDeleteIds;

    public SampleRecordResultSubmitEntity(String dealMode, Long sampleId, List sampleComListJson) {
        this.dealMode = dealMode;
        this.sampleId = sampleId;
        this.sampleComListJson = sampleComListJson;
    }

    public SampleRecordResultSubmitEntity(String dealMode, Long sampleId, List sampleComListJson, List testDeviceListJson, List testMaterialListJson, String testDeviceDeleteIds, String testMaterialDeleteIds) {
        this.dealMode = dealMode;
        this.sampleId = sampleId;
        this.sampleComListJson = sampleComListJson;
        this.testDeviceListJson = testDeviceListJson;
        this.testMaterialListJson = testMaterialListJson;
        this.testDeviceDeleteIds = testDeviceDeleteIds;
        this.testMaterialDeleteIds = testMaterialDeleteIds;
    }

    public List getTestDeviceListJson() {
        return testDeviceListJson;
    }

    public void setTestDeviceListJson(List testDeviceListJson) {
        this.testDeviceListJson = testDeviceListJson;
    }

    public List getTestMaterialListJson() {
        return testMaterialListJson;
    }

    public void setTestMaterialListJson(List testMaterialListJson) {
        this.testMaterialListJson = testMaterialListJson;
    }

    public String getTestDeviceDeleteIds() {
        return testDeviceDeleteIds;
    }

    public void setTestDeviceDeleteIds(String testDeviceDeleteIds) {
        this.testDeviceDeleteIds = testDeviceDeleteIds;
    }

    public String getTestMaterialDeleteIds() {
        return testMaterialDeleteIds;
    }

    public void setTestMaterialDeleteIds(String testMaterialDeleteIds) {
        this.testMaterialDeleteIds = testMaterialDeleteIds;
    }

    public String getDealMode() {
        return dealMode;
    }

    public void setDealMode(String dealMode) {
        this.dealMode = dealMode;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public List getSampleComListJson() {
        return sampleComListJson;
    }

    public void setSampleComListJson(List sampleComListJson) {
        this.sampleComListJson = sampleComListJson;
    }
}
