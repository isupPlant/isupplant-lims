package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/20
 * class name 供应商参照实体
 */
public class SupplierReferenceEntity extends BaseEntity {
    private Long cid;
    private String code;
    private Long id;
    private String name;
    private BaseLongIdNameEntity cooperateClass;
    private boolean isSelect = false;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseLongIdNameEntity getCooperateClass() {
        return cooperateClass;
    }

    public void setCooperateClass(BaseLongIdNameEntity cooperateClass) {
        this.cooperateClass = cooperateClass;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
