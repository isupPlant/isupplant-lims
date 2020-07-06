package com.supcon.mes.module_sample.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.ui.adapter.SampleInquiryAdapter;
import com.supcon.mes.module_sample.R;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 取样列表
 */

@Router(Constant.AppCode.LIMS_Sampling)
public class SamplingActivity extends BaseRefreshRecyclerActivity {
    private SampleInquiryAdapter adapter;

    @Override
    protected IListAdapter createAdapter() {
        adapter = new SampleInquiryAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sampling;
    }
}
