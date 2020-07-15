package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/9
 * class name
 */
public class MaterialReferenceEntity extends BaseEntity {
    private Map attrMap;
    private Long cid;
    private String code;
    private Long id;
    private BaseIdValueEntity isBatch;
    private Boolean isValidityManage;
    private BaseIntIdNameEntity mainUnit;
    private BaseIntIdNameEntity materialClass;
    private String model;
    private String name;
    private BaseIntIdNameEntity produceUnit;
    private BaseIntIdNameEntity purchaseUnit;
    private BaseIntIdNameEntity saleUnit;
    private BaseIntIdNameEntity sampleUnit;
    private String specifications;
    private BaseIntIdNameEntity storeUnit;
    private Boolean valid;
    private Integer version;
    private boolean isSelect;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BaseIdValueEntity getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(BaseIdValueEntity isBatch) {
        this.isBatch = isBatch;
    }

    public Boolean getValidityManage() {
        return isValidityManage;
    }

    public void setValidityManage(Boolean validityManage) {
        isValidityManage = validityManage;
    }

    public BaseIntIdNameEntity getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(BaseIntIdNameEntity mainUnit) {
        this.mainUnit = mainUnit;
    }

    public BaseIntIdNameEntity getMaterialClass() {
        return materialClass;
    }

    public void setMaterialClass(BaseIntIdNameEntity materialClass) {
        this.materialClass = materialClass;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseIntIdNameEntity getProduceUnit() {
        return produceUnit;
    }

    public void setProduceUnit(BaseIntIdNameEntity produceUnit) {
        this.produceUnit = produceUnit;
    }

    public BaseIntIdNameEntity getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(BaseIntIdNameEntity purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public BaseIntIdNameEntity getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(BaseIntIdNameEntity saleUnit) {
        this.saleUnit = saleUnit;
    }

    public BaseIntIdNameEntity getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(BaseIntIdNameEntity sampleUnit) {
        this.sampleUnit = sampleUnit;
    }

    public BaseIntIdNameEntity getStoreUnit() {
        return storeUnit;
    }

    public void setStoreUnit(BaseIntIdNameEntity storeUnit) {
        this.storeUnit = storeUnit;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
