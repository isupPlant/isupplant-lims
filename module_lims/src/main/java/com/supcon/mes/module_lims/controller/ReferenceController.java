package com.supcon.mes.module_lims.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Contract;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SearchResultEntity;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
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
 * on 2020/7/10
 * class name 参照页面的Controller，每个参照页面都有条件搜索。所以写成一个controller
 */
public class ReferenceController extends BaseViewController {

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    private SearchResultEntity resultEntity;
    private OnSearchOverListener mOnSearchOverListener;

    private List<String> searchTypeList = new ArrayList<>();
    private Map<String, Object> params = new HashMap<>();

    private String searchKey;
    private String title;
    private boolean isFinish = false;

    public ReferenceController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

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

            if (null != mOnSearchOverListener) {
                mOnSearchOverListener.onSearchOverClick(screenSearchResult(resultEntity));
            }
        }else if (result.getEventId() == EventInfo.searchKeyClear){
            cleanParams();
            searchTitle.hideSearchBtn();
            if (null != mOnSearchOverListener) {
                mOnSearchOverListener.onSearchOverClick(params);
            }
        }
    }

    public void setSearchTypeList(String ...condition){
        searchTypeList.clear();
        for (String type: condition){
            if (!StringUtil.isEmpty(type)){
                searchTypeList.add(type);
            }
        }
    }


    // 将搜索页面回传的数据进行比对 并放入集合
    public Map<String, Object> screenSearchResult(SearchResultEntity entity){
        if (context.getResources().getString(R.string.lims_quality_standard).equals(entity.key)
                || context.getResources().getString(R.string.lims_materiel_name).equals(entity.key)
                || context.getResources().getString(R.string.lims_application_scheme).equals(entity.key)
                || context.getResources().getString(R.string.lims_customer_name).equals(entity.key)
                || context.getResources().getString(R.string.lims_name).equals(entity.key)
                || context.getResources().getString(R.string.lims_device_name).equals(entity.key)
                || context.getResources().getString(R.string.lims_sample_name).equals(entity.key)) {
            params.put(Constant.BAPQuery.NAME, entity.result);
        } else if (context.getResources().getString(R.string.lims_supplier_code).equals(entity.key) || context.getResources().getString(R.string.lims_materiel_code).equals(entity.key) || context.getResources().getString(R.string.lims_code).equals(entity.key) || context.getResources().getString(R.string.lims_equipment_code).equals(entity.key) || context.getResources().getString(R.string.lims_matCode).equals(entity.key) || context.getResources().getString(R.string.lims_sample_code).equals(entity.key)) {
            params.put(Constant.BAPQuery.CODE, entity.result);
        } else if (context.getResources().getString(R.string.lims_specifications).equals(entity.key)) {
            params.put(Constant.BAPQuery.SPECIFICATIONS, entity.result);
        } else if (context.getResources().getString(R.string.lims_model).equals(entity.key)) {
            params.put(Constant.BAPQuery.MODEL, entity.result);

            params.put(Constant.BAPQuery.BUSI_VERSION, entity.result);
        } else if (context.getResources().getString(R.string.lims_version_number).equals(entity.key)) {
            params.put(Constant.BAPQuery.BUSI_VERSION, entity.result);
        } else if (context.getResources().getString(R.string.lims_produce_batch).equals(entity.key)
                || context.getResources().getString(R.string.lims_batch_number).equals(entity.key)) {
            params.put(Constant.BAPQuery.BATCH_CODE, entity.result);
        } else if (context.getResources().getString(R.string.lims_sampling_point).equals(entity.key)){
            params.put("point-name", entity.result);
        }
        return params;
    }

    public void setSearchOverListener(OnSearchOverListener mOnSearchOverListener) {
        this.mOnSearchOverListener = mOnSearchOverListener;
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
}
