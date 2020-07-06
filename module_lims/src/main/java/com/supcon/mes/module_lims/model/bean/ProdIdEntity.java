package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
public class ProdIdEntity extends BaseEntity {
    private Map attrMap;
    private String code;
    private Long id;
    private String name;
    private BaseIntIdNameEntity sampleUnit;


    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseIntIdNameEntity getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(BaseIntIdNameEntity sampleUnit) {
        this.sampleUnit = sampleUnit;
    }
}
