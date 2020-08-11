package com.supcon.mes.module_retention.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Unit;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.api.RetentionDetailAPI;
import com.supcon.mes.module_retention.model.bean.RecodeListEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.contract.RetentionDetailContract;
import com.supcon.mes.module_retention.presenter.RetentionDetailPresenter;
import com.supcon.mes.module_retention.ui.adapter.RecordAdapter;

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
@Router(Constant.Router.RETENTION_VIEW)
public class RetentionDetainActivity extends BaseRefreshActivity implements RetentionDetailContract.View {

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
    }

    private boolean expand = false;

    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setAutoPullDownRefresh(true);

        RxView.clicks(imageUpDown)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    expand = !expand;
                    if (expand) {
                        expandTv.setText("收起全部");
                        ll_other_info.setVisibility(View.VISIBLE);
                        imageUpDown.setImageResource(com.supcon.mes.module_lims.R.drawable.ic_drop_up);
                    } else {
                        ll_other_info.setVisibility(View.GONE);
                        imageUpDown.setImageResource(com.supcon.mes.module_lims.R.drawable.ic_drop_down);
                        expandTv.setText("展开全部");
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

    }

    private void initPending() {
        if (pendingEntity != null && pendingEntity.id != null) {
            getController(GetPowerCodeController.class).initPowerCode(pendingEntity.activityName);
            getController(WorkFlowViewController.class).initPendingWorkFlowView(customWorkFlowView, pendingEntity.id);
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
        unitTv.setValue(retentionEntity.unitId != null ? retentionEntity.unitId.name : "");
        retainQtyTv.setValue(Util.big2(retentionEntity.retainQty));
        retainDateTv.setValue(retentionEntity.remainDate != null ? DateUtil.dateFormat(retentionEntity.retainDate) : "");
        retainDaysTv.setValue(String.format("%s%s", retentionEntity.retainDays != null ? retentionEntity.retainDays.toString() : "", retentionEntity.retainUnit != null ? retentionEntity.retainUnit.getValue() : ""));
        validDateTv.setValue(retentionEntity.validDate != null ? DateUtil.dateFormat(retentionEntity.validDate) : "");
        staffTv.setValue(retentionEntity.staffId != null ? retentionEntity.staffId.getName() : "");
        deptTv.setValue(retentionEntity.deptId != null ? retentionEntity.deptId.getName() : "");
        keeperTv.setValue(retentionEntity.keeperId != null ? retentionEntity.keeperId.getName() : "");
        storeSetTv.setValue(retentionEntity.storeSetId != null ? retentionEntity.storeSetId.getName() : "");

    }

    @Override
    public void getRetentionDetailByIdSuccess(RetentionEntity entity) {
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
}
