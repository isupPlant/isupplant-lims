package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.CompanyEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.PositionEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name 检验申请详情表头实体
 */
public class InspectionApplicationDetailHeaderEntity extends BaseSystemBackEntity {
    private String bapSearchId;
    private String bapSearchType;
    private Long createStaffId;
    private Long modifyStaffId;
//    private StaffEntity createStaff;
//    private StaffEntity modifyStaff;
    private Long cid;
    private Long tableInfoId;
    private Long createDepartmentId;
    private Long createPositionId;
    private String tableNo;
    private String positionLayRec;
    private Long ownerStaffId;
    private Long ownerPositionId;
    private Long ownerDepartmentId;
    private Long effectStaffId;
    private Long effectTime;
    private Integer status;
//    private StaffEntity ownerStaff;
    private DepartmentEntity createDepartment;
    private DepartmentEntity ownerDepartment;
    private PositionEntity ownerPosition;
    private PositionEntity createPosition;
    private Long deploymentId;
    private String processKey;
    private Integer processVersion;
    private Integer effectiveState;
    private String batchCode;
    private BusiTypeIdEntity busiTypeId;
    private CheckStateEntity checkState;
    private TableTypeIdEntity tableTypeId;
    private VendorIdEntity vendorId;
    private ProdIdEntity prodId;
    private PsIdEntity psId;
    private BigDecimal quantity;
    private Boolean needLab;
    private Boolean refable;
    private Boolean closed;
    private ApplyDeptIdEntity applyDeptId;
    private ApplyStaffIdEntity applyStaffId;
    private String applyTime;
    private PendingEntity pending;
    private CompanyEntity company;
    private Long sourceId;
    private SystemCodeEntity sourceType;


    public String getBapSearchId() {
        return bapSearchId;
    }

    public void setBapSearchId(String bapSearchId) {
        this.bapSearchId = bapSearchId;
    }

    public String getBapSearchType() {
        return bapSearchType;
    }

    public void setBapSearchType(String bapSearchType) {
        this.bapSearchType = bapSearchType;
    }

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

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
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

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
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

    public Long getEffectStaffId() {
        return effectStaffId;
    }

    public void setEffectStaffId(Long effectStaffId) {
        this.effectStaffId = effectStaffId;
    }

    public Long getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(Long effectTime) {
        this.effectTime = effectTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public Integer getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(Integer processVersion) {
        this.processVersion = processVersion;
    }

    public Integer getEffectiveState() {
        return effectiveState;
    }

    public void setEffectiveState(Integer effectiveState) {
        this.effectiveState = effectiveState;
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

    public CheckStateEntity getCheckState() {
        return checkState;
    }

    public void setCheckState(CheckStateEntity checkState) {
        this.checkState = checkState;
    }

    public TableTypeIdEntity getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(TableTypeIdEntity tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public VendorIdEntity getVendorId() {
        return vendorId;
    }

    public void setVendorId(VendorIdEntity vendorId) {
        this.vendorId = vendorId;
    }

    public ProdIdEntity getProdId() {
        return prodId;
    }

    public void setProdId(ProdIdEntity prodId) {
        this.prodId = prodId;
    }

    public PsIdEntity getPsId() {
        return psId;
    }

    public void setPsId(PsIdEntity psId) {
        this.psId = psId;
    }

    public Boolean getNeedLab() {
        return needLab == null ? false : needLab;
    }

    public void setNeedLab(Boolean needLab) {
        this.needLab = needLab;
    }

    public Boolean getRefable() {
        return refable;
    }

    public void setRefable(Boolean refable) {
        this.refable = refable;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public ApplyDeptIdEntity getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(ApplyDeptIdEntity applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public ApplyStaffIdEntity getApplyStaffId() {
        return applyStaffId;
    }

    public void setApplyStaffId(ApplyStaffIdEntity applyStaffId) {
        this.applyStaffId = applyStaffId;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public SystemCodeEntity getSourceType() {
        return sourceType;
    }

    public void setSourceType(SystemCodeEntity sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
