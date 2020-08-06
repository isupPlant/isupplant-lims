package com.supcon.mes.module_lims.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.event.InspectionItemEvent;
import com.supcon.mes.module_lims.event.MaterialDateEvent;

import com.supcon.mes.module_lims.event.QualityStandardEvent;
import com.supcon.mes.module_lims.model.bean.ApplyDeptIdEntity;
import com.supcon.mes.module_lims.model.bean.ApplyStaffIdEntity;
import com.supcon.mes.module_lims.model.bean.AvailableStdEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.BusiTypeIdEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectApplicationSubmitEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtEntity;
import com.supcon.mes.module_lims.model.bean.PleaseCheckSchemeEntity;
import com.supcon.mes.module_lims.model.bean.PsIdEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;
import com.supcon.mes.module_lims.model.bean.StdIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdListEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;
import com.supcon.mes.module_lims.model.bean.TemporaryQualityStandardEntity;
import com.supcon.mes.module_lims.model.bean.VendorIdEntity;
import com.supcon.mes.module_lims.model.contract.AvailableStdIdApi;
import com.supcon.mes.module_lims.model.contract.InspectApplicationSubmitApi;
import com.supcon.mes.module_lims.model.contract.InspectionDetailReadyApi;
import com.supcon.mes.module_lims.presenter.AvailableStdPresenter;
import com.supcon.mes.module_lims.presenter.InspectApplicationSubmitPresenter;
import com.supcon.mes.module_lims.presenter.InspectionDetailReadyPresenter;
import com.supcon.mes.module_lims.ui.adapter.QualityStandardAdapter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * author huodongsheng
 * on 2020/7/13
 * class name 检验申请详情Controller
 */

