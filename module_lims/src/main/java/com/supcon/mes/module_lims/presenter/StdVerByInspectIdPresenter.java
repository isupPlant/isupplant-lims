package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.api.StdVerByInspectIdAPI;
import com.supcon.mes.module_lims.model.bean.FirstStdVerEntity;
import com.supcon.mes.module_lims.model.contract.StdVerByInspectIdContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2021/3/15
 * class name
 */
public class StdVerByInspectIdPresenter extends StdVerByInspectIdContract.Presenter {
    @Override
    public void getStdVerByInspectId(String inspectId) {
        mCompositeSubscription.add(BaseLimsHttpClient.getStdVerByInspectId(inspectId).onErrorReturn(new Function<Throwable, BAP5CommonListEntity<FirstStdVerEntity>>() {
            @Override
            public BAP5CommonListEntity<FirstStdVerEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonListEntity entity = new BAP5CommonListEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonListEntity<FirstStdVerEntity>>() {
            @Override
            public void accept(BAP5CommonListEntity<FirstStdVerEntity> entity) throws Exception {
                if (entity.success){
                    getView().getStdVerByInspectIdSuccess(entity);
                }else {
                    getView().getStdVerByInspectIdFailed(entity.msg);
                }
            }
        }));
    }
}
