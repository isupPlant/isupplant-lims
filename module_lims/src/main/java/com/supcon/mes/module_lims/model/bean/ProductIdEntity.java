package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.Unit;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class ProductIdEntity extends BaseEntity {
    private String code;
    private String name;
    private Long id;
    private BaseIdValueEntity isBatch;
    private BaseLongIdNameEntity mainUnit;
    private Unit unitId;
    private BaseSystemBackEntity sampleUnit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BaseLongIdNameEntity getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(BaseLongIdNameEntity mainUnit) {
        this.mainUnit = mainUnit;
    }

    //是否启用批次
    public boolean isEnableBatch() {
        if (isBatch != null) {
            if ("BaseSet_isBatch/nobatch".equals(isBatch.getId())) {
                return false;
            }
        }
        return true;
    }

    public Unit getUnitId() {
        return unitId;
    }

    public void setUnitId(Unit unitId) {
        this.unitId = unitId;
    }

    public BaseSystemBackEntity getSampleUnit() {
        return sampleUnit;
    }

    public void BaseSystemBackEntity(BaseSystemBackEntity sampleUnit) {
        this.sampleUnit = sampleUnit;
    }
}
