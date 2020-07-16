package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/15
 * class name 判断是否上载
 */
public class IfUploadEntity extends BaseEntity {
    private Boolean dealSuccessFlag;

    public Boolean getDealSuccessFlag() {
        return dealSuccessFlag;
    }

    public void setDealSuccessFlag(Boolean dealSuccessFlag) {
        this.dealSuccessFlag = dealSuccessFlag;
    }
}
