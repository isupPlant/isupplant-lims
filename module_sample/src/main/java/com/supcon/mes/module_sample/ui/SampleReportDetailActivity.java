package com.supcon.mes.module_sample.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.module_lims.model.api.InspectReportDetailAPI;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleReportAPI;
import com.supcon.mes.module_sample.model.api.SampleReportDetailAPI;
import com.supcon.mes.module_sample.model.bean.SampleReportSubmitEntity;
import com.supcon.mes.module_sample.model.contract.SampleReportContract;
import com.supcon.mes.module_sample.model.contract.SampleReportDetailContract;
import com.supcon.mes.module_sample.presenter.SampleReportDetailPresenter;
import com.supcon.mes.module_sample.presenter.SampleReportPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleReportDetailAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */

@Presenter(value = {
        SampleReportDetailPresenter.class,
        SampleReportPresenter.class
})
@Controller(value = {
        GetPowerCodeController.class,
        WorkFlowViewController.class
})
@Router(value = Constant.Router.SAMPLE_REPORT_VIEW)
public class SampleReportDetailActivity extends BaseRefreshActivity implements SampleReportDetailContract.View, SampleReportContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("sampleTv")
    TextView sampleTv;
    @BindByTag("inspectPsTv")
    CustomTextView inspectPsTv;
    @BindByTag("qualityStdTv")
    CustomTextView qualityStdTv;
    @BindByTag("executeStdTv")
    CustomTextView executeStdTv;
    @BindByTag("versionNumberTv")
    CustomTextView versionNumberTv;
    @BindByTag("inspectCheckResultTv")
    CustomTextView inspectCheckResultTv;
    @BindByTag("registerTimeTv")
    CustomTextView registerTimeTv;
    @BindByTag("checkTimeTv")
    CustomTextView checkTimeTv;
    @BindByTag("inspectMaterielTv")
    CustomTextView inspectMaterielTv;
    @BindByTag("inspectBatchTv")
    CustomTextView inspectBatchTv;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("customWorkFlowView")
    CustomWorkFlowView customWorkFlowView;
    SurveyReportEntity reportEntity;
    SampleReportDetailAdapter adapter;
    private PendingEntity pendingEntity;
    @Override
    protected int getLayoutID() {
        return R.layout.ac_sample_report;
    }

    @Override
    protected void onInit() {
        super.onInit();
        Intent intent=getIntent();
        reportEntity= (SurveyReportEntity) intent.getSerializableExtra("reportEntity");
        if (reportEntity!=null){
            pendingEntity=reportEntity.getPending();
        }else {
            pendingEntity = (PendingEntity) intent.getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("样品检验报告单");
        contentView.setLayoutManager(new LinearLayoutManager(context));
//        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        adapter=new SampleReportDetailAdapter(context);
        contentView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(o -> {
                   back();
                });
        if (reportEntity!=null){
            getSampleReportHead();
        }else {
            getSampleReportByPending();
        }

        customWorkFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 0:
                        doSave(workFlowVar);
                        break;
                    case 1:
                        doSubmit(workFlowVar);
                        break;
                    case 2:
                        doSubmit(workFlowVar);
                        break;
                }
            }
        });


    }

    private void getSampleReportHead(){
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pendingEntity.id!=null){
                    customWorkFlowView.setVisibility(View.VISIBLE);
                    getController(WorkFlowViewController.class).initPendingWorkFlowView(customWorkFlowView,pendingEntity.id);
                    getController(GetPowerCodeController.class).initPowerCode(pendingEntity.activityName);
                }
                presenterRouter.create(SampleReportDetailAPI.class).getSampleReport(reportEntity.getId());
            }
        });
    }
    private void getSampleReportByPending(){
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pendingEntity.id!=null){
                    customWorkFlowView.setVisibility(View.VISIBLE);
                    getController(WorkFlowViewController.class).initPendingWorkFlowView(customWorkFlowView,pendingEntity.id);
                    getController(GetPowerCodeController.class).initPowerCode(pendingEntity.activityName);
                }
                presenterRouter.create(SampleReportAPI.class).getSampleReportByPending(pendingEntity.modelId,pendingEntity.id);
            }
        });
    }

    private void setSampleReport(SurveyReportEntity sampleReport){
        if (sampleReport.getSampleId()!=null){
            sampleTv.setText(String.format("%s(%s)",sampleReport.getSampleId().getName(),sampleReport.getSampleId().getCode()));
            inspectPsTv.setValue(sampleReport.getSampleId().getPsId()!=null?sampleReport.getSampleId().getPsId().getName():"");
            registerTimeTv.setValue(sampleReport.getSampleId().getRegisterTime()!=null? DateUtil.dateTimeFormat(sampleReport.getSampleId().getRegisterTime()):"");
            checkTimeTv.setValue(sampleReport.getSampleId().testTime!=null?DateUtil.dateTimeFormat(sampleReport.getSampleId().testTime):"");
            if (sampleReport.getSampleId().getProductId()!=null){
                inspectMaterielTv.setValue(String.format("%s(%s)",sampleReport.getSampleId().getProductId()!=null?sampleReport.getSampleId().getProductId().getName():"",sampleReport.getSampleId().getProductId()!=null?sampleReport.getSampleId().getProductId().getCode():""));
            }
            inspectBatchTv.setValue(sampleReport.getSampleId().getBatchCode());
        }
        qualityStdTv.setValue(sampleReport.getStdVerId()!=null && sampleReport.getStdVerId().getStdId()!=null?sampleReport.getStdVerId().getStdId().getName():"");
        executeStdTv.setValue(sampleReport.getStdVerId()!=null && sampleReport.getStdVerId().getStdId()!=null?sampleReport.getStdVerId().getStdId().getStandard():"");
        versionNumberTv.setValue(sampleReport.getStdVerId()!=null?sampleReport.getStdVerId().getBusiVersion():"");
        inspectCheckResultTv.setValue(sampleReport.getTestResult());

    }
    private void doSave(WorkFlowVar workFlowVar) {

        onLoading("样品报告单保存中...");
        SampleReportSubmitEntity entity = new SampleReportSubmitEntity();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");
        entity.workFlowVar = jsonObject;
        entity.operateType = Constant.Transition.SAVE;
        generateSaveOrSubmit(entity);
    }
    private void generateSaveOrSubmit(SampleReportSubmitEntity entity) {
        entity.deploymentId = pendingEntity.deploymentId+"";
        entity.taskDescription = pendingEntity.taskDescription;
        entity.activityName = pendingEntity.activityName;
        entity.pendingId = pendingEntity.id.toString();
        entity.sampleReport=reportEntity;
        String viewCode="sampleReportView";
        entity.viewCode = "LIMSSample_5.0.0.0_sampleReport_sampleReportView";
        String path = viewCode;
        String _pc_ = getController(GetPowerCodeController.class).getPowerCodeResult();
        Map<String, Object> params = new HashMap<>();
        if (reportEntity.getId() != null) {
            params.put("id", reportEntity.getId());
        }
        params.put("__pc__", _pc_);
        Gson gson = new Gson();
        String s = gson.toJson(entity);
        Log.i("ReportEntity", "->" + s);
        presenterRouter.create(SampleReportDetailAPI.class).submitSampleReport(path, params, entity);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        SampleReportSubmitEntity entity = new SampleReportSubmitEntity();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dec", workFlowVar.dec);
        jsonObject.addProperty("operateType", workFlowVar.operateType);
        jsonObject.addProperty("outcome", workFlowVar.outCome);
        if (workFlowVar.outcomeMapJson != null) {
            jsonObject.addProperty("outcomeMapJson", workFlowVar.outcomeMapJson.toString());
        }
        if (workFlowVar.idsMap != null) {
            jsonObject.addProperty("idsMap", workFlowVar.idsMap.toString());
        }


        if ("驳回".equals(workFlowVar.dec)) {
            onLoading("样品检验报告单驳回中...");
            jsonObject.addProperty("workFlowVarStatus", "cancel");
        } else {
            onLoading("样品检验报告单提交中");
        }
        entity.operateType = Constant.Transition.SUBMIT;
        entity.workFlowVar = jsonObject;
        generateSaveOrSubmit(entity);
    }

    @Override
    public void getSampleReportSuccess(SurveyReportEntity entity) {
        reportEntity=entity;
        setSampleReport(entity);
        Map<String,Object> params=new HashMap<>();
        params.put("reportId",entity.getId());
        params.put("stdVerId",entity.getStdVerId().getStdId().getId());
        params.put("pageNo",1);
        presenterRouter.create(SampleReportDetailAPI.class).getReportComList(params);
    }

    @Override
    public void getSampleReportFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }

    @Override
    public void getReportComListSuccess(StdJudgeSpecListEntity entity) {
        refreshController.refreshComplete();
        if (entity.data!=null && entity.data.result!=null){
            adapter.setList(entity.data.result);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getReportComListFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }

    @Override
    public void submitSampleReportSuccess(SubmitResultEntity entity) {
        onLoadSuccessAndExit("处理成功！", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                back();
            }
        });
    }

    @Override
    public void submitSampleReportFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }

    @Override
    public void getSampleReportByPendingSuccess(SurveyReportEntity entity) {
        reportEntity=entity;
        setSampleReport(reportEntity);
    }

    @Override
    public void getSampleReportByPendingFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}
