package com.supcon.mes.module_sample.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 收样列表
 */

@Router(Constant.AppCode.LIMS_CollectSample)
public class CollectSampleActivity extends BaseRefreshRecyclerActivity {
    @Override
    protected IListAdapter createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_collect_sample;
    }
}
