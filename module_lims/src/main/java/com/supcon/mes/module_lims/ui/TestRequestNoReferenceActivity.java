package com.supcon.mes.module_lims.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.ui.view.CustomMiddleFilterView;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.event.MaterialDateEvent;
import com.supcon.mes.module_lims.event.TestRequestNoEvent;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.api.TestRequestNoAPI;
import com.supcon.mes.module_lims.model.bean.ProdIdEntity;
import com.supcon.mes.module_lims.model.bean.TestRequestNoEntity;
import com.supcon.mes.module_lims.model.bean.VendorIdEntity;
import com.supcon.mes.module_lims.model.contract.TestRequestNoContract;
import com.supcon.mes.module_lims.presenter.TestRequestNoPresenter;
import com.supcon.mes.module_lims.ui.adapter.TestRequestNoAdapter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author huodongsheng
 * on 2020/11/16
 * class name
 */
@Presenter(value = {TestRequestNoPresenter.class})
@Controller(value = {ReferenceController.class})
@Router(value = Constant.AppCode.LIMS_TestRequestNoRef)
public class TestRequestNoReferenceActivity extends BaseRefreshRecyclerActivity<TestRequestNoEntity> implements TestRequestNoContract.View {
    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    @BindByTag("listStatusFilter")
    CustomMiddleFilterView listStatusFilter;
    @BindByTag("ctMaterielCode")
    CustomTextView ctMaterielCode;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("ctSupplier")
    CustomTextView ctSupplier;

    private int type = 0;
    private Map<String, Object> params = new HashMap<>();
    private List<FilterBean> stateList = new ArrayList<>();
    private List<TestRequestNoEntity> list = new ArrayList<>();
    private FilterBean filterBean;
    private TestRequestNoAdapter adapter;
    private String selectTag = "";

    @Override
    protected IListAdapter<TestRequestNoEntity> createAdapter() {
        adapter = new TestRequestNoAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_request_no_reference;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        getController(ReferenceController.class).setSearchTypeList(
                getResources().getString(R.string.lims_document_number),
                getResources().getString(R.string.lims_batch_number),
                getResources().getString(R.string.lims_materiel_name));

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitle.setTitle(getString(R.string.lims_request_reference));

        type = getIntent().getExtras().getInt("type",0);
        selectTag = getIntent().getStringExtra("selectTag");
        if (type == 2){
            ctSupplier.setVisibility(View.VISIBLE);
        }else {
            ctSupplier.setVisibility(View.GONE);
        }

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

        goRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();
        searchTitle.getLeftActionBar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(TestRequestNoAPI.class).getTestRequestNo(type,pageIndex+"",getQuery(),params);
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
                    params.put(Constant.BAPQuery.CHECK_STATE, filterBean.name);
                }
                goRefresh();
            }
        });
        ctMaterielCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    listStatusFilter.setCurrentItem("");
                    ctSupplier.setContent("");
                    searchTitle.hideSearchBtn();
                    params.clear();
                    goRefresh();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("radio",true);
                IntentRouter.go(context,Constant.AppCode.LIMS_MaterialRef,bundle);
            }
        });

        ctSupplier.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    listStatusFilter.setCurrentItem("");
                    ctMaterielCode.setContent("");
                    searchTitle.hideSearchBtn();
                    params.clear();
                    goRefresh();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG,ctSupplier.getTag() + "");
                IntentRouter.go(context, Constant.AppCode.LIMS_CmcPartRef, bundle);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 1){
                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (i != position){
                            adapter.getList().get(i).setSelect(false);
                        }
                    }
                    list.clear();
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (adapter.getList().get(i).isSelect()){
                            list.add(adapter.getList().get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (list.size() > 0){
                        EventBus.getDefault().post(new SelectDataEvent<>(new TestRequestNoEvent(list),selectTag));
                        finish();
                    }else {
                        ToastUtils.show(context,context.getResources().getString(R.string.lims_please_select_at_last_one_data));
                    }

                }
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
        filterBean.name = context.getString(R.string.lims_testing);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getString(R.string.lims_already_test);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getString(R.string.lims_report_formation);
        stateList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = context.getString(R.string.lims_report_review);
        stateList.add(filterBean);

        listStatusFilter.setData(stateList);
    }

    private void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Override
    public void getTestRequestNoSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getTestRequestNoFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    public String getQuery(){
        if (type == 1){
            return "manuInspectRef-query";
        }else if (type == 2){
            return "purchInspectRef-query";
        }else {
            return "otherInspectRef-query";
        }
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMaterialRef(MaterialDateEvent event){
        if (null != event){
            if (event.isRadio()){
                if (event.getData() != null){
                    ProdIdEntity data = event.getData();
                    ctMaterielCode.setContent(data.getCode());

                    listStatusFilter.setCurrentItem("");
                    ctSupplier.setContent("");
                    searchTitle.hideSearchBtn();
                    params.clear();
                    params.put(Constant.BAPQuery.CODE,data.getCode());
                    goRefresh();
                }

            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectDataEvent(SelectDataEvent selectDataEvent){
        if (selectDataEvent.getEntity() instanceof VendorIdEntity){ //供应商 eventBus 回调
            VendorIdEntity vendorIdEntity = (VendorIdEntity)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctSupplier.getTag() + "")){
                    ctSupplier.setValue(vendorIdEntity.getName());

                    listStatusFilter.setCurrentItem("");
                    ctMaterielCode.setContent("");
                    searchTitle.hideSearchBtn();
                    params.clear();
                    params.put("supplier",vendorIdEntity.getName());
                    goRefresh();

                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
