package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.InspectApplicationSubmitEntity;
import com.supcon.mes.module_lims.model.contract.AddTestRequestContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/10/27
 * class name
 */
public class AddTestRequestPresenter extends AddTestRequestContract.Presenter {
    @Override
    public void addTestRequestSave(String testType, Map<String, Object> map, InspectApplicationSubmitEntity inspectApplicationSubmitEntity) {
        mCompositeSubscription.add(BaseLimsHttpClient.addTestRequestSave(testType,map,inspectApplicationSubmitEntity).onErrorReturn(new Function<Throwable, BAP5CommonEntity<PendingEntity>>() {
            @Override
            public BAP5CommonEntity apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<PendingEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<PendingEntity> entity) throws Exception {
                if (entity.success){
                    getView().addTestRequestSaveSuccess(entity.data);
                }else {
                    getView().addTestRequestSaveFailed(entity.msg);
                }
            }
        }));
    }

}
