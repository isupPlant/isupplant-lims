package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 检验申请列表实体
 */
public class InspectionApplicationEntity extends BaseEntity {
    private BaseIntIdNameEntity applyDeptId;
    private BaseIntIdNameEntity applyStaffId;
    private Long applyTime;
    private Map attrMap;
    private String batchCode;
    private BaseIntIdNameEntity busiTypeId;
    private BaseIdValueEntity checkState;
    private Long cid;
    private Boolean closed;
    private Long id;
    private Boolean needLab;
    private PendingEntity pending;
    private ProdIdEntity prodId;
    private Integer status;
    private Long tableInfoId;
    private String tableNo;
    private Boolean valid;
    private Integer version;

    public BaseIntIdNameEntity getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(BaseIntIdNameEntity applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public BaseIntIdNameEntity getApplyStaffId() {
        return applyStaffId;
    }

    public void setApplyStaffId(BaseIntIdNameEntity applyStaffId) {
        this.applyStaffId = applyStaffId;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BaseIntIdNameEntity getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(BaseIntIdNameEntity busiTypeId) {
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

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
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

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public ProdIdEntity getProdId() {
        return prodId;
    }

    public void setProdId(ProdIdEntity prodId) {
        this.prodId = prodId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
}
