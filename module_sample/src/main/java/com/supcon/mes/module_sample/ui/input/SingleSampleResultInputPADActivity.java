package com.supcon.mes.module_sample.ui.input;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.fragment.SingleProjectFragment;
import com.supcon.mes.module_sample.ui.input.fragment.SingleSampleFragment;

/**
 * author huodongsheng
 * on 2021/1/7
 * class name
 */
@Router(Constant.AppCode.LIMS_RecordBySingleSamplePad)
public class SingleSampleResultInputPADActivity extends BaseFragmentActivity {

    private int orientation = 0;
    private Fragment singleSampleFragment,singleProjectFragment;

    private OnNotifySubRefreshListener mOnNotifySubRefreshListener;
    private OnNotifySampleRefreshListener mOnNotifySampleRefreshListener;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_single_sample_result_input_pad;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }

    @Override
    protected void initView() {
        super.initView();
        singleSampleFragment = new SingleSampleFragment();
        singleProjectFragment = new SingleProjectFragment();
        fragmentManager.beginTransaction()
                .add(R.id.flSample,singleSampleFragment)
                .add(R.id.flDetail,singleProjectFragment)
                .commit();

        Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
        orientation = cf.orientation ; //获取屏幕方向
    }

    public int getOrientation(){
        return orientation;
    }

    public void setOnNotifySubRefreshListener(OnNotifySubRefreshListener mOnNotifySubRefreshListener){
        this.mOnNotifySubRefreshListener = mOnNotifySubRefreshListener;
    }

    public void notifySubRefresh(Long sampleId){
        if (null != mOnNotifySubRefreshListener){
            mOnNotifySubRefreshListener.NotifySubRefresh(sampleId);
        }
    }

    public interface OnNotifySubRefreshListener{
        void NotifySubRefresh(Long sampleId);
    }
    public void setOnNotifySampleRefreshListener(OnNotifySampleRefreshListener mOnNotifySampleRefreshListener){
        this.mOnNotifySampleRefreshListener = mOnNotifySampleRefreshListener;
    }

    public void notifySampleRefresh(){
        if (null != mOnNotifySampleRefreshListener){
            mOnNotifySampleRefreshListener.NotifySampleRefresh();
        }
    }

    public interface OnNotifySampleRefreshListener{
        void NotifySampleRefresh();
    }
}
