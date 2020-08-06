package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.util.StringUtil;

import java.util.List;

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
    private BaseLongIdNameEntity inspectProj;
    private List<StdVerComIdEntity> stdVerComList;

    private Integer sort;
    private StdVerIdEntity stdVerId;
//    private Boolean isChecked;
//    private Boolean edited;
//    private Boolean needCheck;
//    private Long key;
//    private String currClickColKey;
//    private Long rowIndex;
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

    public BaseLongIdNameEntity getInspectProj() {
        return inspectProj;
    }

    public void setInspectProj(BaseLongIdNameEntity inspectProj) {
        this.inspectProj = inspectProj;
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

    public List<StdVerComIdEntity> getStdVerComList() {
        return stdVerComList;
    }

    public void setStdVerComList(List<StdVerComIdEntity> stdVerComList) {
        this.stdVerComList = stdVerComList;
    }

    //    public Boolean getChecked() {
//        return isChecked;
//    }
//
//    public void setChecked(Boolean checked) {
//        isChecked = checked;
//    }
//
//    public Boolean getEdited() {
//        return edited;
//    }
//
//    public void setEdited(Boolean edited) {
//        this.edited = edited;
//    }
//
//    public Boolean getNeedCheck() {
//        return needCheck;
//    }
//
//    public void setNeedCheck(Boolean needCheck) {
//        this.needCheck = needCheck;
//    }
//
//    public Long getKey() {
//        return key;
//    }
//
//    public void setKey(Long key) {
//        this.key = key;
//    }
//
//    public String getCurrClickColKey() {
//        return currClickColKey;
//    }
//
//    public void setCurrClickColKey(String currClickColKey) {
//        this.currClickColKey = currClickColKey;
//    }
//
//    public Long getRowIndex() {
//        return rowIndex;
//    }
//
//    public void setRowIndex(Long rowIndex) {
//        this.rowIndex = rowIndex;
//    }
}
