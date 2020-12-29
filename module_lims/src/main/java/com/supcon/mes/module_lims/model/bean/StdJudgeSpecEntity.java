package com.supcon.mes.module_lims.model.bean;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.supcon.mes.mbap.utils.GsonUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghaidong on 2020/7/17
 * Email:wanghaidong1@supcon.com
 */
public class StdJudgeSpecEntity extends InspectReportDetailEntity {
    public Map<String,Object> dispMap;
    public String specLimitListStr;
    public boolean isExpand=false;
    public SampleComeEntity sampleComId;
    public List<StdJudgeEntity> getSpec(){

        List<StdJudgeEntity> list=new ArrayList<>();
        if (!TextUtils.isEmpty(specLimitListStr)){
            list=GsonUtil.jsonToList(specLimitListStr,StdJudgeEntity.class);
        }
        return list;
    }
    private List<StdJudgeEntity> stdJudgeSpecEntities;
    public List<StdJudgeEntity> getStdJudgeSpecEntities(){
        if (!TextUtils.isEmpty(specLimitListStr) && stdJudgeSpecEntities==null){
            stdJudgeSpecEntities=GsonUtil.jsonToList(specLimitListStr,StdJudgeEntity.class);
        }
        return stdJudgeSpecEntities;
    }
}
