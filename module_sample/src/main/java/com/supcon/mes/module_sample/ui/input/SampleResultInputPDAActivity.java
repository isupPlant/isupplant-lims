package com.supcon.mes.module_sample.ui.input;

import android.support.v4.app.Fragment;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author huodongsheng
 * on 2020/8/7
 * class name
 */
@Router(Constant.AppCode.LIMS_SampleResultInputPda)
public class SampleResultInputPDAActivity extends BaseFragmentActivity {

    private SampleFragment sampleFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_input_pda;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }

    @Override
    protected void initView() {
        super.initView();
        sampleFragment = new SampleFragment();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_sample,sampleFragment)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refershSample(String refersh){
        if (refersh.equals("refersh")){
            sampleFragment.goRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
