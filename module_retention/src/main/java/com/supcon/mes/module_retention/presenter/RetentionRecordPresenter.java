package com.supcon.mes.module_retention.presenter;

import com.google.gson.JsonObject;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_retention.model.bean.RecordViewEntity;
import com.supcon.mes.module_retention.model.contract.RetentionRecordContract;
import com.supcon.mes.module_retention.model.network.RetentionHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class RetentionRecordPresenter extends RetentionRecordContract.Presenter {
    @Override
    public void getRetentionRecode(Long id) {
        Map<String,Object> paramsMap=new HashMap<>();
        JsonObject customCondition=new JsonObject();
        paramsMap.put("customCondition",customCondition);
        paramsMap.put("permissionCode","LIMSRetain_5.0.4.1_retention_retRecordView");
        paramsMap.put("pageSize",65535);
        mCompositeSubscription.add(
                RetentionHttpClient
                        .getRetentionRecode(id,paramsMap)
                        .onErrorReturn(error->{
                            CommonBAP5ListEntity entity=new CommonBAP5ListEntity();
                            entity.msg=error.getMessage();
                            entity.success=false;
                            return entity;
                        })
                        .subscribe(new Consumer<CommonBAP5ListEntity<RecordViewEntity>>() {
                            @Override
                            public void accept(CommonBAP5ListEntity<RecordViewEntity> recordListEntity) throws Exception {
                                if (recordListEntity.success)
                                    getView().getRetentionRecodeSuccess(recordListEntity);
                                else
                                    getView().getRetentionRecodeFailed(recordListEntity.msg);
                            }
                        })
        );
    }
}
