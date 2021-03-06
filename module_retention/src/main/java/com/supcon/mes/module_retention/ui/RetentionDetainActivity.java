package com.supcon.mes.module_retention.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.Bind;
import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.middleware.model.bean.Unit;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_lims.model.api.InspectReportDetailAPI;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */

@Presenter(value = {
        RetentionDetailPresenter.class
})
@Controller(value = {
        GetPowerCodeController.class,
        WorkFlowViewController.class
})
@Router(value = Constant.Router.RETENTION_VIEW)
public class RetentionDetainActivity extends BaseRefreshActivity implements RetentionDetailContract.View {

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
    CustomTextView retainQtyTv;
    @BindByTag("retainDateTv")
    CustomTextView retainDateTv;
    @BindByTag("retainDaysTv")
    CustomTextView retainDaysTv;
    @BindByTag("validDateTv")
    CustomTextView validDateTv;
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
    @BindByTag("ll_record_view")
    LinearLayout ll_record_view;
    @BindByTag("observePlanTv")
    TextView observePlanTv;
    @BindByTag("observePlanImg")
    ImageView observePlanImg;
    @BindByTag("ns_scroll")
    NestedScrollView ns_scroll;
    @BindByTag("rlExpand")
    RelativeLayout rlExpand;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_detail_retention;
    }

    RetentionEntity retentionEntity;
    PendingEntity pendingEntity;


    @Override
    protected void onInit() {
        super.onInit();
        Intent intent = getIntent();
        retentionEntity = (RetentionEntity) intent.getSerializableExtra("retentionEntity");
        pendingEntity = (PendingEntity) intent.getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_retention));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        adapter = new RecordAdapter(context);
        contentView.setAdapter(adapter);
        ns_scroll.setNestedScrollingEnabled(false);
    }

    private boolean expand = false;
    private int operate = -1;

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setAutoPullDownRefresh(true);

        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });
        RxView.clicks(rlExpand)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    expand = !expand;
                    if (expand) {
                        expandTv.setText(R.string.lims_shrink);
                        ll_other_info.setVisibility(View.VISIBLE);
                        imageUpDown.setImageResource(com.supcon.mes.module_lims.R.drawable.ic_drop_up);
                    } else {
                        ll_other_info.setVisibility(View.GONE);
                        imageUpDown.setImageResource(com.supcon.mes.module_lims.R.drawable.ic_drop_down);
                        expandTv.setText(R.string.lims_expand);
                    }
                });
        refreshController.setOnRefreshListener(() -> {
            if (retentionEntity != null && pendingEntity == null) {
                pendingEntity = retentionEntity.pending;
                presenterRouter.create(RetentionDetailAPI.class).getRetentionDetailById(retentionEntity.id, null);
            } else if (pendingEntity != null) {
                presenterRouter.create(RetentionDetailAPI.class).getRetentionDetailById(pendingEntity.modelId, pendingEntity.id);
            }
            initPending();
        });


        RxView.clicks(ll_record_view)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (pendingEntity != null && !TextUtils.isEmpty(pendingEntity.openUrl) && pendingEntity.openUrl.contains("retentionEdit")) {
                        return;
                    }
                    if (adapter.selectPosition < 0)
                        ToastUtils.show(context, context.getResources().getString(R.string.lims_please_select_one_data_or_look));
                    else {
                        RecordEntity recordEntity = adapter.getItem(adapter.selectPosition);
                        if (!recordEntity.isStateObserved())
                            ToastUtils.show(context, context.getResources().getString(R.string.lims_status_not));
                        else {
                            Bundle bundle = new Bundle();
                            bundle.putLong("id", recordEntity.id);
                            IntentRouter.go(context, Constant.Router.RETENTION_VIEW_RECORD, bundle);
                        }
                    }
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
                        operate = 1;
                        doSubmit(workFlowVar);
                        break;
                    case 2:
                        operate = 2;
                        doSubmit(workFlowVar);

                        break;
                }
            }
        });


    }

    private void initPending() {
        if (pendingEntity != null && pendingEntity.id != null) {
            if (pendingEntity.openUrl.contains("retentionView")) {
                customWorkFlowView.setVisibility(View.VISIBLE);
                getController(GetPowerCodeController.class).initPowerCode(pendingEntity.activityName);
                getController(WorkFlowViewController.class).initPendingWorkFlowView(customWorkFlowView, pendingEntity.id);
            } else if (pendingEntity.openUrl.contains("retentionEdit")) {
                observePlanTv.setText(R.string.lims_observer_plan);
                observePlanImg.setVisibility(View.GONE);
                adapter.edit = true;
            }
        }
    }

    private void setRetentionEntity() {
        presenterRouter.create(RetentionDetailAPI.class).getRecord(retentionEntity.id);
        if (retentionEntity.sampleId != null && retentionEntity.sampleId.getId() != null) {
            sampleTv.setValue(String.format("%s(%s)", retentionEntity.sampleId.getName(), retentionEntity.sampleId.getCode()));
            pSiteTv.setValue(retentionEntity.sampleId.getPsId() != null && retentionEntity.sampleId.getPsId().getId() != null ? retentionEntity.sampleId.getPsId().getName() : "");
        }
        if (retentionEntity.productId != null && retentionEntity.productId.getId() != null) {
            materialTv.setValue(String.format("%s(%s)", retentionEntity.productId.getName(), retentionEntity.productId.getCode()));
        }
        batchCodeTv.setValue(retentionEntity.batchCode);
        unitTv.setValue(retentionEntity.unitId != null ? retentionEntity.unitId.getName() : "");
        retainQtyTv.setValue(Util.big2(retentionEntity.retainQty));
        retainDateTv.setValue(retentionEntity.retainDate != null ? DateUtil.dateFormat(retentionEntity.retainDate) : "");
        retainDaysTv.setValue(String.format("%s%s", retentionEntity.retainDays != null ? retentionEntity.retainDays.toString() : "", retentionEntity.retainUnit != null ? retentionEntity.retainUnit.getValue() : ""));
        validDateTv.setValue(retentionEntity.validDate != null ? DateUtil.dateFormat(retentionEntity.validDate) : "");
        staffTv.setValue(retentionEntity.staffId != null ? retentionEntity.staffId.getName() : "");
        deptTv.setValue(retentionEntity.deptId != null ? retentionEntity.deptId.getName() : "");
        keeperTv.setValue(retentionEntity.keeperId != null ? retentionEntity.keeperId.getName() : "");
        storeSetTv.setValue(retentionEntity.storeSetId != null ? retentionEntity.storeSetId.getName() : "");

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
        entity.deploymentId = pendingEntity.deploymentId + "";
        entity.taskDescription = pendingEntity.taskDescription;
        entity.activityName = pendingEntity.activityName;
        entity.pendingId = pendingEntity.id.toString();
        entity.retention = retentionEntity;
        String viewCode = "retentionView";
        entity.viewCode = "LIMSRetain_5.0.4.1_retention_retentionView";
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

        if ("??????".equals(workFlowVar.dec)) {
            String tip = getString(R.string.lims_retention) + getString(R.string.lims_reject);
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
        onLoadSuccessAndExit(getString(R.string.lims_deal), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
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
                    EventBus.getDefault().post(new RefreshEvent());
                    back();
                }
            }
        });

    }

    @Override
    public void submitRetentionFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }
}
