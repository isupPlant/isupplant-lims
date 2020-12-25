package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/10
 * class name
 */
public class StdIdEntity extends BaseEntity {
    private Map attrMap;
    private Long id;
    private Boolean isDefault;
    private String name;
    private String standard;
    private AsIdEntity asId;
    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public AsIdEntity getAsId() {
        return asId == null ? new AsIdEntity() : asId;
    }

    public void setAsId(AsIdEntity asId) {
        this.asId = asId;
    }
}
