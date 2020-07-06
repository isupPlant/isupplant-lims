package com.supcon.mes.module_lims.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SearchResultEntity;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.listener.OnTabClickListener;
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
 * on 2020/7/6
 * class name 检验报告列表Controller
 */
public class SurveyReportController extends BaseViewController {

    @BindByTag("filterAll")
    RadioButton filterAll;

    @BindByTag("filterBacklog")
    RadioButton filterBacklog;

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    private OnTabClickListener mOnTabClickListener;
    private OnSearchOverListener mOnSearchOverListener;

    private SearchResultEntity resultEntity;

    private String searchKey;
    private String title;
    private boolean isFinish = false;

    private List<String> searchTypeList = new ArrayList<>();
    private Map<String, Object> params = new HashMap<>();

    public SurveyReportController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        searchTypeList.add(getRootView().getResources().getString(R.string.lims_materiel_name));
        searchTypeList.add(getRootView().getResources().getString(R.string.lims_materiel_code));
        searchTypeList.add(getRootView().getResources().getString(R.string.lims_batch_number));
        searchTypeList.add(getRootView().getResources().getString(R.string.lims_document_number));
        searchTypeList.add(getRootView().getResources().getString(R.string.lims_request_no));
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
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).onBackPressed();
            }
        });

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

        RxView.clicks(filterAll)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (null != mOnTabClickListener) {
                            mOnTabClickListener.onTabClick(true);
                        }
                    }
                });

        RxView.clicks(filterBacklog)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (null != mOnTabClickListener) {
                            mOnTabClickListener.onTabClick(false);
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
            switch (resultEntity.key) {
                case "物料名称":
                    params.put(Constant.BAPQuery.NAME, resultEntity.result);
                    break;
                case "物料编码":
                    params.put(Constant.BAPQuery.CODE, resultEntity.result);
                    break;
                case "批号":
                    params.put(Constant.BAPQuery.BATCH_CODE, resultEntity.result);
                    break;
                case "单据编号":
                case "申请单号":
                    params.put(Constant.BAPQuery.TABLE_NO, resultEntity.result);
                    break;

            }
            if (null != mOnSearchOverListener) {
                mOnSearchOverListener.onSearchOverClick(params);
            }
        }
    }

    private void cleanParams() {
        params.remove(Constant.BAPQuery.NAME);
        params.remove(Constant.BAPQuery.CODE);
        params.remove(Constant.BAPQuery.BATCH_CODE);
        params.remove(Constant.BAPQuery.TABLE_NO);

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


    public void setTabClickListener(OnTabClickListener mTabClickListener) {
        this.mOnTabClickListener = mTabClickListener;
    }

    public void setSearchOverListener(OnSearchOverListener mOnSearchOverListener) {
        this.mOnSearchOverListener = mOnSearchOverListener;
    }



}
