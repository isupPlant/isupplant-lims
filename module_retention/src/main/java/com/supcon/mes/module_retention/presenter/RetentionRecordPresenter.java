package com.supcon.mes.module_retention.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_retention.model.bean.RecordViewEntity;
import com.supcon.mes.module_retention.model.contract.RetentionRecordContract;
import com.supcon.mes.module_retention.model.network.RetentionHttpClient;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class RetentionRecordPresenter extends RetentionRecordContract.Presenter {
    @Override
    public void getRetentionRecode(String data_dg, String datagridCode, Long id) {
        mCompositeSubscription.add(
                RetentionHttpClient
                        .getRetentionRecode(data_dg, datagridCode, id)
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
