package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.PositionEntity;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class ApplyStaffIdEntity extends BaseSystemBackEntity {
    private Long createStaffId;
    private Long modifyStaffId;
    private SexEntity sex;
    private SexEntity workStatus;
    private Long mainPositionId;
    private PositionEntity mainPosition;
    private Long sort;
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

    public SexEntity getSex() {
        return sex;
    }

    public void setSex(SexEntity sex) {
        this.sex = sex;
    }

    public SexEntity getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(SexEntity workStatus) {
        this.workStatus = workStatus;
    }

    public Long getMainPositionId() {
        return mainPositionId;
    }

    public void setMainPositionId(Long mainPositionId) {
        this.mainPositionId = mainPositionId;
    }

    public PositionEntity getMainPosition() {
        return mainPosition;
    }

    public void setMainPosition(PositionEntity mainPosition) {
        this.mainPosition = mainPosition;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
