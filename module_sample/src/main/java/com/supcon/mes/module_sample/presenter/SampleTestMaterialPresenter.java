package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.model.bean.SampleTestMaterialEntity;
import com.supcon.mes.module_sample.model.contract.SampleTestMaterialListContract;
import com.supcon.mes.module_sample.model.contract.TestMaterialListContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class SampleTestMaterialPresenter extends SampleTestMaterialListContract.Presenter {

    @Override
    public void getSampleTestMaterial(String sampleTestIds) {
        mCompositeSubscription.add(SampleHttpClient.getSampleTestMaterial(sampleTestIds).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<SampleTestMaterialEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<SampleTestMaterialEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<SampleTestMaterialEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<SampleTestMaterialEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getSampleTestMaterialSuccess(entity.data);
                }else {
                    getView().getSampleTestMaterialFailed(entity.msg);
                }
            }
        }));
    }
}
