package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultReviewContract;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaobing on 2021/2/2
 * desc:
 */
public class SampleResultCheckWorkflowPresenter extends SampleRecordResultReviewContract.Presenter {


    @Override
    public void recordResultReview(Map<String, Object> paramsMap) {
        try {
            mCompositeSubscription.add(
                    SampleHttpClient.SampleReview(paramsMap)
                            .onErrorReturn(throwable -> {
                                BAP5CommonEntity entity=new BAP5CommonEntity<>();
                                entity.success=false;
                                entity.msg= ErrorMsgHelper.msgParse(throwable.getMessage());
                                return entity;
                            })
                            .subscribe(entity -> {
                                if (entity.success){
                                    getView().recordResultReviewSuccess(entity);
                                }else {
                                    getView().recordResultReviewFailed(entity.msg);
                                }
                            })
            );

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void recordResultReject(Map<String, Object> paramsMap) {

    }

    @Override
    public void recordResultRefuse(Map<String, Object> paramsMap) {

    }
}
