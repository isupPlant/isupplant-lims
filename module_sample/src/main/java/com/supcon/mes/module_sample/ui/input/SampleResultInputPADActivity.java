package com.supcon.mes.module_sample.ui.input;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.fragment.BaseFragment;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SearchResultEntity;
import com.supcon.mes.module_lims.service.SerialWebSocketService;
import com.supcon.mes.module_lims.utils.ConnectStatus;
import com.supcon.mes.module_lims.utils.WebSocketUtils;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleInputController;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionProjectFragment;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionItemsFragment;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/28
 * class name
 */
@Router(Constant.AppCode.LIMS_SampleResultInputPad)
public class SampleResultInputPADActivity extends BaseFragmentActivity {

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    private Fragment sampleFragment,inspectionProjectFragment,inspectionSubItemFragment;

    private OnRefreshInspectionItemListener mOnRefreshInspectionItemListener;
    private OnSampleRefreshListener mOnSampleRefreshListener;
    private OnInspectionItemSubRefreshListener mOnInspectionItemSubRefreshListener;
    private OnOrientationChangeListener mOnOrientationChangeListener;

    private int orientation = 0;

    private Long sampleId,sampleTesId;
    public String sampleCode;
    private List<String> searchTypeList = new ArrayList<>();
    private String searchKey;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_input;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_result_input));

        searchTypeList.add(getString(R.string.lims_sample_code));
        searchTypeList.add(getString(R.string.lims_sample_name));
        searchTypeList.add(getString(R.string.lims_batch_number));
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitle.showScan(false);

        sampleFragment = new SampleFragment();
        inspectionProjectFragment = new InspectionProjectFragment(1l,context.getString(R.string.lims_report_name)); //随便写的1l
        inspectionSubItemFragment = new InspectionItemsFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_sample,sampleFragment)
                .add(R.id.fragment_inspection_items,inspectionProjectFragment)
                .add(R.id.fragment_sub_item,inspectionSubItemFragment)
                .commit();


        Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
        orientation = cf.orientation ; //获取屏幕方向

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        leftBtn.setOnClickListener(v -> onBackPressed());



        //当前页面搜索图标的的点击事件
        RxView.clicks(searchTitle.getSearchBtn())
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(Constant.IntentKey.SEARCH_LIST, (ArrayList<String>) searchTypeList);
                        IntentRouter.go(context, Constant.Router.SEARCH_HISTORY, bundle);
                    }
                });

        //从 历史搜索页面跳转到当前页面的搜索框的点击事件（只要点击 立马跳转到历史搜索页面）
        searchTitle.setSearchClick(new SearchTitleBar.SearchEventListener() {
            @Override
            public void searchClick(boolean isDelete) {
                SearchResultEntity resultEntity = new SearchResultEntity();
                resultEntity.key = searchKey;
                resultEntity.result = title;
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constant.IntentKey.SEARCH_LIST, (ArrayList<String>) searchTypeList);
                if (!isDelete) {
                    bundle.putSerializable(Constant.IntentKey.SEARCH_DATA, resultEntity);
                }
                IntentRouter.go(context, Constant.Router.SEARCH_HISTORY, bundle);
            }
        });

        searchTitle.setSearchLayoutLisetner(new SearchTitleBar.SearchLayoutListener() {
            @Override
            public void searchHideClick() {
                ((SampleFragment)sampleFragment).removeParams();
                ((SampleFragment)sampleFragment).goRefresh();
            }
        });

        RxView.clicks(searchTitle.getRightScanActionBar())
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ((SampleFragment)sampleFragment).openCameraScan();
                    }
                });
    }

    public void setShowTitle(String title,String searchKey){
        searchTitle.showSearchBtn(title, searchKey);
    }

    public int getOrientation(){
        return orientation;
    }

    public Fragment getInspectionProjectFragment(){
        if (null == inspectionProjectFragment){
            return null;
        }
        return inspectionProjectFragment;
    }

//    public Fragment getSampleFragment(){
//        if (null == sampleFragment){
//            return null;
//        }
//        return sampleFragment;
//    }

    public void setSampleId(Long sampleId,String sampleCode){
        this.sampleId = sampleId;
        this.sampleCode=sampleCode;
        if (null != mOnRefreshInspectionItemListener){
            mOnRefreshInspectionItemListener.onRefreshInspectionItem(sampleId);
        }
    }

    public Long getSampleId(){
        return sampleId == null ? 1l : sampleId;
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
        this.sampleTesId = sampleTesId;
        if (null != mOnInspectionItemSubRefreshListener){
            mOnInspectionItemSubRefreshListener.InspectionItemSubRefresh(sampleTesId);
        }
    }

    public Long getSampleTesId(){
        return sampleTesId == null ? 1l : sampleTesId;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (WebSocketUtils.instance!=null && WebSocketUtils.instance.getStatus()== ConnectStatus.Open){
            WebSocketUtils.getInstance().cancel();
        }
    }
}
