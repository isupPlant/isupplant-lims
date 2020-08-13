package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.EamTypeEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
public class DeviceReferenceEntity extends BaseEntity {
    private Long cid;
    private String code;
    private StaffEntity dutyStaff;
    private EamTypeEntity eamType;
    private Long id;
    private InstallPlaceBean installPlace;
    private String model;
    private String name;
    private String remark;
    private BaseIdValueEntity state;
    private BaseLongIdNameEntity useDept;
    private Boolean valid;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StaffEntity getDutyStaff() {
        return dutyStaff;
    }

    public void setDutyStaff(StaffEntity dutyStaff) {
        this.dutyStaff = dutyStaff;
    }

    public EamTypeEntity getEamType() {
        return eamType;
    }

    public void setEamType(EamTypeEntity eamType) {
        this.eamType = eamType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstallPlaceBean getInstallPlace() {
        return installPlace;
    }

    public void setInstallPlace(InstallPlaceBean installPlace) {
        this.installPlace = installPlace;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BaseIdValueEntity getState() {
        return state;
    }

    public void setState(BaseIdValueEntity state) {
        this.state = state;
    }

    public BaseLongIdNameEntity getUseDept() {
        return useDept;
    }

    public void setUseDept(BaseLongIdNameEntity useDept) {
        this.useDept = useDept;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    static class InstallPlaceBean extends BaseEntity {
        /**
         * id : 1003
         * regionName : 四工段
         */

        private String id;
        private String regionName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }
    }
}
