package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name 收样取样列表数据实体
 */
public class SampleInquiryEntity extends BaseEntity {
    private Map attrMap;
    private String batchCode;
    private Long cid;
    private String code;
    private BaseLongIdNameEntity collectStaffId;
    private Long createTime;
    private Long id;
    private String name;
    private ProdIdEntity productId;
    private BaseIntIdNameEntity psId;
    private Long registerTime;
    private BaseIdValueEntity sampleType;
    private BaseIdValueEntity speraType;
    private StdVerIdEntity stdVerId;
    private BaseIdValueEntity useMethod;
    private Boolean valid;
    private Integer version;
    private boolean isSelect = false;
    public boolean isThisCompany = false;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

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

    public BaseLongIdNameEntity getCollectStaffId() {
        return collectStaffId;
    }

    public void setCollectStaffId(BaseLongIdNameEntity collectStaffId) {
        this.collectStaffId = collectStaffId;
    }



    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public ProdIdEntity getProductId() {
        return productId;
    }

    public void setProductId(ProdIdEntity productId) {
        this.productId = productId;
    }

    public BaseIntIdNameEntity getPsId() {
        return psId;
    }

    public void setPsId(BaseIntIdNameEntity psId) {
        this.psId = psId;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }

    public BaseIdValueEntity getSampleType() {
        return sampleType;
    }

    public void setSampleType(BaseIdValueEntity sampleType) {
        this.sampleType = sampleType;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
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

    public BaseIdValueEntity getSperaType() {
        return speraType;
    }

    public void setSperaType(BaseIdValueEntity speraType) {
        this.speraType = speraType;
    }

    public BaseIdValueEntity getUseMethod() {
        return useMethod;
    }

    public void setUseMethod(BaseIdValueEntity useMethod) {
        this.useMethod = useMethod;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
