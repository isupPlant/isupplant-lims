package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.CompanyEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PositionEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class PsIdEntity extends BaseSystemBackEntity {
//    private StaffEntity createStaff;
//    private StaffEntity modifyStaff;
    private Long cid;
    private String layRec;
    private Integer layNo;
    private Long parentId;
    private String _code;
    private String _parentCode;
    private String fullPathName;
    private Boolean isParent;
    private Boolean leaf;
    private Boolean leaf2;
    private Long createDepartmentId;
    private DepartmentEntity createDepartment;
    private Long createPositionId;
    private Long ownerStaffId;
    private Long ownerPositionId;
    private Long ownerDepartmentId;
//    private StaffEntity ownerStaff;
    private DepartmentEntity ownerDepartment;
    private PositionEntity ownerPosition;
    private PositionEntity createPosition;
    private Integer effectiveState;
    private Boolean active;
    private String extraCol;
    private CompanyEntity company;
    private Boolean root;


//    public StaffEntity getCreateStaff() {
//        return createStaff;
//    }
//
//    public void setCreateStaff(StaffEntity createStaff) {
//        this.createStaff = createStaff;
//    }
//
//    public StaffEntity getModifyStaff() {
//        return modifyStaff;
//    }
//
//    public void setModifyStaff(StaffEntity modifyStaff) {
//        this.modifyStaff = modifyStaff;
//    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getLayRec() {
        return layRec;
    }

    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }

    public Integer getLayNo() {
        return layNo;
    }

    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

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

    public String getFullPathName() {
        return fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getLeaf2() {
        return leaf2;
    }

    public void setLeaf2(Boolean leaf2) {
        this.leaf2 = leaf2;
    }

    public Long getCreateDepartmentId() {
        return createDepartmentId;
    }

    public void setCreateDepartmentId(Long createDepartmentId) {
        this.createDepartmentId = createDepartmentId;
    }

    public DepartmentEntity getCreateDepartment() {
        return createDepartment;
    }

    public void setCreateDepartment(DepartmentEntity createDepartment) {
        this.createDepartment = createDepartment;
    }

    public Long getCreatePositionId() {
        return createPositionId;
    }

    public void setCreatePositionId(Long createPositionId) {
        this.createPositionId = createPositionId;
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

//    public StaffEntity getOwnerStaff() {
//        return ownerStaff;
//    }
//
//    public void setOwnerStaff(StaffEntity ownerStaff) {
//        this.ownerStaff = ownerStaff;
//    }

    public DepartmentEntity getOwnerDepartment() {
        return ownerDepartment;
    }

    public void setOwnerDepartment(DepartmentEntity ownerDepartment) {
        this.ownerDepartment = ownerDepartment;
    }

    public PositionEntity getOwnerPosition() {
        return ownerPosition;
    }

    public void setOwnerPosition(PositionEntity ownerPosition) {
        this.ownerPosition = ownerPosition;
    }

    public PositionEntity getCreatePosition() {
        return createPosition;
    }

    public void setCreatePosition(PositionEntity createPosition) {
        this.createPosition = createPosition;
    }

    public Integer getEffectiveState() {
        return effectiveState;
    }

    public void setEffectiveState(Integer effectiveState) {
        this.effectiveState = effectiveState;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getExtraCol() {
        return extraCol;
    }

    public void setExtraCol(String extraCol) {
        this.extraCol = extraCol;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }
}
