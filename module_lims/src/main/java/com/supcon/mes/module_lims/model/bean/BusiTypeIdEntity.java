package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.CompanyEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class BusiTypeIdEntity extends BaseSystemBackEntity {
    private Long createStaffId;
    //private StaffEntity createStaff;
    private Long cid;
    private TableTypeIdEntity tableTypeId;
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

    public TableTypeIdEntity getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(TableTypeIdEntity tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
