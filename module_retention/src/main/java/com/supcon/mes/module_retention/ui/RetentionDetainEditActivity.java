package com.supcon.mes.module_retention.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.MyPickerController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.DeploymentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.middleware.model.contract.DeploymentContract;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.presenter.DeploymentPresenter;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.module_lims.model.bean.WareStoreEntity;
import com.supcon.mes.module_retention.IntentRouter;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.api.RetentionDetailAPI;
import com.supcon.mes.module_retention.model.bean.RecodeListEntity;
import com.supcon.mes.module_retention.model.bean.RecordEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.bean.RetentionSubmitEntity;
import com.supcon.mes.module_retention.model.contract.RetentionDetailContract;
import com.supcon.mes.module_retention.presenter.RetentionDetailPresenter;
import com.supcon.mes.module_retention.ui.adapter.RecordAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * author huodongsheng
 * on 2021/3/5
 * class name
 */
@Presenter(value = {
        RetentionDetailPresenter.class,
        DeploymentPresenter.class
})
@Controller(value = {
        GetPowerCodeController.class,
        WorkFlowViewController.class
})
@Router(value = LimsConstant.AppCode.retentionEdit)
public class RetentionDetainEditActivity extends BaseRefreshActivity implements RetentionDetailContract.View, DeploymentContract.View{
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("sampleTv")
    CustomTextView sampleTv;
    @BindByTag("pSiteTv")
    CustomTextView pSiteTv;
    @BindByTag("materialTv")
    CustomTextView materialTv;
    @BindByTag("batchCodeTv")
    CustomTextView batchCodeTv;
    @BindByTag("unitTv")
    CustomTextView unitTv;
    @BindByTag("retainQtyTv")
    CustomEditText retainQtyTv;
    @BindByTag("retainDateTv")
    CustomDateView retainDateTv;
    @BindByTag("retainDaysTv")
    CustomEditText retainDaysTv;
    @BindByTag("validDateTv")
    CustomDateView validDateTv;
    @BindByTag("staffTv")
    CustomTextView staffTv;
    @BindByTag("deptTv")
    CustomTextView deptTv;
    @BindByTag("keeperTv")
    CustomTextView keeperTv;
    @BindByTag("storeSetTv")
    CustomTextView storeSetTv;
    @BindByTag("ll_other_info")
    LinearLayout ll_other_info;
    @BindByTag("expandTv")
    TextView expandTv;
    @BindByTag("imageUpDown")
    ImageView imageUpDown;
    @BindByTag("contentView")
    RecyclerView contentView;
    RecordAdapter adapter;
    @BindByTag("customWorkFlowView")
    CustomWorkFlowView customWorkFlowView;
    @BindByTag("observePlanTv")
    TextView observePlanTv;
    @BindByTag("observePlanImg")
    ImageView observePlanImg;
    @BindByTag("remainDaysUnitTv")
    TextView remainDaysUnitTv;
    @BindByTag("rlRemainDaysUnit")
    RelativeLayout rlRemainDaysUnit;
    @BindByTag("generateTv")
    TextView generateTv;
    @BindByTag("addTv")
    TextView addTv;
    List<String> remainDaysList = new ArrayList<>();
    private SinglePickController<String> mPickController;
    private MyPickerController mDatePickController;
    Long mDeploymentId;

