package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.SupPlantApplication;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class SampleRecordResultSignEntity extends BaseEntity {
    private String ipAddress= SupPlantApplication.getIp();
    private String firstUserName=SupPlantApplication.getUserName();
    private String firstReason;
    private String signatureType;
    private String buttonCode;
    private String firstRemark="";
    private Long firstSignTime=System.currentTimeMillis();

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName;
    }

    public String getFirstReason() {
        return firstReason;
    }

    public void setFirstReason(String firstReason) {
        this.firstReason = firstReason;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public String getFirstRemark() {
        return firstRemark;
    }

    public void setFirstRemark(String firstRemark) {
        this.firstRemark = firstRemark;
    }

    public Long getFirstSignTime() {
        return firstSignTime;
    }

    public void setFirstSignTime(Long firstSignTime) {
        this.firstSignTime = firstSignTime;
    }
}
