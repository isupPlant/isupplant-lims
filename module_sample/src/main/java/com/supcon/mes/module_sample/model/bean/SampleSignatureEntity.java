package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class SampleSignatureEntity extends BaseEntity {
    private String signature;
    private String viewName;
    private String signatureType;
    private boolean signatureEnabled;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public boolean isSignatureEnabled() {
        return signatureEnabled;
    }

    public void setSignatureEnabled(boolean signatureEnabled) {
        this.signatureEnabled = signatureEnabled;
    }
}
