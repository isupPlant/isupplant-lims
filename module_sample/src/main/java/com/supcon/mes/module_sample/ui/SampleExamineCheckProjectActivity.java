package com.supcon.mes.module_sample.ui;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.MyPickerController;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DealInfoEntity;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.ui.view.CustomEditWithCount;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleExamineCheckProjectAPI;
import com.supcon.mes.module_sample.model.api.SampleRecordResultReviewAPI;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.contract.SampleExamineCheckProjectContract;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultReviewContract;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectContract;
import com.supcon.mes.module_sample.presenter.SampleExamineCheckProjectPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckProjectPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckWorkflowPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleExamineCheckProjectAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressLint("CheckResult")
@Router(Constant.AppCode.LIMS_SampleExamineCheckProject)
@Presenter(value = {SampleExamineCheckProjectPresenter.class, SampleResultCheckWorkflowPresenter.class})
public class SampleExamineCheckProjectActivity extends BaseRefreshRecyclerActivity<SampleResultCheckProjectEntity> implements SampleExamineCheckProjectContract.View, SampleRecordResultReviewContract.View {
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
    private MyPickerController mDatePickController;

    private SampleExamineCheckProjectAdapter adapter;
    private long sampleId;
    private long dealerId;
    private CustomTextView dealer;
    private CustomEditWithCount memo;
    private CustomDateView planStartTime;
    private long longTime;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_examine_check_project;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        sampleId = getIntent().getLongExtra(Constant.IntentKey.LIMS_SAMPLE_ID, 0);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(com.supcon.mes.module_lims.R.string.middleware_no_data)));
        refreshListController.setOnRefreshListener(this::doFilter);

        mDatePickController = new MyPickerController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);
    }

    private void doFilter() {
        presenterRouter.create(SampleExamineCheckProjectAPI.class).getSampleResultCheckProject(sampleId, new HashMap<>());

    }

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
                .subscribe(o -> {
                    if (adapter.selected_data.size() > 0) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                        data.put("dealMode", "active");
                        data.put("signatureInfo", "");
                        presenterRouter.create(SampleRecordResultReviewAPI.class).recordResultReview(data);
                    }else {
                        ToastUtils.show(context,getResources().getString(R.string.lims_select_one_operate));
                    }

                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectData(SelectDataEvent selectDataEvent) {
        if (selectDataEvent != null) {
            if ("selectPeople".equals(selectDataEvent.getSelectTag())) {
                ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
                dealerId = contactEntity.staffId;
                dealer.setValue(contactEntity.name);
            }
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> back());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected IListAdapter createAdapter() {
        adapter = new SampleExamineCheckProjectAdapter(this);
        return adapter;
    }

    @Override
    public void getSampleResultCheckProjectSuccess(List entity) {
        refreshListController.refreshComplete(entity);
    }

    @Override
    public void getSampleResultCheckProjectFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(this, errorMsg);
    }

    @Override
    public void recordResultReviewSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this, entity.message);
        EventInfo.postEvent(EventInfo.refreshSampleList,0);
        back();
    }

    @Override
    public void recordResultReviewFailed(String errorMsg) {
        ToastUtils.show(this, errorMsg);
    }

}