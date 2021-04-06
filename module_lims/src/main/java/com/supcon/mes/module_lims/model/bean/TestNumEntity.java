package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
public class TestNumEntity extends BaseEntity {
    private BigDecimal quantity;
    private SystemCodeEntity sourceType;

    public BigDecimal getQuantity() {
        return quantity == null ? new BigDecimal(0) : quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public SystemCodeEntity getSourceType() {
        return sourceType;
    }

    public void setSourceType(SystemCodeEntity sourceType) {
        this.sourceType = sourceType;
    }
}
