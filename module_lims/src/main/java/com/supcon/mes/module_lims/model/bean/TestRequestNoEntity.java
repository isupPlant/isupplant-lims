package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

/**
 * author huodongsheng
 * on 2020/11/16
 * class name
 */
public class TestRequestNoEntity extends BaseEntity {
    private BaseLongIdNameEntity applyDeptId;
    private BaseLongIdNameEntity applyStaffId;
    private Long applyTime;
    private String  batchCode;
    private BusiTypeIdEntity busiTypeId;
    private BaseIdValueEntity checkState;
    private VendorIdEntity vendorId;//供应商
    private Long cid;
    private Long id;
    private Boolean needLab;
    private ProdIdEntity prodId;
    private BaseIntIdNameEntity psId;
    private Long status;
    private Long tableInfoId;
    private String tableNo;
    private Boolean valid;
    private Integer version;
    private boolean isSelect;

    public BaseLongIdNameEntity getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(BaseLongIdNameEntity applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public BaseLongIdNameEntity getApplyStaffId() {
        return applyStaffId;
    }

    public void setApplyStaffId(BaseLongIdNameEntity applyStaffId) {
        this.applyStaffId = applyStaffId;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BusiTypeIdEntity getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(BusiTypeIdEntity busiTypeId) {
        this.busiTypeId = busiTypeId;
    }

    public BaseIdValueEntity getCheckState() {
        return checkState;
    }

    public void setCheckState(BaseIdValueEntity checkState) {
        this.checkState = checkState;
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

    public Boolean getNeedLab() {
        return needLab;
    }

    public void setNeedLab(Boolean needLab) {
        this.needLab = needLab;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public VendorIdEntity getVendorId() {
        return vendorId;
    }

    public void setVendorId(VendorIdEntity vendorId) {
        this.vendorId = vendorId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
