package com.supcon.mes.module_other.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.contract.SurveyReportApi;
import com.supcon.mes.module_other.R;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 其他检验报告列表
 */

@Router(Constant.AppCode.LIMS_OtherSurveyReport)
public class OtherSurveyReportActivity extends BaseRefreshRecyclerActivity<SurveyReportEntity> implements SurveyReportApi.View {
    @Override
    protected int getLayoutID() {
        return R.layout.activity_other_survey_report;
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
