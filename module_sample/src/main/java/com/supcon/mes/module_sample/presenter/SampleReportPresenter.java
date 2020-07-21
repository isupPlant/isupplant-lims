package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_sample.model.contract.SampleReportContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */
public class SampleReportPresenter extends SampleReportContract.Presenter {
    @Override
    public void getSampleReportByPending(long moduleId, Long pendingId) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .getSampleReportByPending(moduleId, pendingId)
                        .onErrorReturn(error->{
                            BAP5CommonEntity commonEntity=new BAP5CommonEntity();
                            commonEntity.success=false;
                            commonEntity.msg=error.getMessage();
                            return commonEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<SurveyReportEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<SurveyReportEntity> reportEntity) throws Exception {
                                if (reportEntity.success){
                                    getView().getSampleReportByPendingSuccess(reportEntity.data);
                                }else {
                                    getView().getSampleReportByPendingFailed(reportEntity.msg);
                                }
                            }
                        })

        );
    }
}
