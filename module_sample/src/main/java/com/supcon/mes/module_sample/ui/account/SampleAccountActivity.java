package com.supcon.mes.module_sample.ui.account;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.ui.view.CustomMiddleFilterView;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleAccountAPI;
import com.supcon.mes.module_sample.model.bean.SampleAccountEntity;
import com.supcon.mes.module_sample.model.contract.SampleAccountContract;
import com.supcon.mes.module_sample.presenter.SampleAccountPresenter;
import com.supcon.mes.module_sample.ui.account.adapter.SampleAccountAdapter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * author huodongsheng
 * on 2020/10/15
 * class name
 */
@Router(value = Constant.AppCode.LIMS_SampleQuery)
@Presenter(value = {SampleAccountPresenter.class})
@Controller(value = {ReferenceController.class})
public class SampleAccountActivity extends BaseRefreshRecyclerActivity<SampleAccountEntity> implements SampleAccountContract.View {

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("listStatusFilter")
    CustomMiddleFilterView listStatusFilter;



    private SampleAccountAdapter adapter;
    private Map<String, Object> params = new HashMap<>();
    private List<FilterBean> stateList = new ArrayList<>();
    private FilterBean filterBean;

    @Override
    protected IListAdapter<SampleAccountEntity> createAdapter() {
        adapter = new SampleAccountAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_account;
    }

    @Override
    protected void onInit() {
        super.onInit();
        getController(ReferenceController.class).setSearchTypeList(context.getResources().getString(com.supcon.mes.module_lims.R.string.lims_sample_name),context.getResources().getString(com.supcon.mes.module_lims.R.string.lims_sample_code),context.getResources().getString(com.supcon.mes.module_lims.R.string.lims_batch_number),context.getResources().getString(com.supcon.mes.module_lims.R.string.lims_sampling_point));

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitle.showScan(false);
        searchTitle.setTitle(context.getResources().getString(R.string.lims_sample_account));
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);


        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childLayoutPosition = parent.getChildAdapterPosition(view);
                if (childLayoutPosition == 0) {
                    outRect.set(DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
                } else {
                    outRect.set(DisplayUtil.dip2px(12, context), 0, DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
                }
            }
        });
        ((SimpleItemAnimator)contentView.getItemAnimator()).setSupportsChangeAnimations(false);
        goRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
               .subscribe(new Consumer<Object>() {
                   @Override
                   public void accept(Object o) throws Exception {
                       onBackPressed();
                   }
               });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(SampleAccountAPI.class).getSampleAccountList(pageIndex,params);
            }
        });

        getController(ReferenceController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                listStatusFilter.setCurrentItem("");
                params.clear();
                params.putAll(map);
                goRefresh();
            }
        });


        listStatusFilter.setFilterSelectChangedListener(new CustomFilterView.FilterSelectChangedListener() {
            @Override
            public void onFilterSelected(FilterBean filterBean) {
                params.clear();
                searchTitle.hideSearchBtn();
                if (StringUtil.isEmpty(filterBean.name)){
                    params.clear();
                }else{
                    params.put(Constant.BAPQuery.SAMPLE_STATE, filterBean.name);
                }
                goRefresh();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        stateList.clear();

        filterBean = new FilterBean();
        filterBean.name = "";
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_wait_get_sample);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_wait_received_sample);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_wait_handover);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_wait_test);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_some_have_been_inspected);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_already_test);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_already_reviewed);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_already_refuse);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getResources().getString(R.string.lims_already_cancel);
        stateList.add(filterBean);

        listStatusFilter.setData(stateList);
    }

    private void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Override
    public void getSampleAccountListSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSampleAccountListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete();
    }
}
