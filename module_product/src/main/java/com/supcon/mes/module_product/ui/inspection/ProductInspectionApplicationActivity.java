package com.supcon.mes.module_product.ui.inspection;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.controller.InspectionApplicationController;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.listener.OnTabClickListener;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationApi;
import com.supcon.mes.module_lims.presenter.InspectionApplicationPresenter;
import com.supcon.mes.module_lims.ui.adapter.InspectionApplicationAdapter;
import com.supcon.mes.module_product.IntentRouter;
import com.supcon.mes.module_product.R;

import java.util.HashMap;
import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 产品检验申请列表
 */

@Router(Constant.AppCode.LIMS_ProductApplicationInspection)
@Presenter(value = {InspectionApplicationPresenter.class})
@Controller(value = {InspectionApplicationController.class})
public class ProductInspectionApplicationActivity extends BaseRefreshRecyclerActivity<InspectionApplicationEntity> implements InspectionApplicationApi.View {

    private InspectionApplicationAdapter adapter;

    private boolean isWhole = true;
    private Map<String, Object> params = new HashMap<>();

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_product_inspection_application;
    }

    @Override
    protected IListAdapter createAdapter() {
        adapter = new InspectionApplicationAdapter(context);
        return adapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_product_inspection_application));

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
        getController(InspectionApplicationController.class).setTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabClick(boolean isAll) {
                isWhole = isAll;
                goRefresh();
            }
        });

        getController(InspectionApplicationController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                params.clear();
                params.putAll(map);
                goRefresh();
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("id",adapter.getItem(position).getId()+"");
                    bundle.putString("pendingId",adapter.getItem(position).getPending() == null ? "" : adapter.getItem(position).getPending().id+"");
                    IntentRouter.go(context,Constant.AppCode.LIMS_ProductApplicationInspectionDetail,bundle);
                }
            }
        });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionApplicationApi.class).getInspectionApplicationList(BusinessType.PleaseCheck.PRODUCT_PLEASE_CHECK, isWhole, pageIndex, params);
            }
        });
    }

    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void getInspectionApplicationListSuccess(InspectionApplicationListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getInspectionApplicationListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }
}