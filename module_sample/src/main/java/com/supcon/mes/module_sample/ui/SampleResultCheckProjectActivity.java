package com.supcon.mes.module_sample.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.DatePicker;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.MyPickerController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DealInfoEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.ui.view.CustomEditWithCount;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleRecordResultReviewAPI;
import com.supcon.mes.module_sample.model.api.SampleResultCheckProjectAPI;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultReviewContract;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectContract;
import com.supcon.mes.module_sample.presenter.SampleResultCheckProjectPresenter;
import com.supcon.mes.module_sample.presenter.SampleResultCheckWorkflowPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleResultCheckProjectAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressLint("CheckResult")
@Router(Constant.AppCode.LIMS_SampleResultCheckProject)
@Presenter(value = {SampleResultCheckProjectPresenter.class, SampleResultCheckWorkflowPresenter.class})
public class SampleResultCheckProjectActivity extends BaseRefreshRecyclerActivity<SampleResultCheckProjectEntity> implements SampleResultCheckProjectContract.View, SampleRecordResultReviewContract.View {
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
    private MyPickerController mDatePickController;
    private SinglePickController mSinglePickController;

    private SampleResultCheckProjectAdapter adapter;
    private long sampleId;
    private String dealerName;
    private long dealerId;
    private CustomTextView dealer;
    private CustomEditWithCount memo;
    private CustomDateView planStartTime;
    private DateTimePicker.OnYearMonthDayTimePickListener mListener;
    private long longTime;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_check_project;
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
        Date today = null;
        try {
            today = DateUtil.getToday();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year = DateUtil.getYear(today);
        int month = DateUtil.getMonth(today);
        int day = DateUtil.getDay(today);
//        mDatePickController.setSelectedItem(year, month + 1, day, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND));


        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setDividerVisible(false);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.textSize(18);
    }

    private void doFilter() {
        presenterRouter.create(SampleResultCheckProjectAPI.class).getSampleResultCheckProject(sampleId, new HashMap<>());

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
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                    data.put("dealMode", "submit");
                    data.put("signatureInfo", "");
                    presenterRouter.create(SampleRecordResultReviewAPI.class).recordResultReview(data);
                });
        RxView.clicks(rl_reject)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                    data.put("dealMode", "reject");
                    data.put("signatureInfo", "");
                    presenterRouter.create(SampleRecordResultReviewAPI.class).recordResultReview(data);
                });
        RxView.clicks(rl_refuse)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {

                    Dialog dialog = new CustomDialog(context)
                            .layout(R.layout.dialog_sample_refuse_dialog).getDialog();
                    Button confirm = dialog.findViewById(R.id.redBtn);
                    confirm.setText(context.getResources().getString(R.string.confirm));
                    Button cancel = dialog.findViewById(R.id.grayBtn);
                    cancel.setText(context.getResources().getString(R.string.cancel));

                    //提交
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DealInfoEntity dealInfoEntity = new DealInfoEntity();
                            dealInfoEntity.setDealMemo(memo.getValue());
                            dealInfoEntity.setDealDate(longTime);
                            dealInfoEntity.setDealerId(dealerId);
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("sampleTestsJson", GsonUtil.gsonString(adapter.selected_data));
                            data.put("samplesJson", GsonUtil.gsonString(adapter.selected_data));
                            data.put("dealMode", "refuse");
                            data.put("signatureInfo", "");
                            presenterRouter.create(SampleRecordResultReviewAPI.class).recordResultReview(data);
                        }
                    });

                    //取消
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    planStartTime = dialog.findViewById(R.id.planStartTime);

                    //时间
                    planStartTime.setOnChildViewClickListener(new OnChildViewClickListener() {
                        @Override
                        public void onChildViewClick(View childView, int action, Object obj) {
                            mDatePickController
                                    .listener((year, month, day, hour, minute, second) -> {
                                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                                        longTime = DateUtil.dateFormat(dateStr, "yyyy-MM-dd HH:mm:ss");
                                        planStartTime.setContent(DateUtil.dateFormat(longTime, "yyyy-MM-dd HH:mm:ss"));
                                    })
                                    .show(System.currentTimeMillis());
                        }
                    });

                    //人
                    dealer = dialog.findViewById(R.id.people);
                    dealer.setOnChildViewClickListener(new OnChildViewClickListener() {

                        @Override
                        public void onChildViewClick(View childView, int action, Object obj) {
                            if (action != -1) {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                                bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                                bundle.putString(Constant.IntentKey.SELECT_TAG, "selectPeople");
                                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                            } else {
                                dealer.setValue("");
                                dealerName = "";
                                dealerId = 0;
                            }
                        }
                    });

                    //备注
                    memo = dialog.findViewById(R.id.memo);
                    dialog.show();

                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectData(SelectDataEvent selectDataEvent) {
        if (selectDataEvent != null) {
            if ("selectPeople".equals(selectDataEvent.getSelectTag())) {
                ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
                dealerId = contactEntity.staffId;
                dealerName = contactEntity.name;
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
        ToastUtils.show(this, errorMsg);
    }

    @Override
    public void recordResultReviewSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this, entity.message);
        back();
    }

    @Override
    public void recordResultReviewFailed(String errorMsg) {
        ToastUtils.show(this, errorMsg);
    }

    @Override
    public void recordResultRejectSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this, entity.message);
        back();
    }

    @Override
    public void recordResultRejectFailed(String errorMsg) {
        ToastUtils.show(this, errorMsg);
    }

    @Override
    public void recordResultRefuseSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(this, entity.message);
        back();
    }

    @Override
    public void recordResultRefuseFailed(String errorMsg) {
        ToastUtils.show(this, errorMsg);
    }
}