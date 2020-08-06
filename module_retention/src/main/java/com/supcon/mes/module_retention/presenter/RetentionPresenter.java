package com.supcon.mes.module_retention.presenter;

import android.text.TextUtils;

import com.google.gson.JsonObject;
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
        params.put("permissionCode","LIMSRetain_5.0.4.1_retention_retentionList");
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
                if (TextUtils.isEmpty(entity.pending.openUrl) || entity.pending.openUrl.contains("retentionView")) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }
}
