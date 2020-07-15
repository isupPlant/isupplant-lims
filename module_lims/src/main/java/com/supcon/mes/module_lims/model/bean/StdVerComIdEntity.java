package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
public class StdVerComIdEntity extends BaseEntity {
    private Long id;
    private String unitName;
    private BaseIntIdNameEntity comId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BaseIntIdNameEntity getComId() {
        return comId;
    }

    public void setComId(BaseIntIdNameEntity comId) {
        this.comId = comId;
    }
}
