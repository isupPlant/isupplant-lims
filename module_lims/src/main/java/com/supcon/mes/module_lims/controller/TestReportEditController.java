package com.supcon.mes.module_lims.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.FilterInputType;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.api.DeploymentAPI;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
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
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.event.QualityStandardEvent;
import com.supcon.mes.module_lims.event.StdVerComEvent;
import com.supcon.mes.module_lims.event.TestRequestNoEvent;
import com.supcon.mes.module_lims.model.api.FirstStdVerAPI;
import com.supcon.mes.module_lims.model.api.InspectReportDetailAPI;
import com.supcon.mes.module_lims.model.api.QualityStdIdByConclusionAPI;
import com.supcon.mes.module_lims.model.api.TestNumAPI;
import com.supcon.mes.module_lims.model.api.TestReportEditSubmitAPI;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.FirstStdVerEntity;
import com.supcon.mes.module_lims.model.bean.InspectIdEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;
import com.supcon.mes.module_lims.model.bean.QualityStdConclusionEntity;
import com.supcon.mes.module_lims.model.bean.SpecLimitEntity;
import com.supcon.mes.module_lims.model.bean.StdIdEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;
import com.supcon.mes.module_lims.model.bean.TestNumEntity;
import com.supcon.mes.module_lims.model.bean.TestReportEditHeadEntity;
import com.supcon.mes.module_lims.model.bean.TestReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.TestRequestNoEntity;
import com.supcon.mes.module_lims.model.contract.FirstStdVerContract;
import com.supcon.mes.module_lims.model.contract.QualityStdIdByConclusionContract;
import com.supcon.mes.module_lims.model.contract.TestNumContract;
import com.supcon.mes.module_lims.model.contract.TestReportEditSubmitContract;
import com.supcon.mes.module_lims.presenter.FirstStdVerPresenter;
import com.supcon.mes.module_lims.presenter.QualityStdIdByConclusionPresenter;
import com.supcon.mes.module_lims.presenter.TestNumPresenter;
import com.supcon.mes.module_lims.presenter.TestReportEditSubmitPresenter;
import com.supcon.mes.module_lims.ui.adapter.TestReportEditPtAdapter;
import com.supcon.mes.module_lims.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/11/3
 * class name
 */

@Presenter(value = {QualityStdIdByConclusionPresenter.class, TestReportEditSubmitPresenter.class,
        FirstStdVerPresenter.class,QualityStdIdByConclusionPresenter.class, TestNumPresenter.class, DeploymentPresenter.class})
