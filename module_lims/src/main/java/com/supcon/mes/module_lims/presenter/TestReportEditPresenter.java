package com.supcon.mes.module_lims.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.TestReportEditHeadEntity;
import com.supcon.mes.module_lims.model.contract.TestReportEditContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/4
 * class name
 */
public class TestReportEditPresenter extends TestReportEditContract.Presenter {
    @Override
    public void getTestReportEdit(String id, String pendingId) {
        if (TextUtils.isEmpty(pendingId)){
            mCompositeSubscription.add(BaseLimsHttpClient.getTestReportEdit(id).onErrorReturn(new Function<Throwable, BAP5CommonEntity<TestReportEditHeadEntity>>() {
                @Override
                public BAP5CommonEntity<TestReportEditHeadEntity> apply(Throwable throwable) throws Exception {
                    BAP5CommonEntity entity = new BAP5CommonEntity();
                    entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    entity.success = false;
                    return entity;
                }
            }).subscribe(new Consumer<BAP5CommonEntity<TestReportEditHeadEntity>>() {
                @Override
                public void accept(BAP5CommonEntity<TestReportEditHeadEntity> entity) throws Exception {
                    if (entity.success){
                        getView().getTestReportEditSuccess(entity.data);
                    }else {
                        getView().getTestReportEditFailed(entity.msg);
                    }
                }
            }));
        }else {
            mCompositeSubscription.add(BaseLimsHttpClient.getTestReportEdit(id,pendingId).onErrorReturn(new Function<Throwable, BAP5CommonEntity<TestReportEditHeadEntity>>() {
                @Override
                public BAP5CommonEntity<TestReportEditHeadEntity> apply(Throwable throwable) throws Exception {
                    BAP5CommonEntity entity = new BAP5CommonEntity();
                    entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    entity.success = false;
                    return entity;
                }
            }).subscribe(new Consumer<BAP5CommonEntity<TestReportEditHeadEntity>>() {
                @Override
                public void accept(BAP5CommonEntity<TestReportEditHeadEntity> entity) throws Exception {
                    if (entity.success){
                        getView().getTestReportEditSuccess(entity.data);
                    }else {
                        getView().getTestReportEditFailed(entity.msg);
                    }
                }
            }));

        }

    }

}
