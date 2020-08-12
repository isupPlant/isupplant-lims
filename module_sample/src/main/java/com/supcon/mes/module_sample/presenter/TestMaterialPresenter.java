package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;
import com.supcon.mes.module_sample.model.contract.TestDeviceListApi;
import com.supcon.mes.module_sample.model.contract.TestMaterialListApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class TestMaterialPresenter extends TestMaterialListApi.Presenter {

    @Override
    public void getTestMaterial(String sampleTestIds) {
        mCompositeSubscription.add(SampleHttpClient.getTestMaterial(sampleTestIds).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<TestMaterialEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<TestMaterialEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<TestMaterialEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<TestMaterialEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getTestMaterialSuccess(entity.data);
                }else {
                    getView().getTestMaterialFailed(entity.msg);
                }
            }
        }));
    }
}