public class TestReportEditController extends BaseViewController implements QualityStdIdByConclusionContract.View,
        TestReportEditSubmitContract.View, FirstStdVerContract.View, TestNumContract.View, DeploymentContract.View {

    @BindByTag("ctTestRequestNo")
    CustomTextView ctTestRequestNo;

    @BindByTag("ctBusinessType")
    CustomTextView ctBusinessType;

    @BindByTag("ctMateriel")
    CustomTextView ctMateriel;

    @BindByTag("ctBatchNumber")
    CustomTextView ctBatchNumber;

    @BindByTag("csTestConclusion")
    CustomSpinner csTestConclusion;

    @BindByTag("ctQualityStd")
    CustomTextView ctQualityStd;

    @BindByTag("ctGetPoint")
    CustomTextView ctGetPoint;

    @BindByTag("ctUnit")
    CustomTextView ctUnit;

    @BindByTag("ctTestNumber")
    CustomTextView ctTestNumber;

    @BindByTag("ctRequestTestDepartment")
    CustomTextView ctRequestTestDepartment;

    @BindByTag("ctTestPeople")
    CustomTextView ctTestPeople;

    @BindByTag("ctTestDepartment")
    CustomTextView ctTestDepartment;

    @BindByTag("cdTestTime")
    CustomDateView cdTestTime;

    @BindByTag("ctSupplier")
    CustomTextView ctSupplier;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("imageUpDown")
    ImageView imageUpDown;

    @BindByTag("expandTv")
    TextView expandTv;

    @BindByTag("ll_other_info")
    LinearLayout ll_other_info;

    @BindByTag("customWorkFlowView")
    CustomWorkFlowView customWorkFlowView;

    @BindByTag("llReference")
    LinearLayout llReference;

    private TableHeadDataOverListener mTableHeadDataOverListener;
    private QualityChangeListener mQualityChangeListener;
    private TestProjectChangeListener mTestProjectChangeListener;
    private OnRequestHeadListener mOnRequestHeadListener;
    private TestReportEditHeadEntity entity;
    private List<StdJudgeSpecEntity> ptList;
    private List<QualityStdConclusionEntity> conclusionList;
    private List<String> stringConclusionList;
    private List<String> deletePtIds;
    private List<String> myDeleteList = new ArrayList<>();
    private TestReportEditPtAdapter adapter;
    private boolean expand=false;

    GetPowerCodeController powerCodeController;
    WorkFlowViewController workFlowViewController;
    DatePickController datePickController;
    SinglePickController mSinglePickController;

    private ScriptEngine engine; //执行js 代码的实体对象
    private int workFlowType = -1;
    private int type = -1;//产品、来料、其他用1，2，3来表示

    private String inspectId = "";
    private String stdVerId = "";
    private PendingEntity pendingEntity;
    private InspectIdEntity inspectIdEntity;
    private TestRequestNoEntity testRequestNoEntity;
    private Long mDeploymentId;
    private String activityName;
    private String from = "";
    private TableTypeIdEntity tableType;

    public TestReportEditController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        super.initView();

        deletePtIds = new ArrayList<>();
        deletePtIds.clear();
        myDeleteList.clear();

        ptList = new ArrayList<>();
        conclusionList = new ArrayList<>();
        stringConclusionList = new ArrayList<>();
        initRecycler();

        powerCodeController=new GetPowerCodeController(context);
        workFlowViewController=new WorkFlowViewController();

        datePickController = new DatePickController((Activity) context);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);

        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);



        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("javascript");

            String path1 = getAssetsCacheFile(context, "numeric.js");
            String path2 = getAssetsCacheFile(context, "numeral.min.js");
            String path3 = getAssetsCacheFile(context, "jstat.min.js");
            String path4 = getAssetsCacheFile(context, "formula.js");
            String path5 = getAssetsCacheFile(context, "generalCal.js");

            FileReader reader1 = new FileReader(path1);
            FileReader reader2 = new FileReader(path2);
            FileReader reader3 = new FileReader(path3);
            FileReader reader4 = new FileReader(path4);
            FileReader reader5 = new FileReader(path5);

            engine.eval(reader1);
            engine.eval(reader2);
            engine.eval(reader3);
            engine.eval(reader4);
            engine.eval(reader5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();

        RxView.clicks(imageUpDown)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(o->{
                    expand=!expand;
                    if (expand){
                        expandTv.setText(R.string.lims_shrink);
                        ll_other_info.setVisibility(View.VISIBLE);
                        imageUpDown.setImageResource(R.drawable.ic_drop_up);
                    }else {
                        ll_other_info.setVisibility(View.GONE);
                        imageUpDown.setImageResource(R.drawable.ic_drop_down);
                        expandTv.setText(R.string.lims_expand);
                    }
                });

        ctTestRequestNo.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putInt("type",type);
                bundle.putString("selectTag",ctTestRequestNo.getTag()+"");
                IntentRouter.go(context,Constant.AppCode.LIMS_TestRequestNoRef,bundle);
            }
        });

        //检验人选择监听
        ctTestPeople.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                    bundle.putString(Constant.IntentKey.SELECT_TAG, ctTestPeople.getTag() + "");
                    IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                } else {
                    BaseLongIdNameEntity checkStaffIdEntity = new BaseLongIdNameEntity();
                    checkStaffIdEntity.setId(null);
                    checkStaffIdEntity.setName("");
                    entity.setCheckStaffId(checkStaffIdEntity);
                }
            }
        });

        //检验部门选择监听
        ctTestDepartment.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    BaseLongIdNameEntity department = new BaseLongIdNameEntity();
                    department.setId(null);
                    department.setName("");
                    entity.setCheckDeptId(department);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG, ctTestDepartment.getTag() + "");
                IntentRouter.go(context, Constant.Router.DEPART_SELECT, bundle);
            }
        });

        //请检时间选择监听
        cdTestTime.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    entity.setCheckTime(null);
                    return;
                }
                datePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        cdTestTime.setContent(dateStr);
                        //更换表头中请检时间的值
                        entity.setCheckTime(DateUtil.dateFormat(dateStr,"yyyy-MM-dd HH:mm:ss"));
                    }
                }).show(new Date().getTime());
            }
        });

        //质量标准
        ctQualityStd.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    entity.getStdVerId().setStdId(null);
                    csTestConclusion.setContent("");
                }else {
                    //跳转请求质量标准的页面
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isReport",true);
                    bundle.putString("id", entity.getStdVerId().getStdId().getAsId().getId()+"");
                    bundle.putSerializable("existItem", new ArrayList<>());
                    bundle.putString(Constant.IntentKey.SELECT_TAG, ctQualityStd.getTag() + "");
                    IntentRouter.go(context, Constant.AppCode.LIMS_QualityStdVerRef, bundle);
                }

            }
        });

        //结论选择
        csTestConclusion.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    entity.setCheckResult("");
                }else {
                    stringConclusionList.clear();
                    for (QualityStdConclusionEntity conclusionEntity : conclusionList) {
                        stringConclusionList.add(conclusionEntity.getName());
                    }
                    mSinglePickController.list(stringConclusionList)
                            .listener(new SinglePicker.OnItemPickListener() {
                                @Override
                                public void onItemPicked(int index, Object item) {
                                    entity.setCheckResult(stringConclusionList.get(index));
                                    setConclusionColor(entity.getCheckResult(),false);
                                }
                            }).show();
                 }
                }

        });

        adapter.setDispValueChangeListener(new TestReportEditPtAdapter.DispValueChangeListener() {
            @Override
            public void dispValueChange(String value, int position) {
                List<SpecLimitEntity> specLimitList = null;
                ptList.get(position).dispValue = value;
                String resultGrade = "";
                String specLimitListStr = ptList.get(position).specLimitListStr;
                if (!StringUtil.isEmpty(specLimitListStr) && !specLimitListStr.equals("[]")){
                    specLimitList = GsonUtil.jsonToList(specLimitListStr, SpecLimitEntity.class);
                }
                if (!StringUtil.isEmpty(value) && specLimitList != null && specLimitList.size()>0){
                    Object[] specListsArr = specLimitList.toArray();
                    try {
                        Invocable invoke = (Invocable) engine;
                        Object gradeDetermine = invoke.invokeFunction("gradeDetermine", value, specListsArr, specLimitListStr, null);
                        resultGrade = (String) gradeDetermine;
                        ptList.get(position).checkResult = resultGrade;
                        adapter.notifyDataSetChanged();
                        setConclusionColor("auto",true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        adapter.setConclusionChangeListener(new TestReportEditPtAdapter.ConclusionChangeListener() {
            @Override
            public void conclusionChangeClick() {
                setConclusionColor("auto",true);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 2){
                    if (((StdJudgeSpecEntity)adapter.getList().get(position)).id != null){
                        deletePtIds.add(((StdJudgeSpecEntity)adapter.getList().get(position)).id+"");
                    }

                    adapter.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        RxView.clicks(llReference)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ptList.size(); i++) {
                            if (i != ptList.size()-1){
                                sb.append(ptList.get(i).reportName+",");
                            }else {
                                sb.append(ptList.get(i).reportName);
                            }

                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isRadio",true);
                        bundle.putString("stdVerId",entity.getStdVerId().getId()+"");
                        bundle.putString("reportNames",sb.toString());
                        bundle.putString("selectTag",llReference.getTag()+"");
                        IntentRouter.go(context,Constant.AppCode.LIMS_StdVerComRef,bundle);
                    }
                });

        customWorkFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action){
                    case 0:
                        workFlowType = 0;
                        doSave(workFlowVar);
                        break;
                    case 1:
                        workFlowType = 1;
                        if ("cancel".equals(workFlowVar.outcomeMapJson.get(0).type)){
                            new CustomDialog(context)
                                    .twoButtonAlertDialog(context.getResources().getString(R.string.lims_to_void_bill))
                                    .bindView(R.id.grayBtn, context.getResources().getString(com.supcon.mes.middleware.R.string.common_cancel))
                                    .bindView(R.id.redBtn, context.getResources().getString(com.supcon.mes.middleware.R.string.common_sure))
                                    .bindClickListener(R.id.grayBtn, v -> {
                                    }, true)
                                    .bindClickListener(R.id.redBtn, v -> doSubmit(workFlowVar), true)
                                    .show();
                        }else {
                            doSubmit(workFlowVar);
                        }
                        break;
                    case 2:
                        workFlowType = 1;
                        if (checkSubmit()){
                            doSubmit(workFlowVar);
                        }
                        break;
                }
            }
        });
    }

    public void setIsFrom(String from){
        this.from = from;
    }

    public void setTableType(TableTypeIdEntity tableType){
        this.tableType = tableType;
    }

    //工作流--保存
    private void doSave(WorkFlowVar workFlowVar){
        String view=getView();
        ((BaseActivity)context).onLoading(view+context.getResources().getString(R.string.lims_saving));
        TestReportSubmitEntity entity = new TestReportSubmitEntity();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");
        entity.workFlowVar = jsonObject;
        entity.operateType = Constant.Transition.SAVE;
        generateSaveOrSubmit(entity);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        TestReportSubmitEntity entity = new TestReportSubmitEntity();
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

        String view=getView();
        if (context.getResources().getString(R.string.reject).equals(workFlowVar.dec)) {
            ((BaseActivity)context).onLoading(view+context.getResources().getString(R.string.lims_reject));
            jsonObject.addProperty("outcomeType", "cancel");
        }else if (context.getResources().getString(R.string.lims_to_void).equals(workFlowVar.dec)){
            ((BaseActivity)context).onLoading(view+context.getResources().getString(R.string.lims_cancellation));
            jsonObject.addProperty("outcomeType", "cancel");
        }else {
            ((BaseActivity)context).onLoading(view+context.getResources().getString(R.string.lims_submitting));
        }
        entity.operateType = Constant.Transition.SUBMIT;
        entity.workFlowVar = jsonObject;
        generateSaveOrSubmit(entity);
    }

    private void generateSaveOrSubmit(TestReportSubmitEntity entity){
        if (from.equals("add")){
            entity.deploymentId = mDeploymentId+"";
            entity.activityName = activityName;
            entity.taskDescription = context.getResources().getString(R.string.lims_edit);
            this.entity.setTableTypeId(tableType);
        }else {
            entity.deploymentId = pendingEntity.deploymentId+"";
            entity.taskDescription = pendingEntity.taskDescription;
            entity.activityName = pendingEntity.activityName;
            entity.pendingId = pendingEntity.id.toString();
        }
        entity.inspectReport=this.entity;
        myDeleteList.clear();
        for (int i = 0; i < deletePtIds.size(); i++) {
            if (!deletePtIds.get(i).equals("null") ){
                myDeleteList.add(deletePtIds.get(i));
            }
        }

        for (int i = myDeleteList.size(); i >= 1; i--) {
            for (int j = 0; j < ptList.size(); j++) {
                if (myDeleteList.get(i).equals(ptList.get(j).id+"")){
                    myDeleteList.remove(i);
                    break;
                }
            }
        }
        //删除检验项目的ids
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < myDeleteList.size(); i++) {
            if (i < myDeleteList.size() - 1) {
                sb.append(myDeleteList.get(i)).append(",");
            } else if (i == myDeleteList.size() - 1) {
                sb.append(myDeleteList.get(i));
            }
        }
        entity.dgDeletedIds.addProperty(getDg(),sb.length() > 0 ? sb.toString() : null);
        entity.dgList.addProperty(getDg(),GsonUtil.gsonString(ptList));
        String viewCode=getViewCode();
        entity.viewCode = "QCS_5.0.0.0_inspectReport_"+viewCode;
        String path = viewCode;
        String _pc_ = powerCodeController.getPowerCodeResult();
        Map<String, Object> params = new HashMap<>();
        if (this.entity.getId() != null) {
            params.put("id", this.entity.getId());
        }
        params.put("__pc__", _pc_);
        Gson gson = new Gson();
        String s = gson.toJson(entity);
        Log.i("ReportEntity", "->" + s);
        presenterRouter.create(TestReportEditSubmitAPI.class).submitInspectReport(path, params, entity);
    }

    String getView(){
        String view="";
        if (type==1){
            view=context.getResources().getString(R.string.lims_product_test_report);
        }else if (type==2){
            view=context.getResources().getString(R.string.lims_incoming_test_report);
        }else if (type==3){
            view=context.getResources().getString(R.string.lims_other_test_report);
        }
        return view;
    }

    String getViewCode(){
        String viewCode="";
        if (type==1){
            viewCode="manuInspReportEdit";
        }else if (type==2){
            viewCode="purchInspReportEdit";
        }else if (type==3){
            viewCode="otherInspReportEdit";
        }
        return viewCode;
    }

     String getDg(){
        String dg = "";
        if (type == 1){
            dg = "dg1591145511105";
        }else if (type == 2){
            dg = "dg1589174214149";
        }else if (type == 3){
            dg = "dg1591949908424";
        }
        return dg;
    }

    private void setToast(String content){
        ToastUtils.show(context,content);
    }

    private boolean checkSubmit(){
        if (StringUtil.isEmpty(ctTestPeople.getContent().trim())){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_11));
            return false;
        }

        if (StringUtil.isEmpty(ctTestDepartment.getContent().trim())){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_10));
            return false;
        }

        if (StringUtil.isEmpty(cdTestTime.getContent().trim())){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_9));
            return false;
        }

        if (StringUtil.isEmpty(ctQualityStd.getContent().trim())){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_6));
            return false;
        }

        if (StringUtil.isEmpty(csTestConclusion.getContent().trim())){
            setToast(context.getResources().getString(R.string.lims_test_conclusion_cannot_be_empty));
            return false;
        }

        if (ptList.size() < 1){
            setToast(context.getResources().getString(R.string.lims_Inspection_item_data_of_inspection_report_cannot_be_empty));
            return false;
        }
        return true;

    }

    public void setTableHead(int type, TestReportEditHeadEntity entity,TableHeadDataOverListener mTableHeadDataOverListener){
        this.mTableHeadDataOverListener = mTableHeadDataOverListener;
        this.entity = entity;
        this.type = type;
        if (null != entity){
            pendingEntity = entity.getPending();
            if (entity.getInspectId() != null){
                //检验申请单号
                ctTestRequestNo.setContent(entity.getInspectId().getTableNo() == null ?
                        "" : entity.getInspectId().getTableNo());
                //业务类型
                ctBusinessType.setContent(entity.getInspectId().getBusiTypeId() == null ?
                        "" : entity.getInspectId().getBusiTypeId().getName() == null ?
                        "" : entity.getInspectId().getBusiTypeId().getName());
                //采样点
                ctGetPoint.setContent(entity.getInspectId().getPsId() == null ?
                        "" : entity.getInspectId().getPsId().getName() == null ?
                        "" : entity.getInspectId().getPsId().getName());
                //请检数量
                ctTestNumber.setContent(entity.getInspectId().quantity == null ? "" : Util.big2(entity.getInspectId().quantity));
                //请检部门
                ctRequestTestDepartment.setContent(entity.getInspectId().getApplyDeptId() == null ? ""
                        : entity.getInspectId().getApplyDeptId().getName() == null ?
                        "" : entity.getInspectId().getApplyDeptId().getName());
                //计量单位
                ctUnit.setContent(entity.getInspectId().getProdId() == null ?
                        "" : entity.getInspectId().getProdId().getMainUnit() == null ?
                        "" : entity.getInspectId().getProdId().getMainUnit().getName());
                //供应商
                ctSupplier.setContent(entity.getInspectId().vendorId == null ?
                        "" : StringUtil.isEmpty(entity.getInspectId().vendorId.getName()) ?
                        "" : entity.getInspectId().vendorId.getName());
                //物料
                if (null != entity.getInspectId().getProdId()){
                    if (!StringUtil.isEmpty(entity.getInspectId().getProdId().getName()) && !StringUtil.isEmpty(entity.getInspectId().getProdId().getCode())){
                        ctMateriel.setContent(entity.getInspectId().getProdId().getName()+"("+entity.getInspectId().getProdId().getCode()+")");
                    }else {
                        if (StringUtil.isEmpty(entity.getInspectId().getProdId().getName()) && StringUtil.isEmpty(entity.getInspectId().getProdId().getCode())){
                            ctMateriel.setContent("");
                        }else {
                            if (StringUtil.isEmpty(entity.getInspectId().getProdId().getName())){
                                ctMateriel.setContent(entity.getInspectId().getProdId().getCode());
                            }else {
                                ctMateriel.setContent(entity.getInspectId().getProdId().getName());
                            }
                        }
                    }
                }else {
                    ctMateriel.setContent("");
                }
            }else {
                ctTestRequestNo.setContent("");
                ctBusinessType.setContent("");
                ctGetPoint.setContent("");
                ctMateriel.setContent("");
                ctUnit.setContent("");
                ctTestNumber.setContent("");
                ctRequestTestDepartment.setContent("");
                ctTestPeople.setContent("");
                ctTestDepartment.setContent("");
                ctSupplier.setContent("");
            }

            //批号
            ctBatchNumber.setContent(entity.getBatchCode() == null ? "" : entity.getBatchCode());
            //请检时间
            cdTestTime.setContent(entity.getCheckTime() == null ? "" : DateUtil.dateFormat(entity.getCheckTime(),"yyyy-MM-dd HH:mm:ss"));
            //质量标准
            ctQualityStd.setContent(entity.getStdVerId() == null ?
                    "" : entity.getStdVerId().getStdId() == null ?
                    "" : entity.getStdVerId().getStdId().getName() == null ?
                    "" : entity.getStdVerId().getStdId().getName());
            //检验结论
            csTestConclusion.setContent(entity.getCheckResult() == null ? "" : entity.getCheckResult());

            //请检人
            ctTestPeople.setContent(entity.getCheckStaffId() == null ?
                    "" : entity.getCheckStaffId().getName() == null ?
                    "" : entity.getCheckStaffId().getName());
            //检验部门
            ctTestDepartment.setContent(entity.getCheckDeptId() == null ?
                    "" : entity.getCheckDeptId().getName() == null ?
                    "" : entity.getCheckDeptId().getName());

            setWorkFlow(entity.getPending());
        }

        presenterRouter.create(QualityStdIdByConclusionAPI.class).getStdVerGradesByStdVerId(this.entity.getStdVerId().getId()+"");

    }

    public void setTablePt(StdJudgeSpecListEntity entity){
        llReference.setVisibility(View.VISIBLE);
        ptList.clear();
        ptList.addAll(entity.data.result);
        adapter.setList(ptList);
        adapter.setNeedLab(this.entity.getInspectId() != null && (this.entity.getInspectId().getNeedLab() == null ? false :
                this.entity.getInspectId().getNeedLab()));
        adapter.setConclusionOption(conclusionList);
        adapter.notifyDataSetChanged();
        setConclusionColor(this.entity.getCheckResult() == null ? "" : this.entity.getCheckResult(),true);
    }

    public void setStartTabHead(int type,TableHeadDataOverListener mTableHeadDataOverListener){
        this.type = type;
        this.mTableHeadDataOverListener = mTableHeadDataOverListener;
        this.entity = new TestReportEditHeadEntity();

        ctTestPeople.setContent(SupPlantApplication.getAccountInfo().staffName);
        ctTestDepartment.setContent(SupPlantApplication.getAccountInfo().getDepartmentName());
        cdTestTime.setContent(DateUtil.dateFormat(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
        ctQualityStd.setEditable(false);
        ctTestRequestNo.setEditable(true);
        csTestConclusion.setEditable(false);
        llReference.setVisibility(View.GONE);

        String processKey = "";
        if (type == 1){
            processKey = "manuInspectReportWorkFlow";
        }else if (type == 2){
            processKey = "purchInspectReportWorkFlow";
        }else if (type == 3){
            processKey = "otherInspectReportWorkFlow";
        }
        presenterRouter.create(DeploymentAPI.class).getCurrentDeployment(processKey);
    }

    private void initRecycler(){
        ptList.clear();
        adapter = new TestReportEditPtAdapter(context);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.setNestedScrollingEnabled(false);
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
        contentView.setAdapter(adapter);
    }

    private void setStartWorkFlow(Long id, String menuName){
        mDeploymentId = id;
        activityName = menuName;
        powerCodeController.initPowerCode(type == 3 ? "start_xrl1zg5" : type == 2 ? "start_wcguvzx" : type == 1 ? "start_f4jgu4z" : "");
        workFlowViewController.initStartWorkFlowView(customWorkFlowView,id);
    }

    //设置工作流
    private void setWorkFlow(PendingEntity pendingEntity){
        if (pendingEntity!=null && pendingEntity.id!=null){
            powerCodeController.initPowerCode(pendingEntity.activityName);
            workFlowViewController.initPendingWorkFlowView(customWorkFlowView, pendingEntity.id);
            customWorkFlowView.setVisibility(View.VISIBLE);
        }else {
            customWorkFlowView.setVisibility(View.GONE);
        }
    }


    public void setQualityChangeListener(QualityChangeListener mQualityChangeListener){
        this.mQualityChangeListener = mQualityChangeListener;
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectDataEvent(SelectDataEvent selectDataEvent){
        if (selectDataEvent.getEntity() instanceof ContactEntity) {  //请检人 eventBus 回调
            ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctTestPeople.getTag() + "")) {
                    ctTestPeople.setValue(contactEntity.name);
                    //更换表头中的请检人的数据
                    BaseLongIdNameEntity checkStaffIdEntity = new BaseLongIdNameEntity();
                    checkStaffIdEntity.setId(contactEntity.getStaffId());
                    checkStaffIdEntity.setName(contactEntity.name);
                    entity.setCheckStaffId(checkStaffIdEntity);
                }
            }
        }else if (selectDataEvent.getEntity() instanceof DepartmentEntity) {
            DepartmentEntity departmentEntity = (DepartmentEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctTestDepartment.getTag() + "")) {
                    ctTestDepartment.setValue(departmentEntity.getName());
                    //更换表头中请检部门的数据
                    BaseLongIdNameEntity department = new BaseLongIdNameEntity();
                    department.setId(departmentEntity.getId());
                    department.setName(departmentEntity.getName());
                    entity.setCheckDeptId(department);
                }
            }
        }else if (selectDataEvent.getEntity() instanceof QualityStandardEvent) {  // 质量标准 eventBus 回调
            QualityStandardEvent qualityStandardEvent = (QualityStandardEvent) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctQualityStd.getTag() + "")) {
                    List<QualityStandardReferenceEntity> list = qualityStandardEvent.getList();
                    if (list.size() == 1){
                        StdVerIdEntity stdVerIdEntity = new StdVerIdEntity();
                        stdVerIdEntity.setBusiVersion(list.get(0).getBusiVersion());//版本号
                        stdVerIdEntity.setId(list.get(0).getId()); //质量标准外层ID
                        StdIdEntity stdIdEntity = new StdIdEntity();
                        stdIdEntity.setId(list.get(0).getStdId().getId());//质量标准ID
                        stdIdEntity.setStandard(StringUtil.isEmpty(list.get(0).getStdId().getStandard()) ? "" : list.get(0).getStdId().getStandard());//执行标准
                        stdIdEntity.setName(StringUtil.isEmpty(list.get(0).getStdId().getName()) ? "" : list.get(0).getStdId().getName());//质量标准
                        stdVerIdEntity.setStdId(stdIdEntity);
                        entity.setStdVerId(stdVerIdEntity);
                        ctQualityStd.setContent(entity.getStdVerId() == null ?
                                "" : entity.getStdVerId().getStdId() == null ?
                                "" : entity.getStdVerId().getStdId().getName() == null ?
                                "" : entity.getStdVerId().getStdId().getName());
                        if (null != mQualityChangeListener){
                            mQualityChangeListener.qualityChangeClick(entity.getInspectId().getId()+"", entity.getStdVerId().getId()+"");
                        }
                    }
                }
            }

        }else if (selectDataEvent.getEntity() instanceof StdVerComEvent){
            StdVerComEvent stdVerComEvent =  (StdVerComEvent)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(llReference.getTag() + "")){
                    List<StdVerComIdEntity> list = stdVerComEvent.getList();
                    if (list.size() > 0){
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ptList.size(); i++) {
                            sb.append(ptList.get(i).reportName).append(",");
                        }
                        for (int i = 0; i < list.size(); i++) {
                            if (i != list.size() -1){
                                sb.append(list.get(i).getReportName()).append(",");
                            }else {
                                sb.append(list.get(i).getReportName());
                            }
                        }
                        if (null != mTestProjectChangeListener){
                            mTestProjectChangeListener.testProjectChangeClick(this.entity.getInspectId().getId()+"",
                                    this.entity.getStdVerId().getId()+"",sb.toString());
                        }
                    }
                }
            }
        }else if (selectDataEvent.getEntity() instanceof TestRequestNoEvent){
            TestRequestNoEvent event = (TestRequestNoEvent)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctTestRequestNo.getTag()+"")){
                    List<TestRequestNoEntity> list = event.getList();
                    if (list.size() == 1){
                        testRequestNoEntity = list.get(0);
                        ctTestRequestNo.setContent(testRequestNoEntity.getTableNo());
                        ctBusinessType.setContent(testRequestNoEntity.getBusiTypeId() == null ? "" : testRequestNoEntity.getBusiTypeId().getName());
                        ctGetPoint.setContent(testRequestNoEntity.getPsId() == null ? "" : testRequestNoEntity.getPsId().getName());
                        ctMateriel.setContent(testRequestNoEntity.getProdId() == null ? "" : testRequestNoEntity.getProdId().getName()+"("+testRequestNoEntity.getProdId().getCode()+")");
                        ctUnit.setContent(testRequestNoEntity.getProdId() == null ? "" : testRequestNoEntity.getProdId().getMainUnit() == null ? "" : testRequestNoEntity.getProdId().getMainUnit().getName());
                        ctBatchNumber.setContent(StringUtil.isEmpty(testRequestNoEntity.getBatchCode()) ? "" : testRequestNoEntity.getBatchCode());
                        ctTestPeople.setContent(testRequestNoEntity.getApplyStaffId() == null ? "" : testRequestNoEntity.getApplyStaffId().getName());
                        ctTestDepartment.setContent(testRequestNoEntity.getApplyDeptId() == null ? "" : testRequestNoEntity.getApplyDeptId().getName());
                        cdTestTime.setContent(testRequestNoEntity.getApplyTime() == null ? "" : DateUtil.dateFormat(testRequestNoEntity.getApplyTime(),"yyyy-MM-dd HH:mm:ss"));
                        ctRequestTestDepartment.setContent(testRequestNoEntity.getApplyDeptId() == null ? "" : testRequestNoEntity.getApplyDeptId().getName());

                        inspectIdEntity = new InspectIdEntity();
                        inspectIdEntity.setTableNo(testRequestNoEntity.getTableNo());
                        inspectIdEntity.setBusiTypeId(testRequestNoEntity.getBusiTypeId());
                        inspectIdEntity.setPsId(testRequestNoEntity.getPsId());
                        inspectIdEntity.setApplyDeptId(testRequestNoEntity.getApplyDeptId());
                        inspectIdEntity.vendorId = testRequestNoEntity.getVendorId();
                        inspectIdEntity.setProdId(testRequestNoEntity.getProdId());
                        inspectIdEntity.setNeedLab(testRequestNoEntity.getNeedLab());
                        inspectIdEntity.setId(testRequestNoEntity.getId());

                        this.entity.setBatchCode(testRequestNoEntity.getBatchCode());
                        this.entity.setCheckTime(testRequestNoEntity.getApplyTime());
                        this.entity.setCheckStaffId(testRequestNoEntity.getApplyStaffId());
                        this.entity.setCheckDeptId(testRequestNoEntity.getApplyDeptId());

                        inspectId = testRequestNoEntity.getId()+"";

                        presenterRouter.create(TestNumAPI.class).getTestNum(testRequestNoEntity.getId()+""); //获取数量


                    }
                }
            }
        }
    }

    @Override
    public void getStdVerGradesByStdVerIdSuccess(BAP5CommonListEntity entity) {
        conclusionList.clear();
        conclusionList.addAll(entity.data);
        this.mTableHeadDataOverListener.tableHeadOver(inspectId,stdVerId);
    }

    @Override
    public void getStdVerGradesByStdVerIdFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }

    @Override
    public void submitInspectReportSuccess(SubmitResultEntity entity) {
        ((BaseActivity)context).onLoadSuccessAndExit(context.getResources().getString(R.string.lims_deal), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                deletePtIds.clear();
                if (workFlowType == 1){
                    EventBus.getDefault().post(new RefreshEvent());
                    ((BaseActivity)context).back();
                }else {
                    if (from.equals("add")){
                        EventBus.getDefault().post(new RefreshEvent());
                        ((BaseActivity)context).back();
                    }else {
                        if (null != mOnRequestHeadListener){
                            mOnRequestHeadListener.requestHeadClick();
                        }
                    }

                }

            }
        });
    }

    @Override
    public void submitInspectReportFailed(String errorMsg) {
        ((BaseActivity)context).onLoadFailed(errorMsg);
    }

    public void setOnRequestHeadListener(OnRequestHeadListener mOnRequestHeadListener){
        this.mOnRequestHeadListener = mOnRequestHeadListener;
    }

    @Override
    public void getFirstStdVerSuccess(FirstStdVerEntity entity) {
        if (null != entity){
            stdVerId = entity.getStdVerId().getId()+"";

            ctQualityStd.setContent(entity.getStdVerId().getName());
            ctQualityStd.setEditable(true);
            csTestConclusion.setEditable(true);
            this.entity.setStdVerId(entity.getStdVerId());
            this.entity.setInspectId(inspectIdEntity);

            presenterRouter.create(QualityStdIdByConclusionAPI.class).getStdVerGradesByStdVerId(entity.getStdVerId().getId()+""); //获取范围标准
        }else {
            ToastUtils.show(context,context.getResources().getString(R.string.lims_inspection_application_detail_tips_3));
        }

    }

    @Override
    public void getFirstStdVerFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }

    @Override
    public void getTestNumSuccess(TestNumEntity entity) {
        if (null != entity){
            ctTestNumber.setContent(entity.getQuantity() == null ? "" : entity.getQuantity().setScale(2)+"");
            inspectIdEntity.quantity = entity.getQuantity().floatValue();
            presenterRouter.create(FirstStdVerAPI.class).getFirstStdVer( testRequestNoEntity.getId()+""); //获取默认质量标准
        }

    }

    @Override
    public void getTestNumFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }

    @Override
    public void getCurrentDeploymentSuccess(DeploymentEntity entity) {
        String menuName = "";
        if (type == 1){
            menuName = "TaskEvent_1o6ys36";
        }else if (type == 2){
            menuName = "TaskEvent_02g4ihu";
        }else if (type == 3){
            menuName = "TaskEvent_1igkdn3";
        }
        setStartWorkFlow(entity.id,menuName);
    }

    @Override
    public void getCurrentDeploymentFailed(String errorMsg) {

    }

    public interface OnRequestHeadListener{
        void requestHeadClick();
    }


    public interface TableHeadDataOverListener{
        void tableHeadOver(String inspectId, String stdVerId);
    }

    public interface QualityChangeListener{
        void qualityChangeClick(String inspectId, String stdVerId);
    }

    public void setTestProjectChangeListener(TestProjectChangeListener mTestProjectChangeListener){
        this.mTestProjectChangeListener = mTestProjectChangeListener;
    }

    public interface TestProjectChangeListener{
        void testProjectChangeClick(String inspectId, String stdVerId,String reportName);
    }

    //获取文件缓存位置
    public String getAssetsCacheFile(Context context, String fileName) {
        File cacheFile = new File(context.getCacheDir(), fileName);
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile.getAbsolutePath();
    }

    public void setConclusionColor(String checkResult,boolean isAuto){
        if (isAuto){
            boolean isQualified  = true;
            if (null != conclusionList && conclusionList.size() > 0 && null != ptList && ptList.size()>0){
                for (int i = 0; i < ptList.size(); i++) {
                    for (int j = 0; j < conclusionList.size(); j++) {
                        if (!StringUtil.isEmpty(ptList.get(i).checkResult)){
                            if (ptList.get(i).checkResult.equals(conclusionList.get(j).getName())){
                                if (conclusionList.get(j).getStdGrade().getId().equals("LIMSBasic_standardGrade/Unqualified")){
                                    isQualified = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!isQualified){
                        break;
                    }
                }
                if (isQualified){
                    csTestConclusion.setContentTextColor(Color.parseColor("#0BC8C1"));
                }else {
                    csTestConclusion.setContentTextColor(Color.parseColor("#F70606"));
                }
            }else {
                csTestConclusion.setContentTextColor(Color.parseColor("#8f8f8f"));
            }
            if (StringUtil.isEmpty(checkResult)){
                csTestConclusion.setContent("");
            }else {
                if (isQualified){
                    for (QualityStdConclusionEntity conclusionEntity : conclusionList) {
                        if (conclusionEntity.getStdGrade().getId().equals("LIMSBasic_standardGrade/Qualified")){
                            csTestConclusion.setContent(conclusionEntity.getName());
                            this.entity.setCheckResult(conclusionEntity.getName());
                        }
                    }
                }else {
                    for (QualityStdConclusionEntity conclusionEntity : conclusionList) {
                        if (conclusionEntity.getStdGrade().getId().equals("LIMSBasic_standardGrade/Unqualified")){
                            csTestConclusion.setContent(conclusionEntity.getName());
                            this.entity.setCheckResult(conclusionEntity.getName());
                        }
                    }
                }
            }

        }else {
            for (QualityStdConclusionEntity conclusionEntity : conclusionList) {
                if (checkResult.equals(conclusionEntity.getName())){
                    if (conclusionEntity.getStdGrade().getId().equals("LIMSBasic_standardGrade/Qualified")){
                        csTestConclusion.setContentTextColor(Color.parseColor("#0BC8C1"));
                    }else {
                        csTestConclusion.setContentTextColor(Color.parseColor("#F70606"));
                    }
                }
            }
            csTestConclusion.setContent(checkResult);
            this.entity.setCheckResult(checkResult);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
