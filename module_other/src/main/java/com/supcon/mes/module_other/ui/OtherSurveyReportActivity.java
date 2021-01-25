package com.supcon.mes.module_other.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.DeivceHelper;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.controller.SurveyReportController;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.listener.OnTabClickListener;
import com.supcon.mes.module_lims.model.api.SurveyReportAPI;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.contract.SurveyReportContract;
import com.supcon.mes.module_lims.presenter.SurveyReportPresenter;
import com.supcon.mes.module_lims.ui.adapter.SurveyReportAdapter;
import com.supcon.mes.module_lims.utils.SpaceItemDecoration;
import com.supcon.mes.module_other.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 其他检验报告列表
 */

@Router(Constant.AppCode.LIMS_OtherSurveyReport)
@Presenter(value = {SurveyReportPresenter.class})
@Controller(value = {SurveyReportController.class})
public class OtherSurveyReportActivity extends BaseRefreshRecyclerActivity<SurveyReportEntity> implements SurveyReportContract.View {
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;


    private boolean isWhole = false;

    private Map<String, Object> params = new HashMap<>();

    private SurveyReportAdapter adapter;
    SpaceItemDecoration spaceItemDecoration;
    GridLayoutManager gridLayoutManager;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_other_survey_report;
    }

    @Override
    protected IListAdapter<SurveyReportEntity> createAdapter() {
        adapter = new SurveyReportAdapter(context);
        return adapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_other_inspection_report));

        boolean isPad = DeivceHelper.getInstance().isTabletDevice(SupPlantApplication.getAppContext());
        spaceItemDecoration = new SpaceItemDecoration(10, 2);
        gridLayoutManager = new GridLayoutManager(context, 2);
        if (isPad){
            contentView.setLayoutManager(gridLayoutManager);
            contentView.addItemDecoration(spaceItemDecoration);
        }else {
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

        }
        //getController(SurveyReportController.class).setType(3);
        goRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();
        getController(SurveyReportController.class).setTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabClick(boolean isAll) {
                isWhole = isAll;
                goRefresh();
            }
        });

        getController(SurveyReportController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                params.clear();
                params.putAll(map);
                goRefresh();
            }
        });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(SurveyReportAPI.class).getSurveyReportList(LimsConstant.Report.OTHER_REPORT, isWhole, pageIndex, params);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }
    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void getSurveyReportListSuccess(SurveyReportListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getSurveyReportListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
