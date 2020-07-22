package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.contract.StdJudgeSpecContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/7/17
 * Email:wanghaidong1@supcon.com
 */
public class StdJudgeSpecPresenter extends StdJudgeSpecContract.Presenter {
    @Override
    public void getReportComList(Map<String, Object> params) {
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .getReportComList(params)
                        .onErrorReturn(error -> {
                            StdJudgeSpecListEntity specListEntity = new StdJudgeSpecListEntity();
                            specListEntity.msg = error.getMessage();
                            specListEntity.success = false;
                            return specListEntity;
                        })
                        .subscribe(stdJudgeSpecListEntity -> {
                            if (stdJudgeSpecListEntity.success){
                                getView().getReportComListSuccess(stdJudgeSpecListEntity);
                            }else {
                                getView().getReportComListFailed(stdJudgeSpecListEntity.msg);
                            }
                        })

        );
    }
}
