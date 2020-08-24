package com.supcon.mes.module_lims.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/8/21
 * class name
 */
public class ConfigEntity extends BaseEntity {
    @SerializedName("LIMSBasic.specialResult")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
