package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
public class ProdIdEntity extends BaseSystemBackEntity {
    private BaseIntIdNameEntity sampleUnit;

    public BaseIntIdNameEntity getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(BaseIntIdNameEntity sampleUnit) {
        this.sampleUnit = sampleUnit;
    }
}
