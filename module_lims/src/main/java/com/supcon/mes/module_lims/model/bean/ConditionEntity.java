package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name
 */
public class ConditionEntity extends BaseEntity {
    private String menuCode;

    public ConditionEntity(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}
