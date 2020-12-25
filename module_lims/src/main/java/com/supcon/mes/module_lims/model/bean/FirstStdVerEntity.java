package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
public class FirstStdVerEntity extends BaseEntity {
    private Long id;
    private InspectIdEntity inspectId;
    private StdVerIdEntity stdVerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InspectIdEntity getInspectId() {
        return inspectId;
    }

    public void setInspectId(InspectIdEntity inspectId) {
        this.inspectId = inspectId;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId == null ? new StdVerIdEntity() : stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }
}
