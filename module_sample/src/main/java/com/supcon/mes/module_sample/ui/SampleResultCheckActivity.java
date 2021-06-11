package com.supcon.mes.module_sample.ui;


import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleResultCheckAPI;
import com.supcon.mes.module_sample.model.bean.SanpleResultCheckItemEntity;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckContract;
import com.supcon.mes.module_sample.presenter.SampleResultCheckPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleResultCheckAdapter;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

@Router(Constant.AppCode.LIMS_SampleResultCheck)
@Presenter(value = {SampleResultCheckPresenter.class})
public class SampleResultCheckActivity extends BaseRefreshRecyclerActivity<SanpleResultCheckItemEntity> implements SampleResultCheckContract.View {
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("ivSearchBtn")
    ImageView ivSearchBtn;
    @BindByTag("scanRightBtn")
    CustomImageButton scanRightBtn;
    private SampleResultCheckAdapter adapter;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_check;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(com.supcon.mes.module_lims.R.string.middleware_no_data)));
        refreshListController.setOnRefreshListener(this::doFilter);
    }

    private void doFilter() {
        presenterRouter.create(SampleResultCheckAPI.class).getPendingSample("", new HashMap<>());

    }

    @Override
    protected void initView() {
        super.initView();
        ivSearchBtn.setVisibility(View.GONE);
        scanRightBtn.setVisibility(View.GONE);
        titleText.setText(getResources().getString(R.string.sample));
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

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> back());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(this);
    }

    //获取UHF扫描结果
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventInfo EventInfo) {
        doFilter();
    }

    @Override
    protected IListAdapter<SanpleResultCheckItemEntity> createAdapter() {
        adapter = new SampleResultCheckAdapter(this);
        return adapter;
    }

    @Override
    public void getPendingSampleSuccess(List entity) {
        refreshListController.refreshComplete(entity);
        if (adapter.clickPosition > 0) {
            if (adapter.clickPosition < entity.size()) {
                contentView.scrollToPosition(adapter.clickPosition);
            }else {
                contentView.scrollToPosition(entity.size()-1);
            }

        }
    }

    @Override
    public void getPendingSampleFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(this,errorMsg);
    }
}