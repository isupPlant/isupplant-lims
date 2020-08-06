package com.supcon.mes.module_retention.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_retention.model.bean.RecodeListEntity;
import com.supcon.mes.module_retention.model.bean.RecordEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.contract.RetentionDetailContract;
import com.supcon.mes.module_retention.model.network.RetentionHttpClient;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
public class RetentionDetailPresenter extends RetentionDetailContract.Presenter {
    @Override
    public void getRetentionDetailById(Long id) {
        mCompositeSubscription.add(
                RetentionHttpClient
                        .getRetentionDetailById(id)
                        .onErrorReturn(error -> {
                            BAP5CommonEntity commonEntity = new BAP5CommonEntity();
                            commonEntity.msg = error.getMessage();
                            commonEntity.success = false;
                            return commonEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<RetentionEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<RetentionEntity> commonEntity) throws Exception {
                                if (commonEntity.success) {
                                    getView().getRetentionDetailByIdSuccess(commonEntity.data);
                                } else {
                                    getView().getRetentionDetailByIdFailed(commonEntity.msg);
                                }
                            }
                        })
        );
    }

    @Override
    public void getRecord(Long id) {
        mCompositeSubscription.add(
                RetentionHttpClient
                        .getRecord(id)
                        .onErrorReturn(error -> {
                            RecodeListEntity recodeListEntity = new RecodeListEntity();
                            recodeListEntity.msg = error.getMessage();
                            recodeListEntity.success = false;
                            return recodeListEntity;
                        })
                        .subscribe(recodeListEntity -> {
                            if (recodeListEntity.success) {
                                getView().getRecordSuccess(recodeListEntity);
                            } else {
                                getView().getRecordFailed(recodeListEntity.msg);
                            }
                        })
        );
    }
}