@Presenter(value = {InspectionDetailReadyPresenter.class, AvailableStdPresenter.class, InspectApplicationSubmitPresenter.class})
public class InspectionApplicationDetailController extends BaseViewController implements InspectionDetailReadyApi.View, AvailableStdIdApi.View,
        InspectApplicationSubmitApi.View {

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("cdCheckTime")
    CustomDateView cdCheckTime;

    @BindByTag("ctCheckPeople")
    CustomTextView ctCheckPeople;

    @BindByTag("ctCheckDepartment")
    CustomTextView ctCheckDepartment;

    @BindByTag("ctSamplingPoint")
    CustomTextView ctSamplingPoint;

    @BindByTag("ctBusinessType")
    CustomSpinner ctBusinessType;

    @BindByTag("ctSupplier")
    CustomTextView ctSupplier;

    @BindByTag("ctMateriel")
    CustomTextView ctMateriel;

    @BindByTag("ctMaterielUnit")
    CustomTextView ctMaterielUnit;

    @BindByTag("ceMaterielNum")
    CustomEditText ceMaterielNum;

    @BindByTag("ceMaterielBatchNumber")
    CustomEditText ceMaterielBatchNumber;

    @BindByTag("llStandardReference")
    LinearLayout llStandardReference;

    @BindByTag("llInspectionItems")
    LinearLayout llInspectionItems;

    @BindByTag("recyclerView")
    RecyclerView recyclerView;

    @BindByTag("ivNeedLaboratory")
    ImageView ivNeedLaboratory;

    @BindByTag("rlNeedLaboratory")
    RelativeLayout rlNeedLaboratory;

    @BindByTag("customWorkFlowView")
    CustomWorkFlowView customWorkFlowView;

    private OnRequestHeadListener mOnRequestHeadListener;

    private TemporaryQualityStandardEntity myDefaultQualityStandardEntity;
    private InspectionApplicationDetailHeaderEntity mHeadEntity;
    private QualityStandardAdapter adapter;

    private SinglePickController<String> mBusinessTypeController;
    private DatePickController datePickController;
    GetPowerCodeController powerCodeController;
    WorkFlowViewController workFlowViewController;

    private List<String> businessTypeStrList = new ArrayList<>(); // 用于存放展示时业务类型的集合
    private List<BusiTypeIdEntity> businessTypeList = new ArrayList<>();// 存放提交时业务类型的集合
    private List<InspectionDetailPtEntity> ptList = new ArrayList<>();
    private List<String> deletePtIds = new ArrayList<>();

    private int myQualityStandardPosition;
    private boolean isEdit = false;
    private int inspectionItemPosition;
    private int type = -1;
    int workFlowType = -1;

    public InspectionApplicationDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionDetailReadyApi.class).getIfUpload();

    }

    @Override
    public void initView() {
        super.initView();
        deletePtIds.clear();
        searchTitle.showScan(false);
        searchTitle.getSearchBtn().setVisibility(View.GONE);

        mBusinessTypeController = new SinglePickController<>((Activity) context);
        mBusinessTypeController.textSize(18);

        datePickController = new DatePickController((Activity) context);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);

        powerCodeController=new GetPowerCodeController(context);
        workFlowViewController=new WorkFlowViewController();

        initRecycler();
    }


    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).onBackPressed();
            }
        });

        //请检时间选择监听
        cdCheckTime.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                datePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        cdCheckTime.setContent(dateStr);
                        //更换表头中请检时间的值
                        mHeadEntity.setApplyTime(DateUtil.dateFormat(dateStr,"yyyy-MM-dd HH:mm:ss"));
                    }
                }).show(new Date().getTime());
            }
        });

        //业务类型选择监听
        ctBusinessType.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                showSpinnerSelector(ctBusinessType, businessTypeStrList);
            }
        });

        //请检人选择监听
        ctCheckPeople.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                    bundle.putString(Constant.IntentKey.SELECT_TAG, ctCheckPeople.getTag() + "");
                    IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                } else {
                    ctCheckPeople.setValue("");
                }
            }
        });

        //请检部门选择监听
        ctCheckDepartment.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG, ctCheckDepartment.getTag() + "");
                IntentRouter.go(context, Constant.Router.DEPART_SELECT, bundle);
            }
        });

        //批号输入监听
        RxTextView.textChanges(ceMaterielBatchNumber.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        //更换表头中批号数据
                        mHeadEntity.setBatchCode(charSequence.toString());
                    }
                });

        //数量输入监听
        ceMaterielNum.editText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        RxTextView.textChanges(ceMaterielNum.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence s) throws Exception {
                        //删除“.”后面超过2位后的数据
                        if (s.toString().contains(".")) {
                            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                s = s.toString().subSequence(0,
                                        s.toString().indexOf(".") + 3);
                                ceMaterielNum.setContent(s.toString());
                                ceMaterielNum.editText().setSelection(s.length()); //光标移到最后
                            }
                        }
                        //如果"."在起始位置,则起始位置自动补0
                        if (s.toString().trim().substring(0).equals(".")) {
                            s = "0" + s;
                            ceMaterielNum.setContent(s.toString());
                            ceMaterielNum.editText().setSelection(2);
                        }

                        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                        if (s.toString().startsWith("0")
                                && s.toString().trim().length() > 1) {
                            if (!s.toString().substring(1, 2).equals(".")) {
                                ceMaterielNum.editText().setText(s.subSequence(0, 1));
                                ceMaterielNum.editText().setSelection(1);
                                return;
                            }
                        }

                        if (s.length() > 0){
                            //更换表头中数量的数据
                            if (!s.toString().equals("--")){
                                mHeadEntity.setQuantity(new BigDecimal(s.toString()).setScale(2,BigDecimal.ROUND_DOWN));
                            }

                        }else {
                            mHeadEntity.setQuantity(new BigDecimal("0.00").setScale(2,BigDecimal.ROUND_DOWN));
                        }

                    }
                });
        ceMaterielNum.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ceMaterielNum.setContent("");
                }else {
                    if (TextUtils.isEmpty(ceMaterielNum.getContent())){
                        ceMaterielNum.setContent("--");
                    }
                }
            }
        });

        //采样点参照监听
        ctSamplingPoint.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG, ctSamplingPoint.getTag() + "");
                IntentRouter.go(context, Constant.AppCode.LIMS_PickSiteRefPart, bundle);
            }
        });

        //供应商参照监听
        ctSupplier.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mHeadEntity.setVendorId(new VendorIdEntity());
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IntentKey.SELECT_TAG,ctSupplier.getTag() + "");
                    IntentRouter.go(context, Constant.AppCode.LIMS_CmcPartRef, bundle);
                }

            }
        });

        //（点击物料）物料信息参照监听
        ctMateriel.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                ctMateriel.setContent("");
                Bundle bundle = new Bundle();
                bundle.putBoolean("radio", true);
                IntentRouter.go(context, Constant.AppCode.LIMS_MaterialRef, bundle);
            }
        });

        //物料值改变时的监听
        RxTextView.textChanges(ctMateriel.contentView())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {  //物料为空时，计量单位和批号均为不可编辑状态，并且清空原有的值  并且质量标准也清空
                        if (TextUtils.isEmpty(charSequence.toString().trim()) || charSequence.toString().equals("--")) {
                            ctMaterielUnit.setContent("");
                            ceMaterielBatchNumber.setContent("--");
                            ceMaterielBatchNumber.setEditable(false);
                            ceMaterielBatchNumber.setNecessary(false);
                            ptList.clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            if (isEdit){
                                ceMaterielBatchNumber.setEditable(true);
                                ceMaterielBatchNumber.setNecessary(true);
                            }else {
                                ceMaterielBatchNumber.setEditable(false);
                            }
                        }
                    }
                });

        ceMaterielBatchNumber.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ceMaterielBatchNumber.setContent("");
                }else {
                    if (TextUtils.isEmpty(ceMaterielBatchNumber.getContent())){
                        ceMaterielBatchNumber.setContent("--");
                    }
                }
            }
        });


        // 是否需要实验室 监听
        RxView.clicks(rlNeedLaboratory)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //更换表头需实验室检验数据源
                        mHeadEntity.setNeedLab(!mHeadEntity.getNeedLab());
                        setNeedLaboratory();
                    }
                });

        //质量参照按钮点击
        RxView.clicks(llStandardReference)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (null == mHeadEntity.getProdId() || null == mHeadEntity.getProdId().getId()){
                            ToastUtils.show(context,"请选择物料!");
                            return;
                        }
                        //先请求一个接口
                        presenterRouter.create(com.supcon.mes.module_lims.model.api.AvailableStdIdApi.class).getAvailableStdId(mHeadEntity.getProdId().getId() + "");
                    }
                });

        //质量标准条目点击事件 以及请检方案点击事件
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0) { //条目点击
                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (i != position){
                            adapter.getList().get(i).setSelect(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (action == 1) {//点击某个条目的请检方案
                    myQualityStandardPosition = position;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", adapter.getList().get(position).getStdVerId().getId() + "");
                    bundle.putString(Constant.IntentKey.SELECT_TAG, "pleaseCheckScheme");
                    IntentRouter.go(context, Constant.AppCode.LIMS_InspectProjRef, bundle);
                } else if (action == 2) {
                    //删除质量标准时  记录删除对象的id
                    //adapter.getList().get(position).getId() != null  如果是从质量标准参照页面带过来的质量标准  是没有id的
                    if (adapter.getList().get(position).getId() != null){
                        deletePtIds.add(adapter.getList().get(position).getId()+"");
                    }

                    adapter.remove(position);
                    adapter.notifyDataSetChanged();

                }
            }
        });

        //检验项目按钮监听
        RxView.clicks(llInspectionItems)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        int a = -1;
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (adapter.getList().get(i).isSelect()){
                                a = i;
                            }
                        }
                        if (a != -1){
                            inspectionItemPosition = a;
                            Bundle bundle = new Bundle();
                            bundle.putString("stdVersionId",adapter.getList().get(a).getStdVerId() == null ? "" : adapter.getList().get(a).getStdVerId().getId()+"");
                            bundle.putString(Constant.IntentKey.SELECT_TAG,llInspectionItems.getTag()+"");
                            bundle.putString("stdVerCom",adapter.getList().get(a).getInspStdVerCom() == null ? "" : adapter.getList().get(a).getInspStdVerCom());
                            bundle.putString("inspectStdId",StringUtil.isEmpty(adapter.getList().get(a).getId()+"") ? "" :adapter.getList().get(a).getId()+"");
                            bundle.putString("inspectProjId",adapter.getList().get(a).getInspectProjId() == null ? "" : adapter.getList().get(a).getInspectProjId().getId()+"");
                            bundle.putBoolean("isEdit",isEdit);

                            CustomSwipeLayout ll = (CustomSwipeLayout)recyclerView.getChildAt(a);
                            CustomTextView ctApplicationScheme = ll.findViewById(R.id.ctApplicationScheme);
                            bundle.putBoolean("inspectProjIdIsValuable",StringUtil.isEmpty(ctApplicationScheme.getContent()) || ctApplicationScheme.getContent().equals("--") ? false : true);
                            IntentRouter.go(context,Constant.AppCode.LIMS_InspectComDataByStdVerId,bundle);
                        }else {
                            ToastUtils.show(context,"请先选择一项质量标准");
                        }
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
                        if (workFlowVar.dec.equals("作废")){
                            new CustomDialog(context)
                                    .twoButtonAlertDialog("确认要作废单据吗？")
                                    .bindView(R.id.grayBtn, "取消")
                                    .bindView(R.id.redBtn, "确定")
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
                        if (workFlowVar.dec.equals("通过")){
                            doSubmit(workFlowVar);
                        }else {
                            if (checkSubmit()){
                                doSubmit(workFlowVar);
                            }
                        }

                        break;
                }
            }
        });
    }

    private void initRecycler() {
        ptList.clear();
        adapter = new QualityStandardAdapter(context, ptList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        recyclerView.setAdapter(adapter);

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

    public void setHeardData(int type, InspectionApplicationDetailHeaderEntity entity, OnRequestPtListener mOnRequestPtListener) {
        this.type = type;
        //获取业务类型参照的数据
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionDetailReadyApi.class).getBusinessTypeList(type);
        if (null != entity) {
            mHeadEntity = entity;
            if (null != entity.getPending() && null != entity.getPending().openUrl) {
                if (entity.getPending().openUrl.contains(BusinessType.WorkType.PRODUCT_INSPECT_EDIT)) {
                    setCanEdit();
                } else if (entity.getPending().openUrl.contains(BusinessType.WorkType.PRODUCT_INSPECT_VIEW)) { //表示不可编辑
                    setCannotEdit();
                }else {
                    setCannotEdit();
                }
            } else {
                setCannotEdit();
            }
            setWorkFlow(mHeadEntity.getPending());
            adapter.setEdit(isEdit);
            setMakingRules(entity);
            mOnRequestPtListener.requestPtClick(isEdit);
        }
    }

    public void setPtData(List<InspectionDetailPtEntity> list) {
        ptList = list;
        adapter.setList(ptList);
    }


    private void setCannotEdit() {
        isEdit = false;
        cdCheckTime.setEditable(false);
        ctCheckPeople.setEditable(false);
        ctCheckDepartment.setEditable(false);
        ctSamplingPoint.setEditable(false);
        ctBusinessType.setEditable(false);
        ctSupplier.setEditable(false);
        ctMateriel.setEditable(false);
        ceMaterielNum.setEditable(false);
        ceMaterielBatchNumber.setEditable(false);
        rlNeedLaboratory.setOnClickListener(null);
    }

    private void setCanEdit() {
        isEdit = true;
        cdCheckTime.setEditable(true);
        ctCheckPeople.setEditable(true);
        ctCheckDepartment.setEditable(true);
        ctSamplingPoint.setEditable(true);
        ctBusinessType.setEditable(true);
        ctSupplier.setEditable(true);
        ctMateriel.setEditable(true);
        ceMaterielNum.setEditable(true);
        ceMaterielBatchNumber.setEditable(true);
        recyclerView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
    }

    private void setMakingRules(InspectionApplicationDetailHeaderEntity entity) {
        if (null != entity) {
            if (null != entity.getSourceType()) { //单据来源类型不为空时，表头物料编码、批号不可编辑
                ctMateriel.setEditable(false);
                ceMaterielBatchNumber.setEditable(false);
            }




            //请检时间
            cdCheckTime.setContent(entity.getApplyTime() == null ? "--" : DateUtil.dateFormat(entity.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
            //请检人
            if (null != entity.getApplyStaffId()) {
                ctCheckPeople.setContent(StringUtil.isEmpty(entity.getApplyStaffId().getName()) ? "--" : entity.getApplyStaffId().getName());
            } else {
                ctCheckPeople.setContent("--");
            }
            //请检部门
            if (null != entity.getApplyDeptId()) {
                ctCheckDepartment.setContent(StringUtil.isEmpty(entity.getApplyDeptId().getName()) ? "--" : entity.getApplyDeptId().getName());
            } else {
                ctCheckDepartment.setContent("--");
            }
            //采样点
            if (null != entity.getPsId()) {
                ctSamplingPoint.setContent(StringUtil.isEmpty(entity.getPsId().getName()) ? "--" : entity.getPsId().getName());
            } else {
                ctSamplingPoint.setContent("--");
            }
            //业务类型
            if (null != entity.getBusiTypeId()) {
                ctBusinessType.setContent(StringUtil.isEmpty(entity.getBusiTypeId().getName()) ? "--" : entity.getBusiTypeId().getName());
            } else {
                ctBusinessType.setContent("--");
            }
            //物料编码 名称 计量单位
            if (null != entity.getProdId()) {
                if (!StringUtil.isEmpty(entity.getProdId().getCode()) && !StringUtil.isEmpty(entity.getProdId().getName())) {
                    ctMateriel.setContent(entity.getProdId().getName() + "(" + entity.getProdId().getCode() + ")");
                } else {
                    if (StringUtil.isEmpty(entity.getProdId().getName()) && StringUtil.isEmpty(entity.getProdId().getCode())) {
                        ctMateriel.setContent("--");
                    } else {
                        if (!StringUtil.isEmpty(entity.getProdId().getName())) {
                            ctMateriel.setContent(entity.getProdId().getName());
                        } else if (!StringUtil.isEmpty(entity.getProdId().getCode())) {
                            ctMateriel.setContent(entity.getProdId().getCode());
                        }
                    }

                }

                if (null != entity.getProdId().getSampleUnit()) {
                    ctMaterielUnit.setContent(StringUtil.isEmpty(entity.getProdId().getMainUnit().getName()) ? "--" : entity.getProdId().getMainUnit().getName());
                } else {
                    ctMaterielUnit.setContent("--");
                }

            } else {
                ctMateriel.setContent("--");
                ctMaterielUnit.setContent("--");
            }

            //数量
            ceMaterielNum.setContent(null == entity.getQuantity() ? "--" : entity.getQuantity().setScale(2, BigDecimal.ROUND_DOWN) + "");
            //批号
            ceMaterielBatchNumber.setContent(StringUtil.isEmpty(entity.getBatchCode()) ? "--" : entity.getBatchCode());
            //供应商
            if (null != entity.getVendorId()){
                ctSupplier.setContent(StringUtil.isEmpty(entity.getVendorId().getName()) ? "--" : entity.getVendorId().getName());
            }else {
                ctSupplier.setContent("--");
            }

            if (null != entity.getProdId()){
                if (entity.getProdId().isEnableBatch()){
                    ceMaterielBatchNumber.setEditable(true);
                    ceMaterielBatchNumber.setNecessary(true);
                }else {
                    ceMaterielBatchNumber.setEditable(false);
                    ceMaterielBatchNumber.setNecessary(false);
                }
            }

            //是否实验室检验
            setNeedLaboratory();

        }
    }

    private void setNeedLaboratory() {
        if (mHeadEntity.getNeedLab()) {
            ivNeedLaboratory.setBackgroundResource(R.drawable.ic_check_yes);
        } else {
            ivNeedLaboratory.setBackgroundResource(R.drawable.ic_check_no);
        }
    }

    private void showSpinnerSelector(CustomSpinner spinner, List<String> list) {
        int current = findPosition(spinner.getSpinnerValue(), list);
        mBusinessTypeController
                .list(list)
                .listener((index, item) -> {
                    spinner.setSpinner(list.get(index));
                    spinner.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                    mHeadEntity.setBusiTypeId(businessTypeList.get(index));
                })
                .show(current);
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

    //工作流--保存
    private void doSave(WorkFlowVar workFlowVar){
        String view=getView();
        ((BaseActivity)context).onLoading(view+"保存中...");
        InspectApplicationSubmitEntity entity = new InspectApplicationSubmitEntity();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");
        entity.workFlowVar = jsonObject;

        entity.operateType = Constant.Transition.SAVE;

        generateSaveOrSubmit(entity);
    }

    private void generateSaveOrSubmit(InspectApplicationSubmitEntity entity) {
        entity.deploymentId = mHeadEntity.getPending().deploymentId+"";
        entity.taskDescription = mHeadEntity.getPending().taskDescription;
        entity.activityName = mHeadEntity.getPending().activityName;
        entity.pendingId = mHeadEntity.getPending().id.toString();
        entity.inspect = mHeadEntity;

        JsonObject dgJson = new JsonObject();
        dgJson.addProperty(getDg(),GsonUtil.gsonString(ptList));
        entity.dgList = dgJson;

        //删除质量标准的ids
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < deletePtIds.size(); i++) {
            if (i < deletePtIds.size() - 1) {
                sb.append(deletePtIds.get(i)).append(",");
            } else if (i == deletePtIds.size() - 1) {
                sb.append(deletePtIds.get(i));
            }
        }
        entity.dgDeletedIds.addProperty(getDg(),sb.length() > 0 ? sb.toString() : null);

        String viewCode=getViewCode();
        entity.viewCode = "QCS_5.0.0.0_inspect_"+viewCode;
        String path = viewCode;
        String _pc_ = powerCodeController.getPowerCodeResult();
        Map<String, Object> params = new HashMap<>();
        if (mHeadEntity.getId()!= null) {
            params.put("id", mHeadEntity.getId());
        }
        params.put("__pc__", _pc_);
//        Gson gson = new Gson();
//        String s = gson.toJson(entity);
//        Log.i("eeeeeeeeeeeeeeeee", "->" + s);
//        String s1 = gson.toJson(entity);
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectApplicationSubmitApi.class).submitInspectApplication(path, params, entity);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        InspectApplicationSubmitEntity entity = new InspectApplicationSubmitEntity();
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
            ((BaseActivity)context).onLoading(view+"驳回中...");
            jsonObject.addProperty("workFlowVarStatus", "cancel");
        }else if ("作废".equals(workFlowVar.dec)){
            ((BaseActivity)context).onLoading(view+"作废中...");
            jsonObject.addProperty("workFlowVarStatus", "cancel");
        }else {
            ((BaseActivity)context).onLoading(view+"提交中...");
        }
        entity.operateType = Constant.Transition.SUBMIT;
        entity.workFlowVar = jsonObject;
        generateSaveOrSubmit(entity);
    }


    @Override
    public void getBusinessTypeListSuccess(BusinessTypeListEntity entity) {
        businessTypeStrList.clear();
        businessTypeList.clear();
        if (null != entity) {
            if (null != entity.data) {
                if (null != entity.data.result) {
                    businessTypeList.addAll(entity.data.result);
                    for (int i = 0; i < businessTypeList.size(); i++) {
                        businessTypeStrList.add(businessTypeList.get(i).getName());
                    }
                }
            }
        }
        businessTypeList.addAll(entity.data.result);
    }

    @Override
    public void getBusinessTypeListFailed(String errorMsg) {
        ToastUtils.show(context, "getBusinessTypeListFailed" + errorMsg);
    }

    @Override
    public void getIfUploadSuccess(IfUploadEntity entity) {
        if (null != entity) {
            if (!entity.getDealSuccessFlag()) {
                rlNeedLaboratory.setVisibility(View.GONE);
            } else {
                rlNeedLaboratory.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void getIfUploadFailed(String errorMsg) {
        rlNeedLaboratory.setVisibility(View.VISIBLE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectDataEvent(SelectDataEvent selectDataEvent) {  //部门选择 eventBus 回调
        if (selectDataEvent.getEntity() instanceof DepartmentEntity) {
            DepartmentEntity departmentEntity = (DepartmentEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctCheckDepartment.getTag() + "")) {
                    ctCheckDepartment.setValue(departmentEntity.getName());
                    //更换表头中请检部门的数据
                    ApplyDeptIdEntity entity = new ApplyDeptIdEntity();
                    entity.setId(departmentEntity.getId());
                    entity.setName(departmentEntity.getName());
                    mHeadEntity.setApplyDeptId(entity);
                }
            }
        } else if (selectDataEvent.getEntity() instanceof PsIdEntity) {  //采样点 eventBus 回调
            PsIdEntity psIdEntity = (PsIdEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctSamplingPoint.getTag() + "")) {
                    ctSamplingPoint.setValue(psIdEntity.getName());
                    //更换表头采样点数据源
                    mHeadEntity.setPsId(psIdEntity);
                }
            }
        }else if (selectDataEvent.getEntity() instanceof VendorIdEntity){ //供应商 eventBus 回调
            VendorIdEntity vendorIdEntity = (VendorIdEntity)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctSupplier.getTag() + "")){
                    ctSupplier.setValue(vendorIdEntity.getName());

                    //更换表头供应商数据
                    mHeadEntity.setVendorId(vendorIdEntity);
                }
            }

        }else if (selectDataEvent.getEntity() instanceof InspectionItemEvent){ // 检验项目 eventBus 回调
            InspectionItemEvent entity = (InspectionItemEvent) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(llInspectionItems.getTag() + "")){
                    String gsonString = entity.getList().toString();
                    adapter.getList().get(inspectionItemPosition).setInspStdVerCom(gsonString);
                }
            }

        }else if (selectDataEvent.getEntity() instanceof ContactEntity) {  //请检人 eventBus 回调
            ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctCheckPeople.getTag() + "")) {
                    ctCheckPeople.setValue(contactEntity.name);
                    //更换表头中的请检人的数据
                    ApplyStaffIdEntity entity = new ApplyStaffIdEntity();
                    entity.setId(contactEntity.getStaffId());
                    entity.setName(contactEntity.name);
                    mHeadEntity.setApplyStaffId(entity);
                }
            }
        } else if (selectDataEvent.getEntity() instanceof QualityStandardEvent) {  // 质量标准 eventBus 回调
            QualityStandardEvent qualityStandardEvent = (QualityStandardEvent) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(llStandardReference.getTag() + "")) {
                    List<QualityStandardReferenceEntity> list = qualityStandardEvent.getList();
                    for (int i = 0; i < list.size(); i++) {
                        InspectionDetailPtEntity entity = new InspectionDetailPtEntity();
                        StdVerIdEntity stdVerIdEntity = new StdVerIdEntity();
                        stdVerIdEntity.setBusiVersion(list.get(i).getBusiVersion());//版本号
                        stdVerIdEntity.setId(list.get(i).getId()); //质量标准外层ID
                        StdIdEntity stdIdEntity = new StdIdEntity();
                        stdIdEntity.setId(list.get(i).getStdId().getId());//质量标准ID
                        stdIdEntity.setStandard(StringUtil.isEmpty(list.get(i).getStdId().getStandard()) ? "" : list.get(i).getStdId().getStandard());//执行标准
                        stdIdEntity.setName(StringUtil.isEmpty(list.get(i).getStdId().getName()) ? "" : list.get(i).getStdId().getName());//质量标准
                        stdVerIdEntity.setStdId(stdIdEntity);
                        entity.setStdVerId(stdVerIdEntity);
                        //需要调用接口 获取选择过来的质量标准的请见方案
                        presenterRouter.create(com.supcon.mes.module_lims.model.api.AvailableStdIdApi.class).getDefaultInspProjByStdVerId(entity,entity.getStdVerId().getId()+"");

                    }
                }
            }

        } else if (selectDataEvent.getEntity() instanceof PleaseCheckSchemeEntity) {  //请检方案
            PleaseCheckSchemeEntity pleaseCheckSchemeEntity = (PleaseCheckSchemeEntity) selectDataEvent.getEntity();
            if (selectDataEvent.getSelectTag().equals("pleaseCheckScheme")) {
                BaseLongIdNameEntity entity = new BaseLongIdNameEntity();
                entity.setId(pleaseCheckSchemeEntity.getId());
                entity.setName(pleaseCheckSchemeEntity.getName());
                adapter.getList().get(myQualityStandardPosition).setInspectProjId(entity);
                adapter.notifyDataSetChanged();
                //直接更换质量标准中对应的数据源
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectMaterial(MaterialDateEvent event) {
        if (event.isRadio()) {
            if (null != event.getData()) {
                if (!StringUtil.isEmpty(event.getData().getCode()) && !StringUtil.isEmpty(event.getData().getName())) {
                    ctMateriel.setContent(event.getData().getName() + "(" + event.getData().getCode() + ")");
                } else {
                    if (StringUtil.isEmpty(event.getData().getName()) && StringUtil.isEmpty(event.getData().getCode())) {
                        ctMateriel.setContent("--");
                    } else {
                        if (!StringUtil.isEmpty(event.getData().getName())) {
                            ctMateriel.setContent(event.getData().getName());
                        } else if (!StringUtil.isEmpty(event.getData().getCode())) {
                            ctMateriel.setContent(event.getData().getCode());
                        }
                    }

                }
                if (null != event.getData().getMainUnit()) {
                    ctMaterielUnit.setContent(StringUtil.isEmpty(event.getData().getMainUnit().getName()) ? "--" : event.getData().getMainUnit().getName());
                } else {
                    ctMaterielUnit.setContent("--");
                }

                //变更表头物料信息的值
                mHeadEntity.setProdId(event.getData());
                if (event.getData().isEnableBatch()){
                    ceMaterielBatchNumber.setEditable(true);
                    ceMaterielBatchNumber.setNecessary(true);
                }else {
                    ceMaterielBatchNumber.setEditable(false);
                    ceMaterielBatchNumber.setNecessary(false);
                }
                presenterRouter.create(com.supcon.mes.module_lims.model.api.AvailableStdIdApi.class).getDefaultStandardById(mHeadEntity.getProdId().getId()+"");

            }
        }
    }

    String getView(){
        String view="";
        if (type==1){
            view="产品检验申请单";
        }else if (type==2){
            view="来料检验申请单";
        }else if (type==3){
            view="其他检验申请单";
        }
        return view;
    }

    private String getDg(){
        String dg = "";
        if (type == 1){
            dg = "dg1591080786501";
        }else if (type == 2){
            dg = "dg1587627206280";
        }else if (type == 3){
            dg = "dg1591595570526";
        }
        return dg;
    }

    private String getViewCode(){
        String viewCode = "";
        if (type == 1){
            if (isEdit){
                viewCode = "manuInspectEdit";
            }else {
                viewCode = "manuInspectView";
            }

        }else if (type == 2){
            if (isEdit){
                viewCode = "purchInspectEdit";
            }else {
                viewCode = "purchInspectView";
            }

        }else if (type == 3){
            if (isEdit){
                viewCode = "otherInspectEdit";
            }else {
                viewCode = "otherInspectView";
            }
        }

        return viewCode;
    }

    private boolean checkSubmit(){
        if (StringUtil.isEmpty(ctBusinessType.getContent().trim()) || ctBusinessType.getContent().equals("--")){
            setToast("业务类型不允许为空");
            return false;
        }

        if (StringUtil.isEmpty(ctCheckPeople.getContent().trim()) || ctCheckPeople.getContent().equals("--")){
            setToast("请检人不允许为空");
            return false;
        }

        if (StringUtil.isEmpty(ctCheckDepartment.getContent().trim()) || ctCheckDepartment.getContent().equals("--")){
            setToast("请检部门不允许为空");
            return false;
        }

        if (StringUtil.isEmpty(cdCheckTime.getContent().trim()) || cdCheckTime.getContent().equals("--")){
            setToast("请检时间不允许为空");
            return false;
        }
//        if (type == 2) {
//            if (StringUtil.isEmpty(ctSupplier.getContent().trim())) {
//                setToast("供应商不允许为空");
//                return false;
//            }
//        }

        if (StringUtil.isEmpty(ctMateriel.getContent().trim()) || ctMateriel.getContent().equals("--")){
            setToast("物料不允许为空");
            return false;
        }

        if (mHeadEntity.getProdId().isEnableBatch()){ //是否启用批次
            if (StringUtil.isEmpty(ceMaterielBatchNumber.getContent().trim()) || ceMaterielBatchNumber.getContent().equals("--")){
                setToast("批号不允许为空");
                return false;
            }
        }

        if (ptList.size() <= 0){
            setToast("质量标准不允许为空");
            return false;
        }else {
            for (int i = 0; i < ptList.size(); i++) {
                if (ptList.get(i).getInspStdVerCom() == null){
                    setToast("第"+(i+1)+"条质量标准中的检验项目不允许为空");
                    return false;
                }else {
                    List<StdVerComIdEntity> stdVerComIdList = GsonUtil.jsonToList(ptList.get(i).getInspStdVerCom(), StdVerComIdEntity.class);
                    if (stdVerComIdList.size() <= 0){
                        setToast("第"+(i+1)+"条质量标准中的检验项目不允许为空");
                        return false;
                    }
                }

            }
        }

        return true;
    }

    private void setToast(String content){
        ToastUtils.show(context,content);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getAvailableStdIdSuccess(AvailableStdEntity entity) {
        if (entity.getHasStdVer()){  // 表示有可用标准
            Bundle bundle = new Bundle();
            bundle.putBoolean("hasStdVer",entity.getHasStdVer());
            bundle.putString("id", mHeadEntity.getProdId().getId()+"");
            bundle.putSerializable("existItem", (Serializable) ptList);
            bundle.putString(Constant.IntentKey.SELECT_TAG, llStandardReference.getTag() + "");
            IntentRouter.go(context, Constant.AppCode.LIMS_QualityStdVerRef, bundle);
        }else {
            if (null != entity.getAsId()){ //表示没有可用标准 但是关联了样品模板  entity.getAsId()为样品模板的id
                Bundle bundle = new Bundle();
                bundle.putBoolean("hasStdVer",entity.getHasStdVer());
                bundle.putString("id", entity.getAsId()+"");
                bundle.putSerializable("existItem", (Serializable) ptList);
                bundle.putString(Constant.IntentKey.SELECT_TAG, llStandardReference.getTag() + "");
                IntentRouter.go(context, Constant.AppCode.LIMS_QualityStdVerRef, bundle);
            }else {
                ToastUtils.show(context,"物料未被样品模板关联，无法参照!");
            }
        }

    }

    @Override
    public void getAvailableStdIdFailed(String errorMsg) {
        ToastUtils.show(context,"获取可用标准失败");
    }

    @Override
    public void getDefaultStandardByIdSuccess(TemporaryQualityStandardEntity entity) {
        if (null != entity){
            myDefaultQualityStandardEntity = entity;
            if (null != entity.getStdVersion()){
                presenterRouter.create(com.supcon.mes.module_lims.model.api.AvailableStdIdApi.class).getDefaultItems(entity.getStdVersion().getId()+"");
            }else {
                ToastUtils.show(context,"当前物料暂无可用质量标准");
            }
            //获取到的默认质量标准对应的检验项目 需要回填
        }

    }

    @Override
    public void getDefaultStandardByIdFailed(String errorMsg) {
        ToastUtils.show(context,"无法获取默认质量标准");
    }

    @Override
    public void getDefaultItemsSuccess(StdVerComIdListEntity entity) {
        if (null != entity){
            InspectionDetailPtEntity ptEntity = new InspectionDetailPtEntity();
            StdVerIdEntity stdVerIdEntity = new StdVerIdEntity();
            stdVerIdEntity.setId(myDefaultQualityStandardEntity.getStdVersion() == null ? null : myDefaultQualityStandardEntity.getStdVersion().getId());
            stdVerIdEntity.setBusiVersion(myDefaultQualityStandardEntity.getStdVersion() == null ? "" : StringUtil.isEmpty(myDefaultQualityStandardEntity.getStdVersion().getBusiVersion()) ? "" : myDefaultQualityStandardEntity.getStdVersion().getBusiVersion());

            StdIdEntity stdIdEntity = new StdIdEntity();
            stdIdEntity.setId(myDefaultQualityStandardEntity.getQualityStd() == null ? null : myDefaultQualityStandardEntity.getQualityStd().getId());
            stdIdEntity.setName(myDefaultQualityStandardEntity.getQualityStd() == null ? "" : StringUtil.isEmpty(myDefaultQualityStandardEntity.getQualityStd().getName()) ? "" : myDefaultQualityStandardEntity.getQualityStd().getName());
            stdIdEntity.setStandard(myDefaultQualityStandardEntity.getQualityStd() == null ? "" : StringUtil.isEmpty(myDefaultQualityStandardEntity.getQualityStd().getStandard()) ? "" : myDefaultQualityStandardEntity.getQualityStd().getStandard());
            stdVerIdEntity.setStdId(stdIdEntity);

            BaseLongIdNameEntity baseLongIdNameEntity = new BaseLongIdNameEntity();
            baseLongIdNameEntity.setId(myDefaultQualityStandardEntity.getInspectProj() == null ? null : myDefaultQualityStandardEntity.getInspectProj().getId());
            baseLongIdNameEntity.setName(myDefaultQualityStandardEntity.getInspectProj() == null ? "" : StringUtil.isEmpty(myDefaultQualityStandardEntity.getInspectProj().getName()) ? "" : myDefaultQualityStandardEntity.getInspectProj().getName());

            ptEntity.setInspectProjId(baseLongIdNameEntity);//请检方案
            ptEntity.setStdVerId(stdVerIdEntity); // 质量标准
            ptEntity.setInspStdVerCom(new Gson().toJson(entity.data)); //对应的检验项目

            ptList.clear();
            ptList.add(ptEntity);
            adapter.setList(ptList);

        }
    }

    @Override
    public void getDefaultItemsFailed(String errorMsg) {
        ToastUtils.show(context,"无法获取默认质量标准对应的检验项目");
    }

    @Override
    public void getDefaultInspProjByStdVerIdSuccess(InspectionDetailPtEntity entity) {
        ptList.add(entity);
        adapter.setList(ptList);
    }

    @Override
    public void getDefaultInspProjByStdVerIdFailed(String errorMsg) {
        ToastUtils.show(context,"获取质量标准对应的检验方案失败");
    }

    @Override
    public void submitInspectApplicationSuccess(BAP5CommonEntity entity) {
        ((BaseActivity)context).onLoadSuccessAndExit("处理成功！", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                if (workFlowType == 1){
                    EventBus.getDefault().post(new RefreshEvent());
                    ((BaseActivity)context).back();
                }else {
                    customWorkFlowView.findViewById(R.id.commentInput).setVisibility(View.GONE);
                    if (null != mOnRequestHeadListener){
                        mOnRequestHeadListener.requestHeadClick();
                    }
                }

            }
        });
    }

    @Override
    public void submitInspectApplicationFailed(String errorMsg) {
        ((BaseActivity)context).onLoadFailed(errorMsg);
    }

    public void setRefreshHeadData(OnRequestHeadListener mOnRequestHeadListener){
        this.mOnRequestHeadListener = mOnRequestHeadListener;
    }


    public interface OnRequestPtListener {
        void requestPtClick(boolean isEdit);
    }

    public interface OnRequestHeadListener{
        void requestHeadClick();
    }
}
