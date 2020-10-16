package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class InspectionSubProjectPresenter extends InspectionSubProjectContract.Presenter {
    @Override
    public void getInspectionSubProjectList(String sampleTestIds) {
        mCompositeSubscription.add(SampleHttpClient.getInspectionSubProjectList(sampleTestIds).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<InspectionSubEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<InspectionSubEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new  BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<InspectionSubEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<InspectionSubEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getInspectionSubProjectListSuccess(entity.data);
                }else {
                    getView().getInspectionSubProjectListFailed(entity.msg);
                }
            }
        }));
    }
}
