package com.supcon.mes.module_lims.model.bean;

import android.text.TextUtils;

import com.google.gson.JsonArray;
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

    public List<StdJudgeEntity> getSpec(){
        List<StdJudgeEntity> list=new ArrayList<>();
        if (!TextUtils.isEmpty(specLimitListStr)){
            list=GsonUtil.jsonToList(specLimitListStr,StdJudgeEntity.class);
        }
        return list;
    }
}
