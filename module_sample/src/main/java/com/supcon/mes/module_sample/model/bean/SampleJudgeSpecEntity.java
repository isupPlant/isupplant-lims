package com.supcon.mes.module_sample.model.bean;

import android.text.TextUtils;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.module_lims.model.bean.StdJudgeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SampleJudgeSpecEntity extends BaseEntity {

    private String specLimit;
    private String sampleComRes;
    public String stdVer1005_Qualified_Range;



    public String getSpecLimit() {
        return specLimit;
    }

    public void setSpecLimit(String specLimit) {
        this.specLimit = specLimit;
    }

    public String getSampleComRes() {
        return sampleComRes;
    }

    public void setSampleComRes(String sampleComRes) {
        this.sampleComRes = sampleComRes;
    }

    public String getStdVer1005_Qualified_Range() {
        return stdVer1005_Qualified_Range;
    }

    public void setStdVer1005_Qualified_Range(String stdVer1005_Qualified_Range) {
        this.stdVer1005_Qualified_Range = stdVer1005_Qualified_Range;
    }


    public List<StdJudgeEntity> getSpec(){
        List<StdJudgeEntity> list=new ArrayList<>();
        if (!TextUtils.isEmpty(specLimit)){
            list= GsonUtil.jsonToList(specLimit,StdJudgeEntity.class);
        }
        return list;
    }
    public List<SampleCheckResultEntity> getCheckResult(){
        List<SampleCheckResultEntity> list=new ArrayList<>();
        if (!TextUtils.isEmpty(sampleComRes)){
            list=GsonUtil.jsonToList(sampleComRes,SampleCheckResultEntity.class);
        }
        return list;
    }

}
