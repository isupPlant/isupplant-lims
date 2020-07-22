package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
public class InspectionDetailPtEntity extends BaseEntity {
    private Long id;
    private String inspStdVerCom;
    private InspectIdEntity inspectId;
    private BaseLongIdNameEntity inspectProjId;
    private Integer sort;
    private StdVerIdEntity stdVerId;
//    private InspectIdEntity inspectStdId;
//    private StdVerComIdEntity stdVerComId;
    private Integer version;
    private boolean isSelect = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInspStdVerCom() {
        return inspStdVerCom;
    }

    public void setInspStdVerCom(String inspStdVerCom) {
        this.inspStdVerCom = inspStdVerCom;
    }

    public InspectIdEntity getInspectId() {
        return inspectId;
    }

    public void setInspectId(InspectIdEntity inspectId) {
        this.inspectId = inspectId;
    }

    public BaseLongIdNameEntity getInspectProjId() {
        return inspectProjId;
    }

    public void setInspectProjId(BaseLongIdNameEntity inspectProjId) {
        this.inspectProjId = inspectProjId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public StdVerIdEntity getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdEntity stdVerId) {
        this.stdVerId = stdVerId;
    }

//    public InspectIdEntity getInspectStdId() {
//        return inspectStdId;
//    }
//
//    public void setInspectStdId(InspectIdEntity inspectStdId) {
//        this.inspectStdId = inspectStdId;
//    }
//
//    public StdVerComIdEntity getStdVerComId() {
//        return stdVerComId;
//    }
//
//    public void setStdVerComId(StdVerComIdEntity stdVerComId) {
//        this.stdVerComId = stdVerComId;
//    }

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
