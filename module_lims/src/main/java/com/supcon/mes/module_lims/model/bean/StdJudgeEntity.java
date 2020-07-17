package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * Created by wanghaidong on 2020/7/17
 * Email:wanghaidong1@supcon.com
 */
public class StdJudgeEntity extends InspectReportDetailEntity {

    public String judgeCond;
    public String maxValue;
    public String minValue;
    public String resultValue;

    public SystemCodeEntity standardGrade;
    public BaseIntIdNameEntity stdVerComId;

    @Override
    public int getTypeView() {
        return 2;
    }
}
