package com.supcon.mes.module_lims.controller;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.base.controller.IRefreshController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.api.InspectReportDetailAPI;
import com.supcon.mes.module_lims.model.api.StdJudgeSpecAPI;
import com.supcon.mes.module_lims.model.bean.InspectHeadReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.contract.InspectReportDetailContract;
import com.supcon.mes.module_lims.model.contract.StdJudgeSpecContract;
import com.supcon.mes.module_lims.presenter.InspectReportDetailPresenter;
import com.supcon.mes.module_lims.presenter.StdJudgeSpecPresenter;
import com.supcon.mes.module_lims.ui.adapter.InspectReportDetailAdapter;
import com.supcon.mes.module_lims.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */

@Presenter(value = {
        InspectReportDetailPresenter.class,
        StdJudgeSpecPresenter.class
})
public class SurverReportDetailController extends BaseViewController implements InspectReportDetailContract.View, StdJudgeSpecContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("inspectRequestNo")
    CustomTextView inspectRequestNo;
    @BindByTag("inspectbusiTypeTv")
    CustomTextView inspectbusiTypeTv;

    @BindByTag("inspectPsTv")
    CustomTextView inspectPsTv;

    @BindByTag("inspectMaterielTv")
    CustomTextView inspectMaterielTv;

    @BindByTag("inspectUnitTv")
    CustomTextView inspectUnitTv;


    @BindByTag("inspectBatchTv")
    CustomTextView inspectBatchTv;

    @BindByTag("inspectQuantityTv")
    CustomTextView inspectQuantityTv;

    @BindByTag("inspectDeptTv")
    CustomTextView inspectDeptTv;

    @BindByTag("inspectCheckStaffTv")
    CustomTextView inspectCheckStaffTv;

    @BindByTag("inspectCheckDeptTv")
    CustomTextView inspectCheckDeptTv;

    @BindByTag("inspectCheckTimeTv")
    CustomTextView inspectCheckTimeTv;

    @BindByTag("inspectQualityStdTv")
    CustomTextView inspectQualityStdTv;

    @BindByTag("inspectCheckResultTv")
    CustomTextView inspectCheckResultTv;

    @BindByTag("inspectVendorTv")
    CustomTextView inspectVendorTv;

    @BindByTag("vendorLayout")
    LinearLayout vendorLayout;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("customWorkFlowView")
    CustomWorkFlowView customWorkFlowView;

    @BindByTag("imageUpDown")
    ImageView imageUpDown;
    @BindByTag("expandTv")
    TextView expandTv;

    @BindByTag("ll_other_info")
    LinearLayout ll_other_info;

    InspectReportDetailAdapter adapter;
    GetPowerCodeController powerCodeController;
    WorkFlowViewController workFlowViewController;
    private InspectHeadReportEntity reportEntity;
    private PendingEntity pendingEntity;


    public SurverReportDetailController(View rootView) {
        super(rootView);

    }

    @Override
    public void initView() {
        super.initView();

        contentView.setLayoutManager(new LinearLayoutManager(context));
//        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        adapter=new InspectReportDetailAdapter(context);
        contentView.setAdapter(adapter);
        initController();
    }

    private BaseActivity baseActivity;
    private void initController(){
        powerCodeController=new GetPowerCodeController(context);
        workFlowViewController=new WorkFlowViewController();
    }

    private int operate=-1;
    private boolean expand=false;
    @Override
    public void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(o->{
                    baseActivity.back();
                });


        RxView.clicks(imageUpDown)
                .throttleFirst(1000,TimeUnit.MICROSECONDS)
                .subscribe(o->{
                    expand=!expand;
                    if (expand){
                        expandTv.setText("收起全部");
                        ll_other_info.setVisibility(View.VISIBLE);
                        imageUpDown.setImageResource(R.drawable.ic_drop_up);
                    }else {
                        ll_other_info.setVisibility(View.GONE);
                        imageUpDown.setImageResource(R.drawable.ic_drop_down);
                        expandTv.setText("展开全部");
                    }
                });
        customWorkFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 0:
                        operate=0;
                        doSave(workFlowVar);
                        break;
                    case 1:
                        operate=1;
                        doSubmit(workFlowVar);
                        break;
                    case 2:
                        operate=2;
                        doSubmit(workFlowVar);
                        break;
                }
            }
        });

    }
    private IRefreshController refreshController;
    public void setRefreshController(BaseActivity baseActivity,IRefreshController refreshController){
        this.refreshController=refreshController;
        this.baseActivity=baseActivity;
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
    }

    int type=-1;//产品、来料、其他用1，2，3来表示
    public void setReportHead(int type,SurveyReportEntity entity){
        this.type=type;
        this.pendingEntity=entity.pending;
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(InspectReportDetailAPI.class).getInspectHeadReport(entity.getId());
                if (pendingEntity!=null && pendingEntity.id!=null){
                    powerCodeController.initPowerCode(pendingEntity.activityName);
                    workFlowViewController.initPendingWorkFlowView(customWorkFlowView, pendingEntity.id);
                    customWorkFlowView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void setReportPending(int type, PendingEntity pendingEntity){
        this.pendingEntity=pendingEntity;
        this.type=type;
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                presenterRouter.create(InspectReportDetailAPI.class).getInspectReportByPending(pendingEntity.modelId, pendingEntity.id);
                if (pendingEntity.openUrl.contains("ReportView")) {
                    powerCodeController.initPowerCode(pendingEntity.activityName);
                    workFlowViewController.initPendingWorkFlowView(customWorkFlowView, pendingEntity.id);
                    customWorkFlowView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 设置检验报告单详情数据
     * @param entity
     */
    private void setInspectHead(InspectHeadReportEntity entity){
        this.reportEntity=entity;
        inspectRequestNo.setValue(entity.inspectId!=null?entity.inspectId.getTableNo():"");
        inspectbusiTypeTv.setValue(entity.busiTypeId!=null?entity.busiTypeId.getName():"");
        inspectPsTv.setValue(entity.inspectId!=null && entity.inspectId.psId!=null?entity.inspectId.psId.getName():"");
        if (entity.prodId!=null){
            inspectMaterielTv.setValue(String.format("%s(%s)",entity.prodId.getName(),entity.prodId.getCode()));
            inspectUnitTv.setValue(entity.prodId.getMainUnit()!=null?entity.prodId.getMainUnit().getName():"");
        }
        if (type==2){
            vendorLayout.setVisibility(View.VISIBLE);
            inspectVendorTv.setValue(entity.inspectId!=null && entity.inspectId.vendorId!=null?entity.inspectId.vendorId.getName():"");
        }
        inspectBatchTv.setValue(entity.batchCode);
        inspectQuantityTv.setValue(entity.inspectId!=null && entity.inspectId.quantity!=null? Util.big2(entity.inspectId.quantity):"");
        inspectDeptTv.setValue(entity.inspectId!=null && entity.inspectId.getApplyDeptId()!=null?entity.inspectId.getApplyDeptId().getName():"");
        inspectCheckStaffTv.setValue(entity.checkStaffId!=null ?entity.checkStaffId.getName():"");
        inspectCheckDeptTv.setValue(entity.checkDeptId!=null ?entity.checkDeptId.getName():"");
        inspectCheckTimeTv.setValue(entity.checkTime!=null? DateUtil.dateTimeFormat(entity.checkTime):"");
        inspectQualityStdTv.setValue(entity.stdVerId!=null && entity.stdVerId.getStdId()!=null? entity.stdVerId.getStdId().getName():"");
        inspectCheckResultTv.setValue(entity.checkResult);
        if ("不合格".equals(entity.checkResult)){
            inspectCheckResultTv.setValueColor(Color.parseColor("#F70606"));
        }else {
            inspectCheckResultTv.setValueColor(Color.parseColor("#0BC8C1"));
        }
    }

    /**
     * 成功获取检验报告单详情数据
     * @param entity
     */
    @Override
    public void getInspectHeadReportSuccess(InspectHeadReportEntity entity) {
        if (entity!=null){
            setInspectHead(entity);
            Map<String,Object> params=new HashMap<>();
            params.put("inspectReportId",entity.id);
            params.put("pageNo",1);
            presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(params);
//            presenterRouter.create(InspectReportDetailAPI.class).getInspectReportDetails(type,entity.id);
        }
    }

    /**
     * 获取检验报告单详情数据失败
     * @param errorMsg
     */
    @Override
    public void getInspectHeadReportFailed(String errorMsg) {
        refreshController.refreshComplete();
    }

    /**
     * 通过待办id获取检验报告单详情数据成功
     * @param entity
     */
    @Override
    public void getInspectReportByPendingSuccess(InspectReportEntity entity) {
        reportEntity=entity.data;
        this.pendingEntity=reportEntity.pending;
        setInspectHead(reportEntity);
        Map<String,Object> params=new HashMap<>();
        params.put("inspectReportId",reportEntity.id);
        params.put("pageNo",1);
        presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(params);
    }

    /**
     * 通过待办id获取检验报告单详情数据失败
     * @param errorMsg
     */
    @Override
    public void getInspectReportByPendingFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
        refreshController.refreshComplete();
    }


    @Override
    public void getInspectReportDetailsSuccess(InspectReportDetailListEntity entity) {

    }

    @Override
    public void getInspectReportDetailsFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }

    /**
     * 提交检验报告单工作流成功
     * @param entity
     */
    @Override
    public void submitInspectReportSuccess(SubmitResultEntity entity) {
        baseActivity.onLoadSuccessAndExit("处理成功！", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                if(operate==0){
                    pendingEntity.id=entity.data.pendingId;
                    refreshController.setAutoPullDownRefresh(true);
                    refreshController.setPullDownRefreshEnabled(false);
                    refreshController.refreshBegin();
                    refreshController.setOnRefreshListener(new OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            customWorkFlowView.findViewById(R.id.commentInput).setVisibility(View.GONE);
                            presenterRouter.create(InspectReportDetailAPI.class).getInspectHeadReport(reportEntity.id);
                        }
                    });
                }else {
                    EventBus.getDefault().post(new RefreshEvent());
                    baseActivity.back();
                }
            }
        });
    }

    /**
     * 提交检验报告单工作流失败
     * @param errorMsg
     */
    @Override
    public void submitInspectReportFailed(String errorMsg) {
        baseActivity.onLoadFailed(errorMsg);
    }

    /**
     * 获取检验报告单详情pt数据成功
     * @param entity
     */
    @Override
    public void getReportComListSuccess(StdJudgeSpecListEntity entity) {
        refreshController.refreshComplete();
        if (entity.data!=null && entity.data.result!=null){
            adapter.setList(entity.data.result);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取检验报告单详情pt数据失败
     * @param errorMsg
     */
    @Override
    public void getReportComListFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }


    private void doSave(WorkFlowVar workFlowVar) {
        String view=getView();
        baseActivity.onLoading(view+"保存中...");
        InspectReportSubmitEntity entity = new InspectReportSubmitEntity();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");
        entity.workFlowVar = jsonObject;
        entity.operateType = Constant.Transition.SAVE;
        generateSaveOrSubmit(entity);
    }
    private void generateSaveOrSubmit(InspectReportSubmitEntity entity) {
        entity.deploymentId = pendingEntity.deploymentId+"";
        entity.taskDescription = pendingEntity.taskDescription;
        entity.activityName = pendingEntity.activityName;
        entity.pendingId = pendingEntity.id.toString();
        entity.inspectReport=reportEntity;
        String viewCode=getViewCode();
        entity.viewCode = "QCS_5.0.0.0_inspectReport_"+viewCode;
        String path = viewCode;
        String _pc_ = powerCodeController.getPowerCodeResult();
        Map<String, Object> params = new HashMap<>();
        if (reportEntity.id != null) {
            params.put("id", reportEntity.id);
        }
        params.put("__pc__", _pc_);
        Gson gson = new Gson();
        String s = gson.toJson(entity);
        Log.i("ReportEntity", "->" + s);
        presenterRouter.create(InspectReportDetailAPI.class).submitInspectReport(path, params, entity);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        InspectReportSubmitEntity entity = new InspectReportSubmitEntity();
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

        String view=getView();
        if ("驳回".equals(workFlowVar.dec)) {
            baseActivity.onLoading(view+"驳回中...");
            jsonObject.addProperty("workFlowVarStatus", "cancel");
        } else {
            baseActivity.onLoading(view+"提交中");
        }
        entity.operateType = Constant.Transition.SUBMIT;
        entity.workFlowVar = jsonObject;
        generateSaveOrSubmit(entity);
    }
    String getView(){
        String view="";
        if (type==1){
            view="产品检验报告单";
        }else if (type==2){
            view="来料检验报告单";
        }else if (type==3){
            view="其他检验报告单";
        }
        return view;
    }
    String getViewCode(){
        String viewCode="";
        if (type==1){
            viewCode="manuInspReportView";
        }else if (type==2){
            viewCode="purchInspReportView";
        }else if (type==3){
            viewCode="otherInspReportView";
        }
        return viewCode;
    }
}
