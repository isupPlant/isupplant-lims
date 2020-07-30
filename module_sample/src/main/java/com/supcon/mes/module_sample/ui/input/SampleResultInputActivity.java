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
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/28
 * class name
 */
@Router(value = Constant.AppCode.LIMS_SampleResultInput)
public class SampleResultInputActivity extends BaseFragmentActivity {

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    private Fragment sampleFragment,inspectionItemsFragment,inspectionSubItemFragment;

    private List<String> searchTypeList = new ArrayList<>();
    private SearchResultEntity resultEntity;
    private String searchKey;
    private String title;
    private boolean isFinish = false;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_input;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_sample_result_input));
        searchTitle.showScan(false);

        searchTypeList.add(getString(R.string.lims_sample_code));
        searchTypeList.add(getString(R.string.lims_sample_name));
        searchTypeList.add(getString(R.string.lims_batch_number));

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
                isFinish = true;
            }
        });

        searchTitle.setSearchLayoutLisetner(new SearchTitleBar.SearchLayoutListener() {
            @Override
            public void searchHideClick() {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchHistory(EventInfo result) {
        if (result.getEventId() == EventInfo.searchKey) {
            resultEntity = (SearchResultEntity) result.getValue();
            title = resultEntity.result;
            searchKey = resultEntity.key;
            if (!TextUtils.isEmpty(title)) {
                searchTitle.showSearchBtn(title, searchKey);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinish) {
            searchTitle.hideSearchBtn();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
