package com.supcon.mes.module_lims.model.bean;

import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by wanghaidong on 2020/7/17
 * Email:wanghaidong1@supcon.com
 */
public class StdJudgeEntity extends InspectReportDetailEntity {

    public String judgeCond;
    public String maxValue;
    public String minValue;
    public String resultValue;
    String code= UUID.randomUUID().toString();


    public SystemCodeEntity standardGrade;
    public BaseIntIdNameEntity stdVerComId;

    @Override
    public int getTypeView() {
        return 2;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || !(o instanceof StdJudgeEntity) ) return false;
//        StdJudgeEntity that= (StdJudgeEntity) o;
//        return code.equals(that.code);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StdJudgeEntity)) return false;
        StdJudgeEntity that = (StdJudgeEntity) o;
        return id.longValue()==that.id.longValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(judgeCond, maxValue, minValue, resultValue, standardGrade, stdVerComId);
    }
}
