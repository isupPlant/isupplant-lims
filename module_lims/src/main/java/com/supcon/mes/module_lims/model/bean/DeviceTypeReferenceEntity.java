package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
public class DeviceTypeReferenceEntity extends BaseEntity {
    private String _code;
    private String _parentCode;
    private Long cid;
    private String code;
    private Long id;
    private Boolean isMea;
    private Boolean isParent;
    private Boolean isSpecial;
    private Long layNo;
    private String layRec;
    private String name;
    private Long parentId;
    private String remark;
    private Long version;
    private boolean isSelect;

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

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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

    public Boolean getMea() {
        return isMea;
    }

    public void setMea(Boolean mea) {
        isMea = mea;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Boolean getSpecial() {
        return isSpecial;
    }

    public void setSpecial(Boolean special) {
        isSpecial = special;
    }

    public Long getLayNo() {
        return layNo;
    }

    public void setLayNo(Long layNo) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
