package com.supcon.mes.module_sample.ui.input;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SearchResultEntity;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionItemsFragment;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionSubItemFragment;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

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
}
