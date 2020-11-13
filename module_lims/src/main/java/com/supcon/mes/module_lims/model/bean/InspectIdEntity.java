package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;

import java.math.BigDecimal;

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
    public VendorIdEntity vendorId;//供应商
    public BusiTypeIdEntity busiTypeId; //业务类型
    public ProdIdEntity prodId; //物料
    private Boolean needLab;


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

    public BusiTypeIdEntity getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(BusiTypeIdEntity busiTypeId) {
        this.busiTypeId = busiTypeId;
    }

    public ProdIdEntity getProdId() {
        return prodId;
    }

    public void setProdId(ProdIdEntity prodId) {
        this.prodId = prodId;
    }

    public BaseIntIdNameEntity getPsId() {
        return psId;
    }

    public void setPsId(BaseIntIdNameEntity psId) {
        this.psId = psId;
    }

    public Boolean getNeedLab() {
        return needLab;
    }

    public void setNeedLab(Boolean needLab) {
        this.needLab = needLab;
    }
}
