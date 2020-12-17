package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.BusiTypeIdEntity;
import com.supcon.mes.module_lims.model.contract.BusiTypeByCodeContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/12/17
 * class name
 */
public class BusiTypeByCodePresenter extends BusiTypeByCodeContract.Presenter {
    @Override
    public void getBusiTypeByCode(String code) {
        mCompositeSubscription.add(BaseLimsHttpClient.getBusiTypeByCode(code).onErrorReturn(new Function<Throwable, BAP5CommonEntity<BusiTypeIdEntity>>() {
            @Override
            public BAP5CommonEntity<BusiTypeIdEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<BusiTypeIdEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<BusiTypeIdEntity> entity) throws Exception {
                if (entity.success){
                    getView().getBusiTypeByCodeSuccess(entity.data);
                }else {
                    getView().getBusiTypeByCodeFailed(entity.msg);
                }
            }
        }));
    }
}
