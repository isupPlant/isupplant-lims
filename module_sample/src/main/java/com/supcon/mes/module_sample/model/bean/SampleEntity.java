package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.module_lims.model.bean.PsIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
public class SampleEntity extends BaseEntity {
    private StdVerIdEntity stdVerId;
    private String code;
    private ProductIdEntity productId;
    private String registerTime;
    private String batchCode;
    private String name;
    private PsIdEntity psId;
    private Long id;
    private BaseIdValueEntity sampleType;
    private BaseIdValueEntity sampleState;
    private BaseIdValueEntity speraType;
    private boolean isSelect;

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProductIdEntity getProductId() {
        return productId;
    }

    public void setProductId(ProductIdEntity productId) {
        this.productId = productId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PsIdEntity getPsId() {
        return psId;
    }

    public void setPsId(PsIdEntity psId) {
        this.psId = psId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BaseIdValueEntity getSampleType() {
        return sampleType;
    }

    public void setSampleType(BaseIdValueEntity sampleType) {
        this.sampleType = sampleType;
    }

    public BaseIdValueEntity getSampleState() {
        return sampleState;
    }

    public void setSampleState(BaseIdValueEntity sampleState) {
        this.sampleState = sampleState;
    }

    public BaseIdValueEntity getSperaType() {
        return speraType;
    }

    public void setSperaType(BaseIdValueEntity speraType) {
        this.speraType = speraType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
