package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/18
 * Email:wanghaidong1@supcon.com
 */
public class SingleSampleRangeResult  extends BaseEntity {
    public boolean isExpand;

    public String checkResultKey;
    public String checkResultValue;
    public List<StdJudgeEntity> stdJudgeEntities=new ArrayList<>();

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getCheckResultKey() {
        return checkResultKey;
    }

    public void setCheckResultKey(String checkResultKey) {
        this.checkResultKey = checkResultKey;
    }

    public String getCheckResultValue() {
        return checkResultValue;
    }

    public void setCheckResultValue(String checkResultValue) {
        this.checkResultValue = checkResultValue;
    }

    public List<StdJudgeEntity> getStdJudgeEntities() {
        return stdJudgeEntities;
    }

    public void setStdJudgeEntities(List<StdJudgeEntity> stdJudgeEntities) {
        this.stdJudgeEntities = stdJudgeEntities;
    }
}
