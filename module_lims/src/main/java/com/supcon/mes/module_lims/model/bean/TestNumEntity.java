package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
public class TestNumEntity extends BaseEntity {
    private BigDecimal quantity;
    private String sourceType;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
