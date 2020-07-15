package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/10
 * class name 质量标准实体类
 */
public class QualityStandardReferenceEntity extends BaseEntity {
    private Map attrMap;
    private String busiVersion;
    private Long cid;
    private Long id;
    private String name;
    private Long startDate;
    private StdIdEntity stdId;
    private Boolean valid;
    private Integer version;
    private boolean isSelect;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getBusiVersion() {
        return busiVersion;
    }

    public void setBusiVersion(String busiVersion) {
        this.busiVersion = busiVersion;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public StdIdEntity getStdId() {
        return stdId;
    }

    public void setStdId(StdIdEntity stdId) {
        this.stdId = stdId;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
