package com.supcon.mes.module_incoming.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_incoming.R;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.contract.SurveyReportApi;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 来料检验报告列表
 */

@Router(Constant.AppCode.LIMS_IncomingSurveyReport)
public class IncomingSurveyReportActivity extends BaseRefreshRecyclerActivity<SurveyReportEntity> implements SurveyReportApi.View {
    @Override
    protected int getLayoutID() {
        return R.layout.activity_incoming_survey_report;
    }

    @Override
    protected IListAdapter<SurveyReportEntity> createAdapter() {
        return null;
    }

    @Override
    public void getSurveyReportListSuccess(SurveyReportListEntity entity) {

    }

    @Override
    public void getSurveyReportListFailed(String errorMsg) {

    }
}
