package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;

/**
 * author huodongsheng
 * on 2020/11/4
 * class name
 */
public class TestReportEditHeadEntity extends BaseEntity {
    private PendingEntity pending;
    private TableTypeIdEntity tableTypeId;
    private StdVerIdEntity stdVerId;
    private ProdIdEntity prodId;
    private InspectIdEntity inspectId;
    private Long checkTime;
    private String checkResult;
    private BusiTypeIdEntity busiTypeId;
    private String batchCode;
    private String tableNo;
    private Long cid;
    private Long id;
    private Long createTime;
    private Long modifyTime;
    private BaseLongIdNameEntity checkStaffId;//请检人
    private BaseLongIdNameEntity checkDeptId;//检验部门


    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public TableTypeIdEntity getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(TableTypeIdEntity tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

    public ProdIdEntity getProdId() {
        return prodId;
    }

    public void setProdId(ProdIdEntity prodId) {
        this.prodId = prodId;
    }

    public InspectIdEntity getInspectId() {
        return inspectId;
    }

    public void setInspectId(InspectIdEntity inspectId) {
        this.inspectId = inspectId;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public BusiTypeIdEntity getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(BusiTypeIdEntity busiTypeId) {
        this.busiTypeId = busiTypeId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public BaseLongIdNameEntity getCheckStaffId() {
        return checkStaffId;
    }

    public void setCheckStaffId(BaseLongIdNameEntity checkStaffId) {
        this.checkStaffId = checkStaffId;
    }

    public BaseLongIdNameEntity getCheckDeptId() {
        return checkDeptId;
    }

    public void setCheckDeptId(BaseLongIdNameEntity checkDeptId) {
        this.checkDeptId = checkDeptId;
    }
}
