package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
public class StdVerIdEntity extends BaseEntity {
    private Map attrMap;
    private String name;
    private Long id;
    private String busiVersion;
    private StdIdEntity stdId;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
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

    public String getBusiVersion() {
        return busiVersion;
    }

    public void setBusiVersion(String busiVersion) {
        this.busiVersion = busiVersion;
    }

    public StdIdEntity getStdId() {
        return stdId;
    }

    public void setStdId(StdIdEntity stdId) {
        this.stdId = stdId;
    }
}
