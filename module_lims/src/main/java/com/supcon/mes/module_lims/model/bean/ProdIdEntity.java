package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.Unit;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
public class ProdIdEntity extends BaseSystemBackEntity {
    private BaseIntIdNameEntity sampleUnit;
    private BaseSystemBackEntity mainUnit;
    private BaseIdValueEntity isBatch;
    private String specifications;
    private String model;
    private boolean isSelect;
    private BaseIntIdNameEntity unitId;

    public BaseIntIdNameEntity getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(BaseIntIdNameEntity sampleUnit) {
        this.sampleUnit = sampleUnit;
    }

    public BaseSystemBackEntity getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(BaseSystemBackEntity mainUnit) {
        this.mainUnit = mainUnit;
    }

    public BaseIdValueEntity getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(BaseIdValueEntity isBatch) {
        this.isBatch = isBatch;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
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

    public BaseIntIdNameEntity getUnitId() {
        return unitId;
    }

    public void setUnitId(BaseIntIdNameEntity unitId) {
        this.unitId = unitId;
    }
}
