package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.InspectApplicationSubmitEntity;
import com.supcon.mes.module_lims.model.contract.InspectApplicationSubmitApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/23
 * class name
 */
public class InspectApplicationSubmitPresenter extends InspectApplicationSubmitApi.Presenter {
    @Override
    public void submitInspectApplication(String path, Map<String, Object> params, InspectApplicationSubmitEntity entity) {
        mCompositeSubscription.add(BaseLimsHttpClient.submitInspectApplication(path,params,entity)
                .onErrorReturn(new Function<Throwable, BAP5CommonEntity>() {
                    @Override
                    public BAP5CommonEntity apply(Throwable throwable) throws Exception {
                        BAP5CommonEntity bap5CommonEntity = new BAP5CommonEntity();
                        bap5CommonEntity.msg =  HttpErrorReturnUtil.getErrorInfo(throwable);
                        return bap5CommonEntity;
                    }
                }).subscribe(new Consumer<BAP5CommonEntity>() {
            @Override
            public void accept(BAP5CommonEntity entity) throws Exception {
                if (entity.success){
                    getView().submitInspectApplicationSuccess(entity);
                }else {
                    getView().submitInspectApplicationFailed(entity.msg);
                }
            }
        }));
    }
}
