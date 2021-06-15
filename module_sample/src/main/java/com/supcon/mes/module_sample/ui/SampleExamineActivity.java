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
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
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
import com.supcon.mes.module_sample.model.api.SampleExamineAPI;
import com.supcon.mes.module_sample.model.api.SampleExamineReviewAPI;
import com.supcon.mes.module_sample.model.api.SampleRecordResultReviewAPI;
import com.supcon.mes.module_sample.model.api.SampleResultCheckProjectAPI;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.contract.SampleExamineContract;
import com.supcon.mes.module_sample.model.contract.SampleExamineReviewContract;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultReviewContract;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectContract;
import com.supcon.mes.module_sample.presenter.SampleExamineWorkflowPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckProjectPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckWorkflowPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultExamineListPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleExamineAdapter;
import com.supcon.mes.module_sample.ui.input.fragment.SampleFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressLint("CheckResult")
@Router(Constant.AppCode.LIMS_SampleExamine)
@Presenter(value = {SampleResultExamineListPresenter.class, SampleExamineWorkflowPresenter.class})
public class SampleExamineActivity extends BaseRefreshRecyclerActivity<SampleResultCheckProjectEntity> implements SampleExamineContract.View, SampleExamineReviewContract.View {

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
    @BindByTag("rl_refuse")
    RelativeLayout rl_refuse;
    private MyPickerController mDatePickController;

    private SampleExamineAdapter adapter;
    private long sampleId;
    private long dealerId;
    private CustomTextView dealer;
    private CustomEditWithCount memo;
    private CustomDateView planStartTime;
    private long longTime;
    private Dialog dialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_examine;
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
        refreshListController.setOnRefreshListener(() -> {
            adapter.clickPosition = -1;
            doFilter();
        });

        mDatePickController = new MyPickerController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setSecondVisible(true);
        mDatePickController.setCanceledOnTouchOutside(true);
    }

    private void doFilter() {
        presenterRouter.create(SampleExamineAPI.class).getPendingSample("", new HashMap<>());

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
        RxView.clicks(rl_review)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (adapter.selected_data.size() > 0) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("samplesJson", GsonUtil.gsonString(adapter.selected_data));
                        data.put("dealMode", "submit");
                        data.put("signatureInfo", "");
                        presenterRouter.create(SampleExamineReviewAPI.class).recordResultReview(data);
                    } else {
                        ToastUtils.show(context, getResources().getString(R.string.lims_select_one_operate));
                    }

                });
        RxView.clicks(rl_refuse)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (adapter.selected_data.size() > 0) {
                        dialog = new CustomDialog(context)
                                .layout(R.layout.dialog_sample_refuse_dialog).getDialog();
                        Button confirm = dialog.findViewById(R.id.redBtn);
                        confirm.setText(context.getResources().getString(R.string.confirm));
                        Button cancel = dialog.findViewById(R.id.grayBtn);
                        cancel.setText(context.getResources().getString(R.string.cancel));

                        //提交
                        confirm.setOnClickListener(v -> {
                            DealInfoEntity dealInfoEntity = new DealInfoEntity();
                            dealInfoEntity.setDealMemo(memo.getValue());
                            dealInfoEntity.setDealDate(longTime);
                            dealInfoEntity.setDealerId(dealerId);
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                            data.put("samplesJson", GsonUtil.gsonString(adapter.selected_data));
                            data.put("dealInfo", GsonUtil.gsonString(dealInfoEntity));
                            data.put("dealMode", "refuse");
                            data.put("signatureInfo", "");
                            presenterRouter.create(SampleExamineReviewAPI.class).recordResultReview(data);
                        });

                        //取消
                        cancel.setOnClickListener(v -> dialog.dismiss());
                        planStartTime = dialog.findViewById(R.id.planStartTime);

                        //时间
                        planStartTime.setOnChildViewClickListener((childView, action, obj) -> mDatePickController
                                .listener((year, month, day, hour, minute, second) -> {
                                    String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                                    longTime = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm:ss");
                                    planStartTime.setContent(DateUtil.dateFormat(longTime, "yyyy-MM-dd HH:mm:ss"));
                                })
                                .show(System.currentTimeMillis()));

                        //人
                        dealer = dialog.findViewById(R.id.people);
                        dealer.setOnChildViewClickListener((childView, action, obj) -> {
                            if (action != -1) {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                                bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                                bundle.putString(Constant.IntentKey.SELECT_TAG, "selectPeople");
                                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                            } else {
                                dealer.setValue("");
                                dealerId = 0;
                            }
                        });
                        longTime = System.currentTimeMillis();
                        planStartTime.setContent(DateUtil.dateFormat(longTime, "yyyy-MM-dd HH:mm:ss"));
                        AccountInfo accountInfo = SupPlantApplication.getAccountInfo();
                        dealerId = accountInfo.staffId;
                        dealer.setValue(accountInfo.staffName);
                        //备注
                        memo = dialog.findViewById(R.id.memo);
                        dialog.show();
                    } else {
                        ToastUtils.show(context, getResources().getString(R.string.lims_select_one_operate));
                    }


                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventInfo EventInfo) {
        doFilter();
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
        adapter = new SampleExamineAdapter(this);
        return adapter;
    }

    @Override
    public void recordResultReviewSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this, entity.message);
        doFilter();
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public void recordResultReviewFailed(String errorMsg) {
        ToastUtils.show(this, errorMsg);
        if (null != dialog) {
            dialog.dismiss();
        }
    }


    @Override
    public void getPendingSampleSuccess(List entity) {
        refreshListController.refreshComplete(entity);
        if (adapter.clickPosition > 0) {
            if (adapter.clickPosition < entity.size()) {
                contentView.scrollToPosition(adapter.clickPosition);
            } else {
                contentView.scrollToPosition(entity.size() - 1);
            }
        }
    }

    @Override
    public void getPendingSampleFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        ToastUtils.show(this, errorMsg);
    }
}