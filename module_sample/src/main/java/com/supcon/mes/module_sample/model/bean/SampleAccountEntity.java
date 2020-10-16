package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.module_lims.model.bean.ProductIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;

/**
 * author huodongsheng
 * on 2020/10/15
 * class name
 */
public class SampleAccountEntity extends BaseEntity {
    private Long cid;
    private String code;
    private Long id;
    private String name;
    private ProductIdEntity productId;
    private PsIdEntity psId;
    private Long registerTime;
    private BaseIdValueEntity sampleState;
    private BaseIdValueEntity sampleType;
    private BaseIdValueEntity speraType;
    private StdVerIdEntity stdVerId;
    private BaseIdValueEntity useMethod;
    private Boolean valid;
    private Integer version;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductIdEntity getProductId() {
        return productId;
    }

    public void setProductId(ProductIdEntity productId) {
        this.productId = productId;
    }

    public PsIdEntity getPsId() {
        return psId;
    }

    public void setPsId(PsIdEntity psId) {
        this.psId = psId;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }

    public BaseIdValueEntity getSampleState() {
        return sampleState;
    }

    public void setSampleState(BaseIdValueEntity sampleState) {
        this.sampleState = sampleState;
    }

    public BaseIdValueEntity getSampleType() {
        return sampleType;
    }

    public void setSampleType(BaseIdValueEntity sampleType) {
        this.sampleType = sampleType;
    }

    public BaseIdValueEntity getSperaType() {
        return speraType;
    }

    public void setSperaType(BaseIdValueEntity speraType) {
        this.speraType = speraType;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public BaseIdValueEntity getUseMethod() {
        return useMethod;
    }

    public void setUseMethod(BaseIdValueEntity useMethod) {
        this.useMethod = useMethod;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    class PsIdEntity extends BaseEntity{
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
