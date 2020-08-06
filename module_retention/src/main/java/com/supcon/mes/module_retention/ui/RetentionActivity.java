package com.supcon.mes.module_retention.ui;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.listener.OnTabClickListener;

import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.controller.RetentionController;
import com.supcon.mes.module_retention.model.api.RetentionAPI;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.bean.RetentionListEntity;
import com.supcon.mes.module_retention.model.contract.RetentionContract;
import com.supcon.mes.module_retention.presenter.RetentionPresenter;
import com.supcon.mes.module_retention.ui.adapter.RetentionAdapter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
@Router(Constant.AppCode.LIMS_Retention)
@Presenter({RetentionPresenter.class})
@Controller({RetentionController.class})
public class RetentionActivity extends BaseRefreshRecyclerActivity<RetentionEntity> implements RetentionContract.View {
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    RetentionAdapter adapter;

    Map<String,Object> queryParam=new HashMap<>();
    private boolean all;
    @Override
    protected IListAdapter<RetentionEntity> createAdapter() {
        return adapter=new RetentionAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_retention;
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
        titleText.setText(getString(R.string.lims_retention));
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initListener() {
        super.initListener();

        getController(RetentionController.class).setTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabClick(boolean isAll) {
                all=isAll;
                refreshListController.refreshBegin();
            }
        });

        getController(RetentionController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                queryParam.clear();
                queryParam.putAll(map);
                refreshListController.refreshBegin();
            }
        });
        refreshListController.setOnRefreshPageListener(pageNo->{
            presenterRouter.create(RetentionAPI.class).getRetentionList(pageNo,all,queryParam);
        });
    }

    @Override
    public void getRetentionListSuccess(RetentionListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getRetentionListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}
