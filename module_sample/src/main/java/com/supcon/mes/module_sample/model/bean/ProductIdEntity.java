package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
public class ProductIdEntity extends BaseEntity {
    private String code;
    private String name;
    private Long id;
    private BaseIdValueEntity isBatch;
    private BaseSystemBackEntity mainUnit;


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

    public BaseSystemBackEntity getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(BaseSystemBackEntity mainUnit) {
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
}
