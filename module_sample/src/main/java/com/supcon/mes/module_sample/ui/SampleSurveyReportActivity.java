package com.supcon.mes.module_sample.ui;

import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.module_sample.R;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 样品检验报告列表
 */
public class SampleSurveyReportActivity extends BaseRefreshRecyclerActivity {
    @Override
    protected IListAdapter createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_survey_report;
    }
}
