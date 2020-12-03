package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.FunctionEx;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.model.bean.SampleSignatureEntity;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultSubmitContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class SampleRecordResultSubmitPresenter extends SampleRecordResultSubmitContract.Presenter {
    @Override
    public void recordResultSubmit(Map<String, Object> paramsMap) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .recordResultSubmit(paramsMap)
                        .onErrorReturn(error->{
                            BAP5CommonEntity commonEntity=new BAP5CommonEntity();
                            commonEntity.msg= HttpErrorReturnUtil.getErrorInfo(error);
                            if (commonEntity.msg.contains("End of input at line 1 column 1 path"))
                                commonEntity.success=true;
                            else
                                commonEntity.success=false;
                            return commonEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity>() {
                            @Override
                            public void accept(BAP5CommonEntity bap5CommonEntity) throws Exception {
                                if (bap5CommonEntity.success){
                                    getView().recordResultSubmitSuccess(bap5CommonEntity);
                                }else {
                                    getView().recordResultSubmitFailed(bap5CommonEntity.msg);
                                }
                            }
                        })

        );
    }

    @Override
    public void getSignatureEnabled(String buttonCode) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .getSignatureEnabled(buttonCode)
                        .onErrorReturn(new FunctionEx<Throwable, SampleSignatureEntity>() {
                            @Override
                            public BAP5CommonEntity<SampleSignatureEntity> apply(Throwable throwable)  {
                                return super.apply(throwable);
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<SampleSignatureEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<SampleSignatureEntity> sampleSignatureEntity) throws Exception {
                                if (sampleSignatureEntity.success6Local){
                                    getView().getSignatureEnabledSuccess(sampleSignatureEntity.data);
                                }else {
                                    getView().getSignatureEnabledFailed(sampleSignatureEntity.msg);
                                }
                            }
                        })
        );
    }
}
