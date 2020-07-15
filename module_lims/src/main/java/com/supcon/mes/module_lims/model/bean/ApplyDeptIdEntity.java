package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.CompanyEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class ApplyDeptIdEntity extends BaseSystemBackEntity {
    private Long createStaffId;
    private Long modifyStaffId;
//    private StaffEntity createStaff;
//    private StaffEntity modifyStaff;
    private Long cid;
    private CompanyEntity company;
    private String layRec;
    private Integer layNo;
    private Long parentId;
    private String fullPathName;
    private String uuid;

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getModifyStaffId() {
        return modifyStaffId;
    }

    public void setModifyStaffId(Long modifyStaffId) {
        this.modifyStaffId = modifyStaffId;
    }

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

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
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

    public String getFullPathName() {
        return fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
