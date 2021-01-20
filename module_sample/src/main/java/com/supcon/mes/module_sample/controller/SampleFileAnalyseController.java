package com.supcon.mes.module_sample.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SearchResultEntity;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_sample.R;
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
 * author 王海东
 * on 2020/8/29
 * class name 样品结果录入
 */
public class SampleFileAnalyseController extends BaseViewController {


    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    private OnSearchOverListener mOnSearchOverListener;

    private SearchResultEntity resultEntity;

    private String searchKey;
    private String title;
    private boolean isFinish = false;

    private List<String> searchTypeList = new ArrayList<>();
    private Map<String, Object> params = new HashMap<>();

    public SampleFileAnalyseController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        searchTypeList.add(getRootView().getResources().getString(R.string.lims_file_name));
        searchTypeList.add(getRootView().getResources().getString(R.string.lims_md5));
    }

    @Override
    public void initView() {
        super.initView();
        searchTitle.showScan(false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();


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

        searchTitle.getSearchView().cancleBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanParams();
                searchTitle.hideSearchBtn();
                if (null != mOnSearchOverListener) {
                    mOnSearchOverListener.onSearchOverClick(params);
                }
            }
        });

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
            cleanParams();

            if (getRootView().getResources().getString(com.supcon.mes.module_sample.R.string.lims_file_name).equals(resultEntity.key)){
                params.put(LimsConstant.BAPQuery.C_NAME, resultEntity.result);
            }else if (getRootView().getResources().getString(R.string.lims_md5).equals(resultEntity.key)) {
                params.put(LimsConstant.BAPQuery.C_MD5, resultEntity.result);
            }
            if (null != mOnSearchOverListener) {
                mOnSearchOverListener.onSearchOverClick(params);
            }
        }else if (result.getEventId() == EventInfo.searchKeyClear){
            cleanParams();
            searchTitle.hideSearchBtn();
            if (null != mOnSearchOverListener) {
                mOnSearchOverListener.onSearchOverClick(params);
            }
        }
    }

    private void cleanParams() {
        params.clear();
    }

    @Override
    public void onStop() {
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

    public void setSearchOverListener(OnSearchOverListener mOnSearchOverListener) {
        this.mOnSearchOverListener = mOnSearchOverListener;
    }

}
