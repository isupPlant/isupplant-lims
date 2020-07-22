package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name 检验项目实体
 */
public class InspectionItemsEntity extends BaseEntity {
    private StdVerComIdEntity stdVerComId;
    private boolean isSelect;

    public StdVerComIdEntity getStdVerComId() {
        return stdVerComId;
    }

    public void setStdVerComId(StdVerComIdEntity stdVerComId) {
        this.stdVerComId = stdVerComId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
