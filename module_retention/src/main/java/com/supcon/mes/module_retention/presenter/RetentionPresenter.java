package com.supcon.mes.module_retention.presenter;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.bean.RetentionListEntity;
import com.supcon.mes.module_retention.model.contract.RetentionContract;
import com.supcon.mes.module_retention.model.network.RetentionHttpClient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
public class RetentionPresenter extends RetentionContract.Presenter {
    @Override
    public void getRetentionList(int page, boolean all, Map<String, Object> queryParams) {
        Map<String,Object> params=new HashMap<>();
        JsonObject customCondition=new JsonObject();
        params.put("customCondition",customCondition);
        Map<String, Object> firstParams = new HashMap();
        Map<String,Object> secondParams=new HashMap<>();

        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE | TABLE_NO
        if (queryParams.containsKey(Constant.BAPQuery.TABLE_NO)){
            firstParams.put(Constant.BAPQuery.TABLE_NO, queryParams.get(Constant.BAPQuery.TABLE_NO));
        }
        if (queryParams.containsKey(Constant.BAPQuery.BATCH_CODE)){
            firstParams.put(Constant.BAPQuery.BATCH_CODE, queryParams.get(Constant.BAPQuery.BATCH_CODE));
        }
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(firstParams);
        if (queryParams.containsKey(Constant.BAPQuery.CODE)) {
            secondParams.put(Constant.BAPQuery.CODE, queryParams.get(Constant.BAPQuery.CODE));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,"LIMSSA_SAMPLE_INFOS,ID,LIMSR_RETENTIONS,SAMPLE_ID"));
        }
        if (queryParams.containsKey("MATER_NAME")) {
            secondParams.put(Constant.BAPQuery.NAME, queryParams.get("MATER_NAME"));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,"BASESET_MATERIALS,ID,LIMSR_RETENTIONS,PRODUCT_ID"));
        }
        if (queryParams.containsKey("MATER_CODE")) {
            secondParams.put(Constant.BAPQuery.CODE, queryParams.get("MATER_CODE"));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,"BASESET_MATERIALS,ID,LIMSR_RETENTIONS,PRODUCT_ID"));
        }
        if (queryParams.containsKey(Constant.BAPQuery.PICKSITE)){
            Map<String,Object> thirdParams=new HashMap<>();
            thirdParams.put(Constant.BAPQuery.PICKSITE,queryParams.get(Constant.BAPQuery.PICKSITE));
            JoinSubcondEntity subcondEntity = new JoinSubcondEntity();
            subcondEntity.joinInfo="LIMSSA_SAMPLE_INFOS,ID,LIMSR_RETENTIONS,SAMPLE_ID";
            subcondEntity.subconds=new ArrayList<>();
            subcondEntity.type = "2";
            subcondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(thirdParams,"LIMSBA_PICKSITE,ID,LIMSSA_SAMPLE_INFOS,PS_ID"));
            fastQuery.subconds.add(subcondEntity);
        }
        if (queryParams.containsKey(Constant.BAPQuery.STAFF_NAME)){
            secondParams.put(Constant.BAPQuery.NAME, queryParams.get(Constant.BAPQuery.STAFF_NAME));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,"base_staff,ID,LIMSR_RETENTIONS,KEEPER_ID"));
        }


        //创建一个空的实体类
        String modelAlias="retention";
        String viewCode="LIMSRetain_5.0.4.1_retention_retentionList";
        fastQuery.modelAlias = modelAlias;
        fastQuery.viewCode = viewCode;

        params.put("permissionCode","LIMSRetain_5.0.4.1_retention_retentionList");
        params.put("fastQueryCond",fastQuery.toString());
        params.put("pageNo",page);
        params.put("paging",true);
        params.put("pageSize",20);
        String retentionList=all?"retentionList-query":"retentionList-pending";
        mCompositeSubscription.add(
                RetentionHttpClient
                        .getRetentionList(retentionList,params)
                        .onErrorReturn(error->{
                            RetentionListEntity entity=new RetentionListEntity();
                            entity.msg=error.getMessage();
                            entity.success=false;
                            return entity;
                        })
                        .subscribe(retentionListEntity -> {
                            if (retentionListEntity.success){
                                retentionListEntity.data.result=filterRetention(retentionListEntity.data.result);
                                getView().getRetentionListSuccess(retentionListEntity);
                            }else {
                                getView().getRetentionListFailed(retentionListEntity.msg);
                            }
                        })
        );
    }
    private List<RetentionEntity> filterRetention(List<RetentionEntity> list){
        List<RetentionEntity> entities=new ArrayList<>();
        if (list!=null){
            for(RetentionEntity entity:list){
                if (TextUtils.isEmpty(entity.pending.openUrl) || entity.pending.openUrl.contains("retentionView") || entity.pending.openUrl.contains("retentionEdit")) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }
}
