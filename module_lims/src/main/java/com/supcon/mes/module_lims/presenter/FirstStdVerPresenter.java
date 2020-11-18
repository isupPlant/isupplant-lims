package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.FirstStdVerEntity;
import com.supcon.mes.module_lims.model.contract.FirstStdVerContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
public class FirstStdVerPresenter extends FirstStdVerContract.Presenter {
    @Override
    public void getFirstStdVer(String inspectId) {
        mCompositeSubscription.add(BaseLimsHttpClient.getFirstStdVerByInspectId(inspectId).onErrorReturn(new Function<Throwable, BAP5CommonEntity<FirstStdVerEntity>>() {
            @Override
            public BAP5CommonEntity<FirstStdVerEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<FirstStdVerEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<FirstStdVerEntity> entity) throws Exception {
                if (entity.success){
                    getView().getFirstStdVerSuccess(entity.data);
                }else {
                    getView().getFirstStdVerFailed(entity.msg);
                }
            }
        }));
    }
}
