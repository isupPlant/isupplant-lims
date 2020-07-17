package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
public class InspectIdEntity extends BaseEntity {
    private BaseIntIdNameEntity applyDeptId;
    private Long id;
    private String tableNo;
    public Float quantity;
    public BaseIntIdNameEntity checkStaffId;//请检人
    public BaseIntIdNameEntity checkDeptId;//检验部门
    public BaseIntIdNameEntity psId;//采样点

    public BaseIntIdNameEntity getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(BaseIntIdNameEntity applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }
}
