package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.TestNumEntity;
import com.supcon.mes.module_lims.model.contract.TestNumContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
public class TestNumPresenter extends TestNumContract.Presenter {
    @Override
    public void getTestNum(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("includes","quantity,sourceType.id");
        mCompositeSubscription.add(BaseLimsHttpClient.getTestNum(id,map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<TestNumEntity>>() {
            @Override
            public BAP5CommonEntity<TestNumEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<TestNumEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<TestNumEntity> entity) throws Exception {
                if (entity.success){
                    getView().getTestNumSuccess(entity.data);
                }else {
                    getView().getTestNumFailed(entity.msg);
                }
            }
        }));
    }
}
