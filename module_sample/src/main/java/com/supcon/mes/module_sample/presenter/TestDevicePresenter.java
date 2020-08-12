package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;
import com.supcon.mes.module_sample.model.contract.TestDeviceListApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class TestDevicePresenter extends TestDeviceListApi.Presenter {
    @Override
    public void getTestDevice(String sampleTestIds) {
        mCompositeSubscription.add(SampleHttpClient.getTestDevice(sampleTestIds).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<TestDeviceEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<TestDeviceEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<TestDeviceEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<TestDeviceEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getTestDeviceSuccess(entity.data);
                }else {
                    getView().getTestDeviceFailed(entity.msg);
                }
            }
        }));
    }
}
