package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;

/**
 * author huodongsheng
 * on 2020/7/30
 * class name
 */
public class InspectionItemsEntity extends BaseEntity {
    private BaseLongIdNameEntity analystGroupId;
    private Long id;
    private Integer parallelNo;
    private BaseLongIdNameEntity sampleId;
    private Integer sort;
    private StdVerIdEntity testId;
    private BaseLongIdNameEntity testStaffId;
    private BaseIdValueEntity testState;
    private Integer version;
    private boolean isSelect;

    public BaseLongIdNameEntity getAnalystGroupId() {
        return analystGroupId;
    }

    public void setAnalystGroupId(BaseLongIdNameEntity analystGroupId) {
        this.analystGroupId = analystGroupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParallelNo() {
        return parallelNo;
    }

    public void setParallelNo(Integer parallelNo) {
        this.parallelNo = parallelNo;
    }

    public BaseLongIdNameEntity getSampleId() {
        return sampleId;
    }

    public void setSampleId(BaseLongIdNameEntity sampleId) {
        this.sampleId = sampleId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public StdVerIdEntity getTestId() {
        return testId;
    }

    public void setTestId(StdVerIdEntity testId) {
        this.testId = testId;
    }

    public BaseLongIdNameEntity getTestStaffId() {
        return testStaffId;
    }

    public void setTestStaffId(BaseLongIdNameEntity testStaffId) {
        this.testStaffId = testStaffId;
    }

    public BaseIdValueEntity getTestState() {
        return testState;
    }

    public void setTestState(BaseIdValueEntity testState) {
        this.testState = testState;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
