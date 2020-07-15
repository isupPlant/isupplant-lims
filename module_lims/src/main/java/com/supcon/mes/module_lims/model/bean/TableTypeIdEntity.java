package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.Company;
import com.supcon.mes.middleware.model.bean.CompanyEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PositionEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class TableTypeIdEntity extends BaseSystemBackEntity {
    private Long createStaffId;
    //private StaffEntity createStaff;
    private Long cid;
    private Long createDepartmentId;
    private Long createPositionId;
    private Long ownerStaffId;
    //private StaffEntity ownerStaff;
    private DepartmentEntity createDepartment;
    private PositionEntity createPosition;
    private Integer effectiveState;
    private String reportCodeRule;
    private String inspCodeRule;
    private CompanyEntity company;

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

//    public StaffEntity getCreateStaff() {
//        return createStaff;
//    }
//
//    public void setCreateStaff(StaffEntity createStaff) {
//        this.createStaff = createStaff;
//    }

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

    public Long getOwnerStaffId() {
        return ownerStaffId;
    }

    public void setOwnerStaffId(Long ownerStaffId) {
        this.ownerStaffId = ownerStaffId;
    }

//    public StaffEntity getOwnerStaff() {
//        return ownerStaff;
//    }
//
//    public void setOwnerStaff(StaffEntity ownerStaff) {
//        this.ownerStaff = ownerStaff;
//    }

    public DepartmentEntity getCreateDepartment() {
        return createDepartment;
    }

    public void setCreateDepartment(DepartmentEntity createDepartment) {
        this.createDepartment = createDepartment;
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

    public String getReportCodeRule() {
        return reportCodeRule;
    }

    public void setReportCodeRule(String reportCodeRule) {
        this.reportCodeRule = reportCodeRule;
    }

    public String getInspCodeRule() {
        return inspCodeRule;
    }

    public void setInspCodeRule(String inspCodeRule) {
        this.inspCodeRule = inspCodeRule;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
