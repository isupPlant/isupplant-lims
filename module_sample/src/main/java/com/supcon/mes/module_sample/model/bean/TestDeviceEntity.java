package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class TestDeviceEntity extends BaseEntity {
    private EamIdEntity eamId;
    private BaseLongIdNameEntity eamTypeId;
    private Long id;
    private BaseLongIdNameEntity sampleTestId;
    private Long sort;
    private Long version;
    private boolean isSelect;

    public EamIdEntity getEamId() {
        return eamId;
    }

    public void setEamId(EamIdEntity eamId) {
        this.eamId = eamId;
    }

    public BaseLongIdNameEntity getEamTypeId() {
        return eamTypeId;
    }

    public void setEamTypeId(BaseLongIdNameEntity eamTypeId) {
        this.eamTypeId = eamTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
