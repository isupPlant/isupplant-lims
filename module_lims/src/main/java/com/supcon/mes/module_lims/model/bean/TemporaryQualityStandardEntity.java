package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2020/7/22
 * class name
 */
public class TemporaryQualityStandardEntity extends BaseEntity {
    private boolean dealSuccessFlag;
    private StdVerIdEntity stdVersion;
    private StdIdEntity qualityStd;
    private BaseLongIdNameEntity inspectProj;


    public boolean isDealSuccessFlag() {
        return dealSuccessFlag;
    }

    public void setDealSuccessFlag(boolean dealSuccessFlag) {
        this.dealSuccessFlag = dealSuccessFlag;
    }

    public StdVerIdEntity getStdVersion() {
        return stdVersion;
    }

    public void setStdVersion(StdVerIdEntity stdVersion) {
        this.stdVersion = stdVersion;
    }

    public StdIdEntity getQualityStd() {
        return qualityStd;
    }

    public void setQualityStd(StdIdEntity qualityStd) {
        this.qualityStd = qualityStd;
    }

    public BaseLongIdNameEntity getInspectProj() {
        return inspectProj;
    }

    public void setInspectProj(BaseLongIdNameEntity inspectProj) {
        this.inspectProj = inspectProj;
    }
}
