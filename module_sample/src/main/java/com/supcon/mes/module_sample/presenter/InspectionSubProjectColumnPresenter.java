package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectColumnApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/8/4
 * class name
 */
public class InspectionSubProjectColumnPresenter extends InspectionSubProjectColumnApi.Presenter {
    @Override
    public void getInspectionSubProjectColumn(String sampleTestIds) {
        mCompositeSubscription.add(SampleHttpClient.getInspectionSubProjectColumn(sampleTestIds).onErrorReturn(new Function<Throwable, BAP5CommonListEntity<InspectionItemColumnEntity>>() {
            @Override
            public BAP5CommonListEntity<InspectionItemColumnEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonListEntity entity = new BAP5CommonListEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonListEntity<InspectionItemColumnEntity>>() {
            @Override
            public void accept(BAP5CommonListEntity<InspectionItemColumnEntity> entity) throws Exception {
                if (entity.success){
                    getView().getInspectionSubProjectColumnSuccess(entity);
                }else {
                    getView().getInspectionSubProjectColumnFailed(entity.msg);
                }
            }
        }));
    }
}