    private List<BaseIdValueEntity> remainDaysBaseIdValueEntities = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.ac_detail_retention_edit;
    }

    RetentionEntity retentionEntity;
    PendingEntity pendingEntity;


    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        retentionEntity = (RetentionEntity) intent.getSerializableExtra("retentionEntity");
        pendingEntity = (PendingEntity) intent.getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
        if (intent.hasExtra("deploymentId")) {
            mDeploymentId = intent.getLongExtra("deploymentId", 0);
            add = true;
        }
        remainDaysList.add(getString(R.string.lims_day));
        remainDaysList.add(getString(R.string.lims_month));
        remainDaysList.add(getString(R.string.lims_year));

        remainDaysBaseIdValueEntities.add(new BaseIdValueEntity("LIMSBasic_retainUnit/day", remainDaysList.get(0)));
        remainDaysBaseIdValueEntities.add(new BaseIdValueEntity("LIMSBasic_retainUnit/month", remainDaysList.get(1)));
        remainDaysBaseIdValueEntities.add(new BaseIdValueEntity("LIMSBasic_retainUnit/year", remainDaysList.get(2)));

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_retention));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        contentView.setItemAnimator(null);//去掉item刷新时时的闪烁动画
        adapter = new RecordAdapter(context);
        contentView.setAdapter(adapter);
        retainDaysTv.setInputType(InputType.TYPE_CLASS_NUMBER);


    }

    @Override
    protected void initControllers() {
        super.initControllers();

        mPickController = new SinglePickController<>(this);
        mPickController.textSize(18);

        mDatePickController = new MyPickerController(this);
        mDatePickController.textSize(18);
        mDatePickController.setCycleDisable(false);
        mDatePickController.setCanceledOnTouchOutside(true);
        mDatePickController.setDateOnly(true);
    }

    private boolean expand = false;
    private int operate = -1;
    Long ptTime;
    private List<Long> dgDeletedIds = new ArrayList<>();

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        retainQtyTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });
        RxView.clicks(imageUpDown)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    expand = !expand;
                    if (expand) {
                        expandTv.setText(R.string.lims_shrink);
                        ll_other_info.setVisibility(View.VISIBLE);
                        imageUpDown.setImageResource(R.drawable.ic_drop_up);
                    } else {
                        ll_other_info.setVisibility(View.GONE);
                        imageUpDown.setImageResource(R.drawable.ic_drop_down);
                        expandTv.setText(R.string.lims_expand);
                    }
                });
        staffTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                bundle.putString(Constant.IntentKey.SELECT_TAG, "selectStaff");
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });
        deptTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG, "selectDepart");
                bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                IntentRouter.go(context, Constant.Router.DEPART_SELECT, bundle);
            }
        });
        keeperTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                bundle.putString(Constant.IntentKey.SELECT_TAG, "selectKeeper");
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);

            }
        });
        storeSetTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                IntentRouter.go(context, LimsConstant.AppCode.WARE_STORE_REF);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                RecordEntity entity = adapter.getItem(position);
                if (entity.id != null) {
                    dgDeletedIds.add(entity.id);
                    adapter.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        });
        if (add) {
            setEdit();
            retentionEntity = new RetentionEntity();
            retentionEntity.staffId = new BaseSystemBackEntity();
            retentionEntity.staffId.setId(SupPlantApplication.getAccountInfo().staffId);
            retentionEntity.staffId.setName(SupPlantApplication.getAccountInfo().staffName);

            retentionEntity.keeperId = new BaseSystemBackEntity();
            retentionEntity.keeperId.setId(SupPlantApplication.getAccountInfo().staffId);
            retentionEntity.keeperId.setName(SupPlantApplication.getAccountInfo().staffName);
            retentionEntity.deptId = new DepartmentEntity();
            retentionEntity.deptId.setId(SupPlantApplication.getAccountInfo().departmentId);
            retentionEntity.deptId.setName(SupPlantApplication.getAccountInfo().departmentName);

            setRetentionEntity();
            refreshController.setPullDownRefreshEnabled(false);
            refreshController.setAutoPullDownRefresh(false);

            getController(GetPowerCodeController.class).initPowerCode("start_5qr95zw");
            getController(WorkFlowViewController.class).initStartWorkFlowView(customWorkFlowView, mDeploymentId);
        } else {
            refreshController.setPullDownRefreshEnabled(false);
            refreshController.setAutoPullDownRefresh(true);
        }
        sampleTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                IntentRouter.go(context, LimsConstant.AppCode.SAMPLE_REF);
            }
        });
        RxTextView.textChanges(retainQtyTv.editText())
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
                .map(CharSequence::toString)
                .subscribe(s -> {
                    retentionEntity.retainQty = !TextUtils.isEmpty(s) ? Float.valueOf(s) : null;
                });
        RxView.clicks(addTv)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    mDatePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                        @Override
                        public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                            String dateStr = year + "-" + month + "-" + day;
                            ptTime = DateUtil.dateFormat(dateStr, "yyyy-MM-dd");
                            RecordEntity recordEntity = new RecordEntity();
                            recordEntity.planDate = ptTime;
                            adapter.addData(recordEntity);
                            adapter.notifyItemRangeInserted(adapter.getItemCount(), 1);
                        }
                    }).show(ptTime != null ? ptTime : System.currentTimeMillis());
                });


        RxTextView.textChanges(retainDaysTv.editText())
                .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(o -> {
                    retentionEntity.retainDays = !TextUtils.isEmpty(o) ? Integer.valueOf(o) : null;
                    if (retentionEntity.retainDate != null) {
                        if (retentionEntity.retainDays != null) {
                            setValidDate();
                        } else {
                            validDateTv.setDate("");
                            retentionEntity.validDate = null;
                        }
                    }
                });

        RxView.clicks(generateTv)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (retentionEntity.retainDate != null) {
                        if (retentionEntity.retainDays != null) {
                            showTipDialog();
                        } else {
                            ToastUtils.show(context, getString(R.string.lims_retention_days_tips));
                        }
                    } else {
                        ToastUtils.show(context, getString(R.string.lims_retention_date_tip));
                    }
                });
        retainDateTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action != -1) {
                    mDatePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                        @Override
                        public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                            String dateStr = year + "-" + month + "-" + day;
                            retentionEntity.retainDate = DateUtil.dateFormat(dateStr, "yyyy-MM-dd");
                            retainDateTv.setDate(dateStr);
                            retainDateTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                            if (retentionEntity.retainDays != null) {
                                setValidDate();
                            }
                        }
                    }).show(retentionEntity.retainDate != null ? retentionEntity.retainDate : System.currentTimeMillis());
                }
            }
        });
        validDateTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action != -1) {
                    mDatePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                        @Override
                        public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                            String dateStr = year + "-" + month + "-" + day;
                            retentionEntity.validDate = DateUtil.dateFormat(dateStr, "yyyy-MM-dd");
                            validDateTv.setDate(dateStr);
                            validDateTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                        }
                    }).show(retentionEntity.validDate != null ? retentionEntity.validDate : System.currentTimeMillis());
                }

            }
        });
        unitTv.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                IntentRouter.go(context, LimsConstant.AppCode.SAMPLE_UNIT_REF);
            }
        });

        refreshController.setOnRefreshListener(() -> {
            if (!add) {
                if (retentionEntity != null && pendingEntity == null) {
                    pendingEntity = retentionEntity.pending;
                    presenterRouter.create(RetentionDetailAPI.class).getRetentionDetailById(retentionEntity.id, null);
                } else if (pendingEntity != null) {
                    presenterRouter.create(RetentionDetailAPI.class).getRetentionDetailById(pendingEntity.modelId, pendingEntity.id);
                }
                initPending();
            }
        });

        RxView.clicks(rlRemainDaysUnit)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    showSpinnerSelector(remainDaysUnitTv, remainDaysList);
                });
        customWorkFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 0:
                        operate = 0;
                        doSave(workFlowVar);
                        break;
                    case 1:
                        if (retentionEntity.id!=null){
                            operate = 1;
                            doSubmit(workFlowVar);
                        }else {
                            ToastUtils.show(context,getString(R.string.lims_no_table));
                        }
                        break;
                    case 2:
                        if (check()) {
                            operate = 2;
                            doSubmit(workFlowVar);
                        }
                        break;
                }
            }
        });


    }

    private int selectDateType = 0;

    private void setValidDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(retentionEntity.retainDate));
        if (selectDateType == 0) {
            calendar.add(Calendar.DAY_OF_MONTH, retentionEntity.retainDays);
        } else if (selectDateType == 1) {
            calendar.add(Calendar.MONTH, retentionEntity.retainDays);
        } else if (selectDateType == 2) {
            calendar.add(Calendar.YEAR, retentionEntity.retainDays);
        }
        retentionEntity.validDate = calendar.getTime().getTime();
        retentionEntity.validDate = calendar.getTime().getTime();
        validDateTv.setDate(DateUtil.dateFormat(retentionEntity.validDate, "yyyy-MM-dd"));
        validDateTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
    }

    private void showSpinnerSelector(TextView spinner, List<String> list) {
        int current = !TextUtils.isEmpty(spinner.getText().toString()) ? findPosition(spinner.getText().toString(), list) : 0;
        mPickController
                .list(list)
                .listener((index, item) -> {
                    selectDateType = index;
                    spinner.setText(list.get(index));
                    retentionEntity.retainUnit = remainDaysBaseIdValueEntities.get(index);
                    if (retentionEntity.retainDate != null && retentionEntity.retainDays != null) {
                        setValidDate();
                    }
                })
                .show(current);
    }

    private void showSpinnerSelector(CustomSpinner spinner, List<String> list) {
        int current = !TextUtils.isEmpty(spinner.getContent()) ? findPosition(spinner.getContent(), list) : 0;
        mPickController
                .list(list)
                .listener((index, item) -> {
                    spinner.setContent(list.get(index));
                    timeUnitType = index;
                    spinner.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                })
                .show(current);
    }

    Integer timeInterval;
    int timeUnitType = 0;
    Long currentDate;

    private void showTipDialog() {
        CustomDialog customDialog = new CustomDialog(context);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent); // 除去dialog设置四个圆角出现的黑色背景
        customDialog.layout(R.layout.dialog_retention_date, DisplayUtil.dip2px(300, context), WRAP_CONTENT);
        CustomEditText timeIntervalTv = customDialog.getDialog().findViewById(R.id.timeIntervalTv);
        timeIntervalTv.editText().setFocusable(true);
        timeIntervalTv.editText().setFocusableInTouchMode(true);
        timeIntervalTv.editText().requestFocus();
        if (timeInterval != null) {
            timeIntervalTv.setContent(timeInterval.toString());
        }

        timeIntervalTv.setInputType(InputType.TYPE_CLASS_NUMBER);
        CustomSpinner timeUnitCs = customDialog.getDialog().findViewById(R.id.timeUnitCs);
        RxTextView.textChanges(timeIntervalTv.editText())
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
                .map(CharSequence::toString)
                .subscribe(s -> {
                    timeInterval = !TextUtils.isEmpty(s) ? timeInterval = Integer.parseInt(s) : null;
                });
        timeUnitCs.setContent(remainDaysList.get(timeUnitType));
        timeUnitCs.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        timeUnitCs.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                showSpinnerSelector(timeUnitCs, remainDaysList);
            }
        });

        customDialog.bindClickListener(R.id.cancelTv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        }, true)
                .bindClickListener(R.id.confirmTv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentDate = null;
                        if (adapter.getItemCount() > 0) {
                            for (RecordEntity recordEntity : adapter.getList()) {
                                if (recordEntity.id != null) {
                                    dgDeletedIds.add(recordEntity.id);
                                }
                            }
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                        while (true) {
                            if (currentDate == null) {
                                currentDate = retentionEntity.retainDate;
                                RecordEntity recordEntity = new RecordEntity();
                                recordEntity.planDate = currentDate;
                                adapter.addData(recordEntity);
                                continue;
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date(currentDate));
                            if (timeUnitType == 0) {
                                calendar.add(Calendar.DAY_OF_MONTH, timeInterval);
                            } else if (timeUnitType == 1) {
                                calendar.add(Calendar.MONTH, timeInterval);
                            } else if (timeUnitType == 2) {
                                calendar.add(Calendar.YEAR, timeInterval);
                            }

                            currentDate = calendar.getTimeInMillis();
                            if (currentDate >= retentionEntity.validDate) {
                                adapter.notifyDataSetChanged();
                                break;
                            }
                            RecordEntity recordEntity = new RecordEntity();
                            recordEntity.planDate = currentDate;
                            adapter.addData(recordEntity);
                        }
                    }
                }, true);
        customDialog.show();
    }

    /**
     * 查找当前默认项的位置
     *
     * @return
     */
    private int findPosition(String str, List<String> mfaultTypeName) {
        for (int i = 0; i < mfaultTypeName.size(); i++) {
            String s = mfaultTypeName.get(i);
            if (str.equals(s)) {
                return i;
            }
        }
        return 0;
    }

    private void initPending() {
        if (pendingEntity != null && pendingEntity.id != null) {
            getController(GetPowerCodeController.class).initPowerCode(pendingEntity.activityName);
            getController(WorkFlowViewController.class).initPendingWorkFlowView(customWorkFlowView, pendingEntity.id);
            setEdit();
        }
    }

    private void setEdit() {
        observePlanTv.setText(R.string.lims_observer_plan);
        observePlanImg.setVisibility(View.GONE);
        adapter.edit = true;
        unitTv.setNecessary(true);
        retainDateTv.setNecessary(true);
        staffTv.setNecessary(true);
        staffTv.setEditable(true);
        deptTv.setNecessary(true);
        deptTv.setEditable(true);
        keeperTv.setNecessary(true);
        keeperTv.setEditable(true);
        storeSetTv.setEditable(true);
        storeSetTv.setNecessary(true);
        retainDaysTv.setEditable(true);
        retainDateTv.setEditable(true);
        validDateTv.setEditable(true);
        storeSetTv.setEditable(true);
        retainQtyTv.setEditable(true);
        retainQtyTv.setNecessary(true);
        customWorkFlowView.setVisibility(View.VISIBLE);
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

    }



    private void setRetentionEntity() {
        presenterRouter.create(RetentionDetailAPI.class).getRecord(retentionEntity.id);
        if (retentionEntity.sampleId != null && retentionEntity.sampleId.getId() != null) {
            sampleTv.setValue(String.format("%s(%s)", retentionEntity.sampleId.getName(), retentionEntity.sampleId.getCode()));
            pSiteTv.setValue(retentionEntity.sampleId.getPsId() != null && retentionEntity.sampleId.getPsId().getId() != null ? retentionEntity.sampleId.getPsId().getName() : "");
        }else {
            sampleTv.setEditable(true);
        }
        if (retentionEntity.productId != null && retentionEntity.productId.getId() != null) {
            materialTv.setValue(String.format("%s(%s)", retentionEntity.productId.getName(), retentionEntity.productId.getCode()));
        }
        batchCodeTv.setValue(retentionEntity.batchCode);
        unitTv.setValue(retentionEntity.unitId != null ? retentionEntity.unitId.getName() : "");
        unitTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        unitTv.setEditable(retentionEntity.unitId==null || retentionEntity.unitId.getId()==null);
        retainQtyTv.setContent(Util.big2(retentionEntity.retainQty));
        retainDateTv.setDate(retentionEntity.retainDate != null ? DateUtil.dateFormat(retentionEntity.retainDate) : "");
        retainDateTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        retainDaysTv.setContent(retentionEntity.retainDays != null ? retentionEntity.retainDays.toString() : "");
        if (retentionEntity.retainUnit == null || TextUtils.isEmpty(retentionEntity.retainUnit.getId())) {
            retentionEntity.retainUnit = remainDaysBaseIdValueEntities.get(0);
        }
        remainDaysUnitTv.setText(retentionEntity.retainUnit != null ? retentionEntity.retainUnit.getValue() : "");

        if (retentionEntity.retainUnit!=null && !TextUtils.isEmpty(retentionEntity.retainUnit.getId())){
            for (int i = 0; i < remainDaysBaseIdValueEntities.size(); i++) {
                BaseIdValueEntity baseIdValueEntity=remainDaysBaseIdValueEntities.get(i);
                if (retentionEntity.retainUnit.getId().equals(baseIdValueEntity.getId())){
                    selectDateType=i;
                    break;
                }
            }
        }
        validDateTv.setDate(retentionEntity.validDate != null ? DateUtil.dateFormat(retentionEntity.validDate) : "");
        validDateTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        staffTv.setValue(retentionEntity.staffId != null ? retentionEntity.staffId.getName() : "");
        staffTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        deptTv.setValue(retentionEntity.deptId != null ? retentionEntity.deptId.getName() : "");
        deptTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        keeperTv.setValue(retentionEntity.keeperId != null ? retentionEntity.keeperId.getName() : "");
        keeperTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        storeSetTv.setValue(retentionEntity.storeSetId != null ? retentionEntity.storeSetId.getName() : "");
        storeSetTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
    }

    private boolean check() {
        if (retentionEntity.unitId == null || retentionEntity.unitId.getId() == null) {
            ToastUtils.show(context, getString(R.string.lims_retention_unit_tip));
            return false;
        }
        if (retentionEntity.retainQty == null) {
            ToastUtils.show(context, getString(R.string.lims_retention_quantity_tip));
            return false;
        }
        if (retentionEntity.staffId == null || retentionEntity.staffId.getId() == null) {
            ToastUtils.show(context, getString(R.string.lims_retention_staff_tip));
            return false;
        }
        if (retentionEntity.deptId == null || retentionEntity.deptId.getId() == null) {
            ToastUtils.show(context, getString(R.string.lims_retention_dept_tip));
            return false;
        }

        if (retentionEntity.retainDate == null) {
            ToastUtils.show(context, getString(R.string.lims_retention_date_tip));
            return false;
        }
        if (retentionEntity.keeperId == null || retentionEntity.keeperId.getId() == null) {
            ToastUtils.show(context, getString(R.string.lims_retention_keeper_tip));
            return false;
        }

        return true;
    }

    private void doSave(WorkFlowVar workFlowVar) {
        String tip = getString(R.string.lims_retention) + getString(R.string.lims_saving);
        onLoading(tip);
        RetentionSubmitEntity entity = new RetentionSubmitEntity();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");
        entity.workFlowVar = jsonObject;
        entity.operateType = Constant.Transition.SAVE;
        generateSaveOrSubmit(entity);
    }

    private void generateSaveOrSubmit(RetentionSubmitEntity entity) {
        entity.deploymentId = pendingEntity != null ? pendingEntity.deploymentId + "" : mDeploymentId.toString();
        entity.taskDescription = pendingEntity != null ? pendingEntity.taskDescription : "";
        entity.activityName = pendingEntity != null ? pendingEntity.activityName : "TaskEvent_19t2axy";
        if (pendingEntity!=null && pendingEntity.id != null)
            entity.pendingId = pendingEntity.id.toString();
        entity.retention = retentionEntity;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dgDeletedIds.size(); i++) {
            if (i < dgDeletedIds.size() - 1) {
                sb.append(dgDeletedIds.get(i)).append(",");
            } else if (i == dgDeletedIds.size() - 1) {
                sb.append(dgDeletedIds.get(i));
            }
        }
        entity.dgList.addProperty("dg1592198588916", adapter.getItemCount() > 0 ? adapter.getList().toString() : null);
        entity.dgDeletedIds.addProperty("dg1592198588916", sb.length() > 0 ? sb.toString() : null);
        String viewCode = "retentionEdit";
        entity.viewCode = "LIMSRetain_5.0.4.1_retention_retentionEdit";
        String path = viewCode;
        String _pc_ = getController(GetPowerCodeController.class).getPowerCodeResult();
        Map<String, Object> params = new HashMap<>();
        if (retentionEntity.id != null) {
            params.put("id", retentionEntity.id);
        }

        params.put("__pc__", _pc_);
        Gson gson = new Gson();
        String s = gson.toJson(entity);
        Log.i("RetentionEntity", "->" + s);
        presenterRouter.create(RetentionDetailAPI.class).submitRetention(path, params, entity);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        RetentionSubmitEntity entity = new RetentionSubmitEntity();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dec", workFlowVar.dec);
        jsonObject.addProperty("operateType", workFlowVar.operateType);
        jsonObject.addProperty("outcome", workFlowVar.outCome);
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");

        if (workFlowVar.outcomeMapJson != null) {
            jsonObject.addProperty("outcomeMapJson", workFlowVar.outcomeMapJson.toString());
        }
        if (workFlowVar.idsMap != null) {
            jsonObject.addProperty("idsMap", workFlowVar.idsMap.toString());
        }

        if (operate==1) {
            String tip = getString(R.string.lims_retention) + getString(R.string.lims_cancellation);
            onLoading(tip);
            jsonObject.addProperty("outcomeType", "cancel");
        } else {
            String tip = getString(R.string.lims_retention) + getString(R.string.lims_submitting);
            onLoading(tip);
        }
        entity.operateType = Constant.Transition.SUBMIT;
        entity.workFlowVar = jsonObject;
        generateSaveOrSubmit(entity);
    }

    @Override
    public void getRetentionDetailByIdSuccess(RetentionEntity entity) {
        if (retentionEntity == null)
            pendingEntity = entity.pending;
        retentionEntity = entity;
        setRetentionEntity();
    }

    @Override
    public void getRetentionDetailByIdFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void getRecordSuccess(RecodeListEntity entity) {
        adapter.setList(entity.data.result);
        adapter.notifyDataSetChanged();
        refreshController.refreshComplete();
    }

    @Override
    public void getRecordFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void submitRetentionSuccess(SubmitResultEntity entity) {
        EventBus.getDefault().post(new RefreshEvent());
        onLoadSuccessAndExit(getString(R.string.lims_deal), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                if (retentionEntity.id == null) {
                    back();
                } else {
                    if (operate == 0) {
                        pendingEntity.id = entity.data.pendingId;
                        refreshController.setAutoPullDownRefresh(true);
                        refreshController.setPullDownRefreshEnabled(false);
                        refreshController.refreshBegin();
                        refreshController.setOnRefreshListener(new OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                customWorkFlowView.findViewById(R.id.commentInput).setVisibility(View.GONE);
                                presenterRouter.create(RetentionDetailAPI.class).getRetentionDetailById(retentionEntity.id, null);
                            }
                        });
                    } else {
                        back();
                    }
                }

            }
        });

    }

    @Override
    public void submitRetentionFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }

    boolean add;

    @Override
    public void getCurrentDeploymentSuccess(DeploymentEntity entity) {
        mDeploymentId = entity.id;
        add = true;
        setEdit();
    }

    @Override
    public void getCurrentDeploymentFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventData(SelectDataEvent dataEvent) {
        if ("SampleEntity".equals(dataEvent.getSelectTag())) {
            SampleEntity sampleEntity = (SampleEntity) dataEvent.getEntity();
            retentionEntity.sampleId = sampleEntity;
            retentionEntity.productId = sampleEntity.getProductId();
            retentionEntity.batchCode = sampleEntity.getBatchCode();
            if (retentionEntity.productId != null)
                retentionEntity.unitId = retentionEntity.productId.getSampleUnit();
            if (retentionEntity.sampleId != null && retentionEntity.sampleId.getId() != null) {
                sampleTv.setValue(String.format("%s(%s)", retentionEntity.sampleId.getName(), retentionEntity.sampleId.getCode()));
                pSiteTv.setValue(retentionEntity.sampleId.getPsId() != null && retentionEntity.sampleId.getPsId().getId() != null ? retentionEntity.sampleId.getPsId().getName() : "");
            }
            if (retentionEntity.productId != null && retentionEntity.productId.getId() != null) {
                materialTv.setValue(String.format("%s(%s)", retentionEntity.productId.getName(), retentionEntity.productId.getCode()));
            }
            unitTv.setValue(retentionEntity.unitId != null ? retentionEntity.unitId.getName() : "");
            unitTv.setEditable(!(retentionEntity.unitId != null && retentionEntity.unitId.getId() != null));
            unitTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            batchCodeTv.setValue(retentionEntity.batchCode);
        } else if ("UnitEntity".equals(dataEvent.getSelectTag())) {
            retentionEntity.unitId = (BaseSystemBackEntity) dataEvent.getEntity();
            unitTv.setValue(retentionEntity.unitId != null ? retentionEntity.unitId.getName() : "");
            unitTv.setEditable(false);
            unitTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        } else if ("WareStoreEntity".equals(dataEvent.getSelectTag())) {
            WareStoreEntity wareStoreEntity = (WareStoreEntity) dataEvent.getEntity();
            BaseIntIdNameEntity storeSetId = new BaseIntIdNameEntity();
            storeSetId.setId(wareStoreEntity.id);
            storeSetId.setName(wareStoreEntity.name);
            retentionEntity.storeSetId = storeSetId;
            storeSetTv.setValue(retentionEntity.storeSetId != null ? retentionEntity.storeSetId.getName() : "");
            storeSetTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        } else if ("selectStaff".equals(dataEvent.getSelectTag())) {
            ContactEntity contactEntity = (ContactEntity) dataEvent.getEntity();
            retentionEntity.staffId = new BaseSystemBackEntity();
            retentionEntity.staffId.setId(contactEntity.getStaffId());
            retentionEntity.staffId.setName(contactEntity.getName());
            staffTv.setValue(retentionEntity.staffId != null ? retentionEntity.staffId.getName() : "");
            staffTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);

        } else if ("selectKeeper".equals(dataEvent.getSelectTag())) {
            ContactEntity contactEntity = (ContactEntity) dataEvent.getEntity();
            retentionEntity.keeperId = new BaseSystemBackEntity();
            retentionEntity.keeperId.setId(contactEntity.getStaffId());
            retentionEntity.keeperId.setName(contactEntity.getName());
            keeperTv.setValue(retentionEntity.keeperId != null ? retentionEntity.keeperId.getName() : "");
            keeperTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        } else if ("selectDepart".equals(dataEvent.getSelectTag())) {
            DepartmentEntity departmentEntity = (DepartmentEntity) dataEvent.getEntity();
            retentionEntity.deptId = departmentEntity;
            deptTv.setValue(retentionEntity.deptId != null ? retentionEntity.deptId.getName() : "");
            deptTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
