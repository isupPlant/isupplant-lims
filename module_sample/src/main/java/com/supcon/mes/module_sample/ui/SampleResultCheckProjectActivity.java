package com.supcon.mes.module_sample.ui;


import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.blankj.utilcode.util.JsonUtils;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleRecordResultReviewAPI;
import com.supcon.mes.module_sample.model.api.SampleResultCheckAPI;
import com.supcon.mes.module_sample.model.api.SampleResultCheckProjectAPI;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultReviewContract;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckContract;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectContract;
import com.supcon.mes.module_sample.presenter.SampleResultCheckPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckProjectPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckWorkflowPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleResultCheckAdapter;
import com.supcon.mes.module_sample.ui.adapter.SampleResultCheckProjectAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

@Router(Constant.AppCode.LIMS_SampleResultCheckProject)
@Presenter(value = {SampleResultCheckProjectPresenter.class, SampleResultCheckWorkflowPresenter.class})
public class SampleResultCheckProjectActivity extends BaseRefreshRecyclerActivity implements SampleResultCheckProjectContract.View , SampleRecordResultReviewContract.View {
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
    @BindByTag("rl_review")
    RelativeLayout rl_review;
    @BindByTag("rl_reject")
    RelativeLayout rl_reject;
    @BindByTag("rl_refuse")
    RelativeLayout rl_refuse;

    private SampleResultCheckProjectAdapter adapter;
    private long sampleId;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_check_project;
    }

    @Override
    protected void onInit() {
        super.onInit();
        sampleId = getIntent().getLongExtra(Constant.IntentKey.LIMS_SAMPLE_ID,0);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(com.supcon.mes.module_lims.R.string.middleware_no_data)));
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                doFilter();
            }

        });
    }

    private void doFilter() {
        presenterRouter.create(SampleResultCheckProjectAPI.class).getSampleResultCheckProject(sampleId, new HashMap());

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        ivSearchBtn.setVisibility(View.GONE);
        scanRightBtn.setVisibility(View.GONE);
        titleText.setText(getResources().getString(R.string.check_project));
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
        RxView.clicks(rl_review)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        HashMap<String,Object> data = new HashMap<>();
                        data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                        data.put("dealMode","submit");
                        data.put("signatureInfo","");
                        presenterRouter.create(SampleRecordResultReviewAPI.class).recordResultReview(data);
                    }
                });
        RxView.clicks(rl_reject)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        HashMap<String,Object> data = new HashMap<>();
                        data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                        data.put("dealMode","reject");
                        data.put("signatureInfo","");
                        presenterRouter.create(SampleRecordResultReviewAPI.class).recordResultReview(data);                    }
                });
        RxView.clicks(rl_refuse)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ToastUtils.show(SampleResultCheckProjectActivity.this,"sdf" + adapter.id_selected.size());
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
    }

    @Override
    protected IListAdapter createAdapter() {
        adapter = new SampleResultCheckProjectAdapter(this);
        return adapter;
    }

    @Override
    public void getSampleResultCheckProjectSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getSampleResultCheckProjectFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(this,errorMsg);
    }

    @Override
    public void recordResultReviewSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this,entity.message);
        back();
    }

    @Override
    public void recordResultReviewFailed(String errorMsg) {
        ToastUtils.show(this,errorMsg);
    }

    @Override
    public void recordResultRejectSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this,entity.message);
        back();
    }

    @Override
    public void recordResultRejectFailed(String errorMsg) {
        ToastUtils.show(this,errorMsg);
    }

    @Override
    public void recordResultRefuseSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this,entity.message);
        back();
    }

    @Override
    public void recordResultRefuseFailed(String errorMsg) {
        ToastUtils.show(this,errorMsg);
    }
}