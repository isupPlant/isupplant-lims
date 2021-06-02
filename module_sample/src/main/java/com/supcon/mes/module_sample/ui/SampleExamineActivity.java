package com.supcon.mes.module_sample.ui;


import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Router(Constant.AppCode.LIMS_SampleExamine)
public class SampleExamineActivity extends BaseFragmentActivity {

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_check;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}