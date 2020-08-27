package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class ConclusionEntity extends BaseEntity {
    private String columnType;
    private String columnKey;
    private String columnName;
    private String finalResult; //前端维护字段  用于表示当前应该显示的结论
    private boolean isQualified = true; // 用于标识 当前结论是否合格
    private boolean isOpen = false;  // 是否展开子项
    private List<InspectionItemColumnEntity> columnList;

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<InspectionItemColumnEntity> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<InspectionItemColumnEntity> columnList) {
        this.columnList = columnList;
    }

    public boolean isQualified() {
        return isQualified;
    }

    public void setQualified(boolean qualified) {
        isQualified = qualified;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
