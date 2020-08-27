package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class InspectionItemColumnEntity extends BaseEntity {
    private String result;
    private String columnType;
    private Boolean isLoad;
    private String columnKey;
    private String columnName;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Boolean getLoad() {
        return isLoad;
    }

    public void setLoad(Boolean load) {
        isLoad = load;
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
}
