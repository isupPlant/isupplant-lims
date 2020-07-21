package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_sample.model.bean.SampleReportSubmitEntity;
import com.supcon.mes.module_sample.model.contract.SampleReportDetailContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */
public class SampleReportDetailPresenter extends SampleReportDetailContract.Presenter {
    @Override
    public void getSampleReport(Long id) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .getSampleReport(id)
                        .onErrorReturn(error -> {
                            BAP5CommonEntity commonEntity = new BAP5CommonEntity();
                            commonEntity.success = false;
                            commonEntity.msg = error.getMessage();
                            return commonEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<SurveyReportEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<SurveyReportEntity> surveyReportEntity) throws Exception {
                                if (surveyReportEntity.success) {
                                    getView().getSampleReportSuccess(surveyReportEntity.data);
                                } else {
                                    getView().getSampleReportFailed(surveyReportEntity.msg);
                                }
                            }
                        })

        );
    }

    @Override
    public void getReportComList(Map<String, Object> params) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .getReportComList(params)
                        .onErrorReturn(error -> {
                            StdJudgeSpecListEntity specListEntity = new StdJudgeSpecListEntity();
                            specListEntity.msg = error.getMessage();
                            specListEntity.success = false;
                            return specListEntity;
                        })
                        .subscribe(stdJudgeSpecListEntity -> {
                            if (stdJudgeSpecListEntity.success) {
                                getView().getReportComListSuccess(stdJudgeSpecListEntity);
                            } else {
                                getView().getReportComListFailed(stdJudgeSpecListEntity.msg);
                            }
                        })
        );
    }

    @Override
    public void submitSampleReport(String path, Map<String, Object> params, SampleReportSubmitEntity reportSubmitEntity) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .submitSampleReport(path, params, reportSubmitEntity)
                        .onErrorReturn(error -> {
                            SubmitResultEntity resultEntity = new SubmitResultEntity();
                            resultEntity.success = false;
                            resultEntity.msg = error.getMessage();
                            return resultEntity;
                        })
                        .subscribe(new Consumer<SubmitResultEntity>() {
                            @Override
                            public void accept(SubmitResultEntity submitResultEntity) throws Exception {
                                if (submitResultEntity.success) {
                                    getView().submitSampleReportSuccess(submitResultEntity);
                                } else {
                                    getView().submitSampleReportFailed(submitResultEntity.msg);
                                }
                            }
                        })
        );

    }

}
