package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.model.contract.ScanSamplingPointIdContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/10/21
 * class name
 */
public class ScanSamplingPointPresenter extends ScanSamplingPointIdContract.Presenter {
    @Override
    public void scanSamplingPointId(String pickSiteId) {
        mCompositeSubscription.add(SampleHttpClient.scanSamplingPointId(pickSiteId).onErrorReturn(new Function<Throwable, BAP5CommonEntity>() {
            @Override
            public BAP5CommonEntity apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity>() {
            @Override
            public void accept(BAP5CommonEntity entity) throws Exception {
                if (entity.success){
                    getView().scanSamplingPointIdSuccess(entity);
                }else {
                    getView().scanSamplingPointIdFailed(entity.msg);
                }
            }
        }));
    }
}
