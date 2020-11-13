package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.QualityStdConclusionEntity;
import com.supcon.mes.module_lims.model.contract.QualityStdIdByConclusionContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/9
 * class name
 */
public class QualityStdIdByConclusionPresenter extends QualityStdIdByConclusionContract.Presenter {
    @Override
    public void getStdVerGradesByStdVerId(String stdVersionId) {
        mCompositeSubscription.add(BaseLimsHttpClient.getStdVerGradesByStdVerId(stdVersionId).onErrorReturn(new Function<Throwable, BAP5CommonListEntity<QualityStdConclusionEntity>>() {
            @Override
            public BAP5CommonListEntity<QualityStdConclusionEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonListEntity entity = new BAP5CommonListEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonListEntity<QualityStdConclusionEntity>>() {
            @Override
            public void accept(BAP5CommonListEntity<QualityStdConclusionEntity> entity) throws Exception {
                if (entity.success){
                    getView().getStdVerGradesByStdVerIdSuccess(entity);
                }else {
                    getView().getStdVerGradesByStdVerIdFailed(entity.msg);
                }
            }
        }));
    }
}
