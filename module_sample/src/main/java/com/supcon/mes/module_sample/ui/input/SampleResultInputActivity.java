package com.supcon.mes.module_sample.ui.input;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionItemsFragment;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionSubItemFragment;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;

/**
 * author huodongsheng
 * on 2020/7/28
 * class name
 */
@Router(value = Constant.AppCode.LIMS_SampleResultInput)
public class SampleResultInputActivity extends BaseFragmentActivity {

    private Fragment sampleFragment,inspectionItemsFragment,inspectionSubItemFragment;

    private OnRefreshInspectionItemListener mOnRefreshInspectionItemListener;
    private OnSampleRefreshListener mOnSampleRefreshListener;
    private OnInspectionItemSubRefreshListener mOnInspectionItemSubRefreshListener;
    private OnOrientationChangeListener mOnOrientationChangeListener;

    private int orientation = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_input;
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
        inspectionItemsFragment = new InspectionItemsFragment();
        inspectionSubItemFragment = new InspectionSubItemFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_sample,sampleFragment)
                .add(R.id.fragment_inspection_items,inspectionItemsFragment)
                .add(R.id.fragment_sub_item,inspectionSubItemFragment)
                .commit();


        Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
        orientation = cf.orientation ; //获取屏幕方向

    }


    public int getOrientation(){
        return orientation;
    }

    public void setSampleId(Long sampleId){
        if (null != mOnRefreshInspectionItemListener){
            mOnRefreshInspectionItemListener.onRefreshInspectionItem(sampleId);
        }
    }

    public void sampleRefresh(){
        if (null != mOnSampleRefreshListener){
            mOnSampleRefreshListener.onSampleRefresh();
        }
    }

    public void setOnRefreshInspectionItemListener(OnRefreshInspectionItemListener mOnRefreshInspectionItemListener){
        this.mOnRefreshInspectionItemListener = mOnRefreshInspectionItemListener;
    }

    public interface OnRefreshInspectionItemListener{
        void onRefreshInspectionItem(Long sampleId);
    }
    public void setOnSampleRefreshListener(OnSampleRefreshListener mOnSampleRefreshListener){
        this.mOnSampleRefreshListener = mOnSampleRefreshListener;
    }

    public interface OnSampleRefreshListener{
        void onSampleRefresh();
    }

    public void setSampleTesId(Long sampleTesId){
        if (null != mOnInspectionItemSubRefreshListener){
            mOnInspectionItemSubRefreshListener.InspectionItemSubRefresh(sampleTesId);
        }
    }

    public void setOnInspectionItemSubRefreshListener(OnInspectionItemSubRefreshListener mOnInspectionItemSubRefreshListener){
        this.mOnInspectionItemSubRefreshListener = mOnInspectionItemSubRefreshListener;
    }

    public interface OnInspectionItemSubRefreshListener{
        void InspectionItemSubRefresh(Long sampleTesId);
    }

    public void setOnOrientationChangeListener(OnOrientationChangeListener mOnOrientationChangeListener){
        this.mOnOrientationChangeListener = mOnOrientationChangeListener;
    }

    public interface OnOrientationChangeListener{
        void orientationChange(int orientation);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation = getResources().getConfiguration().orientation;
        if (null != mOnOrientationChangeListener){ // 通知当前屏幕方向
            mOnOrientationChangeListener.orientationChange(orientation);
        }

    }
}
