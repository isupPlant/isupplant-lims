package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
public class VendorIdEntity extends BaseEntity {
    private Long id;
    private Long cid;
    private Long createDepartmentId;
    private Long createPositionId;
    private String positionLayRec;
    private Long ownerStaffId;
    private Long ownerPositionId;
    private Long ownerDepartmentId;
    private Long effectiveState;
    private String code;
    private Boolean isContractor;
    private Boolean isCustomer;
    private Boolean isSupplier;
    private String name;
    private boolean isSelect = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getCreateDepartmentId() {
        return createDepartmentId;
    }

    public void setCreateDepartmentId(Long createDepartmentId) {
        this.createDepartmentId = createDepartmentId;
    }

    public Long getCreatePositionId() {
        return createPositionId;
    }

    public void setCreatePositionId(Long createPositionId) {
        this.createPositionId = createPositionId;
    }

    public String getPositionLayRec() {
        return positionLayRec;
    }

    public void setPositionLayRec(String positionLayRec) {
        this.positionLayRec = positionLayRec;
    }

    public Long getOwnerStaffId() {
        return ownerStaffId;
    }

    public void setOwnerStaffId(Long ownerStaffId) {
        this.ownerStaffId = ownerStaffId;
    }

    public Long getOwnerPositionId() {
        return ownerPositionId;
    }

    public void setOwnerPositionId(Long ownerPositionId) {
        this.ownerPositionId = ownerPositionId;
    }

    public Long getOwnerDepartmentId() {
        return ownerDepartmentId;
    }

    public void setOwnerDepartmentId(Long ownerDepartmentId) {
        this.ownerDepartmentId = ownerDepartmentId;
    }

    public Long getEffectiveState() {
        return effectiveState;
    }

    public void setEffectiveState(Long effectiveState) {
        this.effectiveState = effectiveState;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getContractor() {
        return isContractor;
    }

    public void setContractor(Boolean contractor) {
        isContractor = contractor;
    }

    public Boolean getCustomer() {
        return isCustomer;
    }

    public void setCustomer(Boolean customer) {
        isCustomer = customer;
    }

    public Boolean getSupplier() {
        return isSupplier;
    }

    public void setSupplier(Boolean supplier) {
        isSupplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
