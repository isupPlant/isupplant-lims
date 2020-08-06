package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/4
 * class name
 */
public class ConclusionEntity extends BaseEntity {
    private String columnType;
    private String columnKey;
    private String columnName;
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
}
