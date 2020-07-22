package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
public class AvailableStdEntity extends BaseEntity {
    private Long asId;
    private Boolean hasStdVer;

    public Long getAsId() {
        return asId;
    }

    public void setAsId(Long asId) {
        this.asId = asId;
    }

    public Boolean getHasStdVer() {
        return hasStdVer;
    }

    public void setHasStdVer(Boolean hasStdVer) {
        this.hasStdVer = hasStdVer;
    }
}
