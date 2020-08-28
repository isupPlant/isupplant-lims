package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.ProductIdEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class TestMaterialEntity extends BaseEntity {
    private Long id;
    private String batchCode;
    private String matCode;
    private Long matRecordId;
    private String memoField;
    private ProductIdEntity productId;
    private Long sampleId;
    private BaseLongIdNameEntity sampleTestId;
    private Long sort;
    private BigDecimal useQty;
    private Long version;
    private boolean isSelect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public Long getMatRecordId() {
        return matRecordId;
    }

    public void setMatRecordId(Long matRecordId) {
        this.matRecordId = matRecordId;
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

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public BaseLongIdNameEntity getSampleTestId() {
        return sampleTestId;
    }

    public void setSampleTestId(BaseLongIdNameEntity sampleTestId) {
        this.sampleTestId = sampleTestId;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public BigDecimal getUseQty() {
        return useQty;
    }

    public void setUseQty(BigDecimal useQty) {
        this.useQty = useQty;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


}
