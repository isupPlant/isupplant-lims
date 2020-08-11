package com.supcon.mes.module_sample.ui.input;

import android.support.v4.app.Fragment;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;

/**
 * author huodongsheng
 * on 2020/8/7
 * class name
 */
@Router(Constant.AppCode.LIMS_SampleResultInputPda)
public class SampleResultInputPDAActivity extends BaseFragmentActivity {

    private Fragment sampleFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_input_pda;
    }

    @Override
    protected void onInit() {
        super.onInit();
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
}
