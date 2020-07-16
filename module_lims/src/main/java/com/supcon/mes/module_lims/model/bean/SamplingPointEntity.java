package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
public class SamplingPointEntity extends BaseEntity {
    private String _code;
    private String _parentCode;
    private Boolean active;
    private Long cid;
    private Long id;
    private Boolean isParent;
    private Integer layNo;
    private String layRec;
    private String name;
    private Long parentId;
    private Long sort;
    private Integer version;
    private boolean isSelect = false;

    public String get_code() {
        return _code;
    }

    public void set_code(String _code) {
        this._code = _code;
    }

    public String get_parentCode() {
        return _parentCode;
    }

    public void set_parentCode(String _parentCode) {
        this._parentCode = _parentCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Integer getLayNo() {
        return layNo;
    }

    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }

    public String getLayRec() {
        return layRec;
    }

    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
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
