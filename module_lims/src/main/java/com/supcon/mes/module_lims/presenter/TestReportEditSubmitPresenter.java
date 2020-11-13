package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.TestReportSubmitEntity;
import com.supcon.mes.module_lims.model.contract.TestReportEditSubmitContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/11
 * class name
 */
public class TestReportEditSubmitPresenter extends TestReportEditSubmitContract.Presenter {
    @Override
    public void submitInspectReport(String path, Map<String, Object> params, TestReportSubmitEntity reportSubmitEntity) {
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .setTestReportEditSubmit(path, params, reportSubmitEntity)
                        .onErrorReturn(new Function<Throwable, SubmitResultEntity>() {
                            @Override
                            public SubmitResultEntity apply(Throwable error) throws Exception {
                                SubmitResultEntity entity = new SubmitResultEntity();
                                if (error.getMessage().contains("503")) {
                                    entity.msg = SupPlantApplication.getAppContext().getResources().getString(R.string.lims_service_not_exist);
                                } else {
                                    entity.msg = HttpErrorReturnUtil.getErrorInfo(error);
                                }
                                entity.success = false;
                                return entity;
                            }
                        })
                        .subscribe(new Consumer<SubmitResultEntity>() {
                            @Override
                            public void accept(SubmitResultEntity submitResultEntity) throws Exception {
                                if (submitResultEntity.success) {
                                    getView().submitInspectReportSuccess(submitResultEntity);
                                } else {
                                    getView().submitInspectReportFailed(submitResultEntity.msg);
                                }
                            }
                        })
        );
    }
}
