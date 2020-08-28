package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class SampleMaterialEntity extends BaseEntity {
    private String batchCode;
    private Long cid;
    private String code;
    private String concentration;
    private Long id;
    private String memoField;
    private ProductIdEntity productId;
    private BigDecimal stockQty;
    private BaseLongIdNameEntity storeSetId;
    private BaseLongIdNameEntity unitId;
    private Boolean valid;
    private Integer version;
    private boolean isSelect;

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

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemoField() {
        return memoField;
    }

    public void setMemoField(String memoField) {
        this.memoField = memoField;
    }

    public ProductIdEntity getProductId() {
        return productId;
    }

    public void setProductId(ProductIdEntity productId) {
        this.productId = productId;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public BaseLongIdNameEntity getStoreSetId() {
        return storeSetId;
    }

    public void setStoreSetId(BaseLongIdNameEntity storeSetId) {
        this.storeSetId = storeSetId;
    }

    public BaseLongIdNameEntity getUnitId() {
        return unitId;
    }

    public void setUnitId(BaseLongIdNameEntity unitId) {
        this.unitId = unitId;
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
