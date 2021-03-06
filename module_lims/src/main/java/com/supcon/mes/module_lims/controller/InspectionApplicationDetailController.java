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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DataUtil;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.event.InspectionItemEvent;
import com.supcon.mes.module_lims.event.MaterialDateEvent;

import com.supcon.mes.module_lims.event.QualityStandardEvent;
import com.supcon.mes.module_lims.model.api.AddTestRequestAPI;
import com.supcon.mes.module_lims.model.api.AvailableStdIdAPI;
import com.supcon.mes.module_lims.model.api.BusiTypeByCodeAPI;
import com.supcon.mes.module_lims.model.api.InspectApplicationSubmitAPI;
import com.supcon.mes.module_lims.model.api.InspectionDetailReadyAPI;
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
import com.supcon.mes.module_lims.model.bean.ProdIdEntity;
import com.supcon.mes.module_lims.model.bean.PsIdEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;
import com.supcon.mes.module_lims.model.bean.StdIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdListEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;
import com.supcon.mes.module_lims.model.bean.TemporaryQualityStandardEntity;
import com.supcon.mes.module_lims.model.bean.VendorIdEntity;

import com.supcon.mes.module_lims.model.contract.AddTestRequestContract;
import com.supcon.mes.module_lims.model.contract.AvailableStdIdContract;
import com.supcon.mes.module_lims.model.contract.BusiTypeByCodeContract;
import com.supcon.mes.module_lims.model.contract.InspectApplicationSubmitContract;
import com.supcon.mes.module_lims.model.contract.InspectionDetailReadyContract;
import com.supcon.mes.module_lims.presenter.AddTestRequestPresenter;
import com.supcon.mes.module_lims.presenter.AvailableStdPresenter;
import com.supcon.mes.module_lims.presenter.BusiTypeByCodePresenter;
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
 * class name ??????????????????Controller
 */

@Presenter(value = {InspectionDetailReadyPresenter.class, AvailableStdPresenter.class,
        InspectApplicationSubmitPresenter.class, AddTestRequestPresenter.class, BusiTypeByCodePresenter.class})
public class InspectionApplicationDetailController extends BaseViewController implements InspectionDetailReadyContract.View, AvailableStdIdContract.View,
        InspectApplicationSubmitContract.View, AddTestRequestContract.View, BusiTypeByCodeContract.View {

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
    private OnRequestWorkFlowListener mOnRequestWorkFlowListener;

    private TemporaryQualityStandardEntity myDefaultQualityStandardEntity;
    private InspectionApplicationDetailHeaderEntity mHeadEntity;
    private QualityStandardAdapter adapter;

    private SinglePickController<String> mBusinessTypeController;
    private DatePickController datePickController;
    GetPowerCodeController powerCodeController;
    WorkFlowViewController workFlowViewController;

    private List<String> businessTypeStrList = new ArrayList<>(); // ??????????????????????????????????????????
    private List<BusiTypeIdEntity> businessTypeList = new ArrayList<>();// ????????????????????????????????????
    private List<InspectionDetailPtEntity> ptList = new ArrayList<>();
    private List<String> deletePtIds = new ArrayList<>();
    private List<String> myDeleteList = new ArrayList<>();

    private int myQualityStandardPosition;
    private boolean isEdit = false;
    private String from = "";
    private int inspectionItemPosition;
    private int type = -1;
    int workFlowType = -1;
    private Long mDeploymentId;
    private String activityName;
    private TableTypeIdEntity tableType;

    public InspectionApplicationDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        presenterRouter.create(InspectionDetailReadyAPI.class).getIfUpload();

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
                EventBus.getDefault().post(new RefreshEvent());
            }
        });

        //????????????????????????
        cdCheckTime.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    mHeadEntity.setApplyTime("");
                    return;
                }
                datePickController.listener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute, String second) {
                        String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        cdCheckTime.setContent(dateStr);
                        //?????????????????????????????????
                        mHeadEntity.setApplyTime(DateUtil.dateFormat(dateStr,"yyyy-MM-dd HH:mm:ss")+"");
                    }
                }).show(new Date().getTime());
            }
        });

        //????????????????????????
        ctBusinessType.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    BusiTypeIdEntity entity = new BusiTypeIdEntity();
                    entity.setId(null);
                    entity.setName("");
                    mHeadEntity.setBusiTypeId(entity);
                    return;
                }
                showSpinnerSelector(ctBusinessType, businessTypeStrList);
            }
        });

        //?????????????????????
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
                    ApplyStaffIdEntity entity = new ApplyStaffIdEntity();
                    entity.setId(null);
                    entity.setName("");
                    mHeadEntity.setApplyStaffId(entity);
                }
            }
        });

        //????????????????????????
        ctCheckDepartment.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    ApplyDeptIdEntity entity = new ApplyDeptIdEntity();
                    entity.setId(null);
                    entity.setName("");
                    mHeadEntity.setApplyDeptId(entity);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG, ctCheckDepartment.getTag() + "");
                IntentRouter.go(context, Constant.Router.DEPART_SELECT, bundle);
            }
        });

        //??????????????????
        RxTextView.textChanges(ceMaterielBatchNumber.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        //???????????????????????????
                        mHeadEntity.setBatchCode(charSequence.toString());
                    }
                });

        //??????????????????
        ceMaterielNum.editText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        RxTextView.textChanges(ceMaterielNum.editText())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence s) throws Exception {
                        //?????????.???????????????2???????????????
                        if (s.toString().contains(".")) {
                            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                s = s.toString().subSequence(0,
                                        s.toString().indexOf(".") + 3);
                                ceMaterielNum.setContent(s.toString());
                                ceMaterielNum.editText().setSelection(s.length()); //??????????????????
                            }
                        }
                        //??????"."???????????????,????????????????????????0
                        if (s.toString().trim().substring(0).equals(".")) {
                            s = "0" + s;
                            ceMaterielNum.setContent(s.toString());
                            ceMaterielNum.editText().setSelection(2);
                        }

                        //?????????????????????0,????????????????????????".",?????????????????????
                        if (s.toString().startsWith("0")
                                && s.toString().trim().length() > 1) {
                            if (!s.toString().substring(1, 2).equals(".")) {
                                ceMaterielNum.editText().setText(s.subSequence(0, 1));
                                ceMaterielNum.editText().setSelection(1);
                                return;
                            }
                        }

                        if (s.length() > 0){
                            //??????????????????????????????
                            if (!s.toString().equals("--")){
                                mHeadEntity.setQuantity(new BigDecimal(s.toString()).setScale(2,BigDecimal.ROUND_DOWN));
                            }

                        }else {
                            //mHeadEntity.setQuantity(new BigDecimal("0.00").setScale(2,BigDecimal.ROUND_DOWN));
                            mHeadEntity.setQuantity(null);
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

        //?????????????????????
        ctSamplingPoint.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    PsIdEntity entity = new PsIdEntity();
                    entity.setId(null);
                    entity.setName("");
                    mHeadEntity.setPsId(entity);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG, ctSamplingPoint.getTag() + "");
                IntentRouter.go(context, Constant.AppCode.LIMS_PickSiteRefPart, bundle);
            }
        });

        //?????????????????????
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

        //??????????????????????????????????????????
        ctMateriel.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action == -1){
                    ProdIdEntity entity = new ProdIdEntity();
                    mHeadEntity.setProdId(entity);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("radio", true);
                IntentRouter.go(context, Constant.AppCode.LIMS_MaterialRef, bundle);
            }
        });

        //???????????????????????????
        RxTextView.textChanges(ctMateriel.contentView())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {  //??????????????????????????????????????????????????????????????????????????????????????????  ???????????????????????????
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


        // ????????????????????? ??????
        RxView.clicks(rlNeedLaboratory)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //???????????????????????????????????????
                        mHeadEntity.setNeedLab(!mHeadEntity.getNeedLab());
                        setNeedLaboratory();
                    }
                });

        //????????????????????????
        RxView.clicks(llStandardReference)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (null == mHeadEntity.getProdId() || null == mHeadEntity.getProdId().getId()){
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_please_select_material));
                            return;
                        }
                        //?????????????????????
                        presenterRouter.create(AvailableStdIdAPI.class).getAvailableStdId(mHeadEntity.getProdId().getId() + "");
                    }
                });

        //?????????????????????????????? ??????????????????????????????
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0) { //????????????
                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (i != position){
                            adapter.getList().get(i).setSelect(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (action == 1) {//?????????????????????????????????
                    myQualityStandardPosition = position;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", adapter.getList().get(position).getStdVerId().getId() + "");
                    bundle.putString(Constant.IntentKey.SELECT_TAG, "pleaseCheckScheme");
                    IntentRouter.go(context, Constant.AppCode.LIMS_InspectProjRef, bundle);
                } else if (action == 2) {
                    //?????????????????????  ?????????????????????id
                    //adapter.getList().get(position).getId() != null  ????????????????????????????????????????????????????????????  ?????????id???
                    if (adapter.getList().get(position).getId() != null){
                        deletePtIds.add(adapter.getList().get(position).getId()+"");
                    }

                    adapter.remove(position);
                    adapter.notifyDataSetChanged();

                }
            }
        });

        //????????????????????????
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
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_please_select_quality_standard));
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
                        if (getViewCode().contains("Edit")){ //????????????
                            if (checkSubmit()){
                                doSubmit(workFlowVar);
                            }
                        }else {
                            doSubmit(workFlowVar);
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

    public void startWorkFlow(Long id,String menuName){
        ctMateriel.setEditable(true);
        ceMaterielBatchNumber.setEditable(false);
        mDeploymentId = id;
        activityName = menuName;
        mHeadEntity = new InspectionApplicationDetailHeaderEntity();
        presenterRouter.create(BusiTypeByCodeAPI.class).getBusiTypeByCode(type == 1 ? "product" : type == 2 ? "material" : type == 3 ? "other" : ""  );

        //????????????????????????
        ApplyStaffIdEntity staffIdEntity = new ApplyStaffIdEntity();
        staffIdEntity.setName(SupPlantApplication.getAccountInfo().staffName);
        staffIdEntity.setId(SupPlantApplication.getAccountInfo().staffId);
        mHeadEntity.setApplyStaffId(staffIdEntity);
        ctCheckPeople.setContent(mHeadEntity.getApplyStaffId().getName());

        //????????????????????????
        ApplyDeptIdEntity deptIdEntity = new ApplyDeptIdEntity();
        deptIdEntity.setName(SupPlantApplication.getAccountInfo().departmentName);
        deptIdEntity.setId(SupPlantApplication.getAccountInfo().departmentId);
        mHeadEntity.setApplyDeptId(deptIdEntity);
        ctCheckDepartment.setContent(mHeadEntity.getApplyDeptId().getName());

        //????????????????????????
        long time = System.currentTimeMillis();
        mHeadEntity.setApplyTime(time+"");
        cdCheckTime.setContent(DateUtil.dateFormat(time,"yyyy-MM-dd HH:mm:ss"));

        //???????????????????????????
        ivNeedLaboratory.setBackgroundResource(R.drawable.ic_check_yes);
        mHeadEntity.setNeedLab(true);

        //?????????????????????????????????
        presenterRouter.create(InspectionDetailReadyAPI.class).getBusinessTypeList(type);
        powerCodeController.initPowerCode(type == 3 ? "start_t1vhtik" : type == 2 ? "start_7r8amon" : type == 1 ? "start_ju6mjql" : "");
        workFlowViewController.initStartWorkFlowView(customWorkFlowView,id);
        customWorkFlowView.setVisibility(View.VISIBLE);

        //???????????????????????????????????????
        recyclerView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
    }

    //???????????????
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
        //?????????????????????????????????
        presenterRouter.create(InspectionDetailReadyAPI.class).getBusinessTypeList(type);
        if (null != entity) {
            mHeadEntity = entity;
            if (null != entity.getPending() && null != entity.getPending().openUrl) {
                if (entity.getPending().openUrl.contains(LimsConstant.WorkType.PRODUCT_INSPECT_EDIT)) {
                    setCanEdit();
                } else if (entity.getPending().openUrl.contains(LimsConstant.WorkType.PRODUCT_INSPECT_VIEW)) { //??????????????????
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
        if (StringUtil.isEmpty(ctMateriel.getContent()) || ctMateriel.getContent().equals("--")){
            ptList.clear();
            adapter.setList(ptList);
            return;
        }
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
        llStandardReference.setVisibility(View.GONE);

        cdCheckTime.setNecessary(false);
        ctCheckPeople.setNecessary(false);
        ctCheckDepartment.setNecessary(false);
        ctBusinessType.setNecessary(false);
        ctMateriel.setNecessary(false);
        ceMaterielBatchNumber.setNecessary(false);
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
        llStandardReference.setVisibility(View.VISIBLE);

        cdCheckTime.setNecessary(true);
        ctCheckPeople.setNecessary(true);
        ctCheckDepartment.setNecessary(true);
        ctBusinessType.setNecessary(true);
        ctMateriel.setNecessary(true);
        ceMaterielBatchNumber.setNecessary(true);
    }

    private void setMakingRules(InspectionApplicationDetailHeaderEntity entity) {
        if (null != entity) {

            //????????????
            cdCheckTime.setContent(StringUtil.isEmpty(entity.getApplyTime()) || entity.getApplyTime().equals("--")  ? "--" : DateUtil.dateFormat(Long.valueOf(entity.getApplyTime()), "yyyy-MM-dd HH:mm:ss")+"");
            //?????????
            if (null != entity.getApplyStaffId()) {
                ctCheckPeople.setContent(StringUtil.isEmpty(entity.getApplyStaffId().getName()) ? "--" : entity.getApplyStaffId().getName());
            } else {
                ctCheckPeople.setContent("--");
            }
            //????????????
            if (null != entity.getApplyDeptId()) {
                ctCheckDepartment.setContent(StringUtil.isEmpty(entity.getApplyDeptId().getName()) ? "--" : entity.getApplyDeptId().getName());
            } else {
                ctCheckDepartment.setContent("--");
            }
            //?????????
            if (null != entity.getPsId()) {
                ctSamplingPoint.setContent(StringUtil.isEmpty(entity.getPsId().getName()) ? "--" : entity.getPsId().getName());
            } else {
                ctSamplingPoint.setContent("--");
            }
            //????????????
            if (null != entity.getBusiTypeId()) {
                ctBusinessType.setContent(StringUtil.isEmpty(entity.getBusiTypeId().getName()) ? "--" : entity.getBusiTypeId().getName());
            } else {
                ctBusinessType.setContent("--");
            }
            //???????????? ?????? ????????????
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

            //??????
            ceMaterielNum.setContent(null == entity.getQuantity() ? "--" : entity.getQuantity().setScale(2, BigDecimal.ROUND_DOWN) + "");
            //??????
            ceMaterielBatchNumber.setContent(StringUtil.isEmpty(entity.getBatchCode()) ? "--" : entity.getBatchCode());
            //?????????
            if (null != entity.getVendorId()){
                ctSupplier.setContent(StringUtil.isEmpty(entity.getVendorId().getName()) ? "--" : entity.getVendorId().getName());
            }else {
                ctSupplier.setContent("--");
            }

            if (null != entity.getProdId() && isEdit){
                if (entity.getProdId().isEnableBatch()){
                    ceMaterielBatchNumber.setEditable(true);
                    ceMaterielBatchNumber.setNecessary(true);
                }else {
                    ceMaterielBatchNumber.setEditable(false);
                    ceMaterielBatchNumber.setNecessary(false);
                }
            }

            if (null != entity.getSourceType() || null != entity.getSourceId()) { //????????????????????????????????????????????????????????????????????????
                ctMateriel.setEditable(false);
                ctMateriel.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                ceMaterielBatchNumber.setEditable(false);
            }

            //?????????????????????
            setNeedLaboratory();

        }
    }

    private void setNeedLaboratory() {
        if (null != mHeadEntity.getNeedLab()){
            if (mHeadEntity.getNeedLab()) {
                ivNeedLaboratory.setBackgroundResource(R.drawable.ic_check_yes);
            } else {
                ivNeedLaboratory.setBackgroundResource(R.drawable.ic_check_no);
            }
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
     * ??????????????????????????????
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

    //?????????--??????
    private void doSave(WorkFlowVar workFlowVar){
        String view=getView();
        ((BaseActivity)context).onLoading(view+context.getResources().getString(R.string.lims_saving));
        InspectApplicationSubmitEntity entity = new InspectApplicationSubmitEntity();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment", !TextUtils.isEmpty(workFlowVar.comment) ? workFlowVar.comment : "");
        entity.workFlowVar = jsonObject;

        entity.operateType = Constant.Transition.SAVE;

        generateSaveOrSubmit(entity);
    }

    private void generateSaveOrSubmit(InspectApplicationSubmitEntity entity) {
        if (from.equals("add")){
            entity.deploymentId = mDeploymentId+"";
            entity.activityName = activityName;
            entity.taskDescription = context.getResources().getString(R.string.lims_edit);
            mHeadEntity.setTableTypeId(tableType);
            entity.inspect = mHeadEntity;
        }else {
            entity.deploymentId = mHeadEntity.getPending().deploymentId+"";
            entity.taskDescription = mHeadEntity.getPending().taskDescription;
            entity.activityName = mHeadEntity.getPending().activityName;
            entity.pendingId = mHeadEntity.getPending().id.toString();
            entity.inspect = mHeadEntity;
        }

        JsonObject dgJson = new JsonObject();
        dgJson.addProperty(getDg(),GsonUtil.gsonString(ptList));
        entity.dgList = dgJson;

        myDeleteList.clear();
        for (int i = 0; i < deletePtIds.size(); i++) {
            if (!deletePtIds.get(i).equals("null") ){
                myDeleteList.add(deletePtIds.get(i));
            }
        }
        //?????????????????????ids
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < myDeleteList.size(); i++) {
            if (i < myDeleteList.size() - 1) {
                sb.append(myDeleteList.get(i)).append(",");
            } else if (i == myDeleteList.size() - 1) {
                sb.append(myDeleteList.get(i));
            }
        }
        entity.dgDeletedIds.addProperty(getDg(),sb.length() > 0 ? sb.toString() : null);

        String viewCode=getViewCode();
        entity.viewCode = "QCS_5.0.0.0_inspect_"+viewCode;
        String path = viewCode;
        String _pc_ = powerCodeController.getPowerCodeResult();
        if (from.equals("add")){
            Map<String, Object> params = new HashMap<>();
            if (mHeadEntity.getId()!= null) {
                params.put("id", mHeadEntity.getId());
            }
            params.put("__pc__", "");
            presenterRouter.create(AddTestRequestAPI.class).addTestRequestSave(path,params,entity);
        }else {
            Map<String, Object> params = new HashMap<>();
            if (mHeadEntity.getId()!= null) {
                params.put("id", mHeadEntity.getId());
            }
            if (TextUtils.isEmpty(_pc_)){
                _pc_="111";
            }
            params.put("__pc__", "");
            presenterRouter.create(InspectApplicationSubmitAPI.class).submitInspectApplication(path, params, entity);
        }

    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        InspectApplicationSubmitEntity entity = new InspectApplicationSubmitEntity();
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
    public void selectDataEvent(SelectDataEvent selectDataEvent) {  //???????????? eventBus ??????
        if (selectDataEvent.getEntity() instanceof DepartmentEntity) {
            DepartmentEntity departmentEntity = (DepartmentEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctCheckDepartment.getTag() + "")) {
                    ctCheckDepartment.setValue(departmentEntity.getName());
                    //????????????????????????????????????
                    ApplyDeptIdEntity entity = new ApplyDeptIdEntity();
                    entity.setId(departmentEntity.getId());
                    entity.setName(departmentEntity.getName());
                    mHeadEntity.setApplyDeptId(entity);
                }
            }
        } else if (selectDataEvent.getEntity() instanceof PsIdEntity) {  //????????? eventBus ??????
            PsIdEntity psIdEntity = (PsIdEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctSamplingPoint.getTag() + "")) {
                    ctSamplingPoint.setValue(psIdEntity.getName());
                    //??????????????????????????????
                    mHeadEntity.setPsId(psIdEntity);
                }
            }
        }else if (selectDataEvent.getEntity() instanceof VendorIdEntity){ //????????? eventBus ??????
            VendorIdEntity vendorIdEntity = (VendorIdEntity)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctSupplier.getTag() + "")){
                    ctSupplier.setValue(vendorIdEntity.getName());

                    //???????????????????????????
                    mHeadEntity.setVendorId(vendorIdEntity);
                }
            }

        }else if (selectDataEvent.getEntity() instanceof InspectionItemEvent){ // ???????????? eventBus ??????
            InspectionItemEvent entity = (InspectionItemEvent) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(llInspectionItems.getTag() + "")){
                    String gsonString = entity.getList().toString();
                    adapter.getList().get(inspectionItemPosition).setInspStdVerCom(gsonString);
                }
            }

        }else if (selectDataEvent.getEntity() instanceof ContactEntity) {  //????????? eventBus ??????
            ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctCheckPeople.getTag() + "")) {
                    ctCheckPeople.setValue(contactEntity.name);
                    //????????????????????????????????????
                    ApplyStaffIdEntity entity = new ApplyStaffIdEntity();
                    entity.setId(contactEntity.getStaffId());
                    entity.setName(contactEntity.name);
                    mHeadEntity.setApplyStaffId(entity);
                }
            }
        } else if (selectDataEvent.getEntity() instanceof QualityStandardEvent) {  // ???????????? eventBus ??????
            QualityStandardEvent qualityStandardEvent = (QualityStandardEvent) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(llStandardReference.getTag() + "")) {
                    List<QualityStandardReferenceEntity> list = qualityStandardEvent.getList();
                    for (int i = 0; i < list.size(); i++) {
                        InspectionDetailPtEntity entity = new InspectionDetailPtEntity();
                        StdVerIdEntity stdVerIdEntity = new StdVerIdEntity();
                        stdVerIdEntity.setBusiVersion(list.get(i).getBusiVersion());//?????????
                        stdVerIdEntity.setId(list.get(i).getId()); //??????????????????ID
                        StdIdEntity stdIdEntity = new StdIdEntity();
                        stdIdEntity.setId(list.get(i).getStdId().getId());//????????????ID
                        stdIdEntity.setStandard(StringUtil.isEmpty(list.get(i).getStdId().getStandard()) ? "" : list.get(i).getStdId().getStandard());//????????????
                        stdIdEntity.setName(StringUtil.isEmpty(list.get(i).getStdId().getName()) ? "" : list.get(i).getStdId().getName());//????????????
                        stdVerIdEntity.setStdId(stdIdEntity);
                        entity.setStdVerId(stdVerIdEntity);
                        //?????????????????? ????????????????????????????????????????????????
                        presenterRouter.create(AvailableStdIdAPI.class).getDefaultInspProjByStdVerId(entity,entity.getStdVerId().getId()+"");

                    }
                }
            }

        } else if (selectDataEvent.getEntity() instanceof PleaseCheckSchemeEntity) {  //????????????
            PleaseCheckSchemeEntity pleaseCheckSchemeEntity = (PleaseCheckSchemeEntity) selectDataEvent.getEntity();
            if (selectDataEvent.getSelectTag().equals("pleaseCheckScheme")) {
                BaseLongIdNameEntity entity = new BaseLongIdNameEntity();
                entity.setId(pleaseCheckSchemeEntity.getId());
                entity.setName(pleaseCheckSchemeEntity.getName());
                adapter.getList().get(myQualityStandardPosition).setInspectProjId(entity);
                adapter.notifyDataSetChanged();
                //?????????????????????????????????????????????
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

                //??????????????????
                ceMaterielBatchNumber.setContent("--");

                //??????????????????????????????
                mHeadEntity.setProdId(event.getData());
                if (event.getData().isEnableBatch()){
                    ceMaterielBatchNumber.setEditable(true);
                    ceMaterielBatchNumber.setNecessary(true);
                }else {
                    ceMaterielBatchNumber.setEditable(false);
                    ceMaterielBatchNumber.setNecessary(false);
                }
                presenterRouter.create(AvailableStdIdAPI.class).getDefaultStandardById(mHeadEntity.getProdId().getId()+"");

            }
        }
    }

    String getView(){
        String view="";
        if (type==1){
            view=context.getResources().getString(R.string.lims_product_test_request_bill);
        }else if (type==2){
            view=context.getResources().getString(R.string.lims_incoming_test_request_bill);
        }else if (type==3){
            view=context.getResources().getString(R.string.lims_other_test_request_bill);
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
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_12));
            return false;
        }

        if (StringUtil.isEmpty(ctCheckPeople.getContent().trim()) || ctCheckPeople.getContent().equals("--")){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_11));
            return false;
        }

        if (StringUtil.isEmpty(ctCheckDepartment.getContent().trim()) || ctCheckDepartment.getContent().equals("--")){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_10));
            return false;
        }

        if (StringUtil.isEmpty(cdCheckTime.getContent().trim()) || cdCheckTime.getContent().equals("--")){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_9));
            return false;
        }
//        if (type == 2) {
//            if (StringUtil.isEmpty(ctSupplier.getContent().trim())) {
//                setToast("????????????????????????");
//                return false;
//            }
//        }

        if (StringUtil.isEmpty(ctMateriel.getContent().trim()) || ctMateriel.getContent().equals("--")){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_8));
            return false;
        }

        if (mHeadEntity.getProdId().isEnableBatch()){ //??????????????????
            if (StringUtil.isEmpty(ceMaterielBatchNumber.getContent().trim()) || ceMaterielBatchNumber.getContent().equals("--")){
                setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_7));
                return false;
            }
        }

        if (ptList.size() <= 0){
            setToast(context.getResources().getString(R.string.lims_inspection_application_detail_tips_6));
            return false;
        }else {
            for (int i = 0; i < ptList.size(); i++) {
                if (ptList.get(i).getInspStdVerCom() == null){
                    String string = context.getResources().getString(R.string.lims_inspection_application_detail_tips_13);
                    String format = String.format(string, (i + 1)+"");
                    setToast(format);
                    return false;
                }else {
                    List<StdVerComIdEntity> stdVerComIdList = GsonUtil.jsonToList(ptList.get(i).getInspStdVerCom(), StdVerComIdEntity.class);
                    if (stdVerComIdList.size() <= 0){
                        String string = context.getResources().getString(R.string.lims_inspection_application_detail_tips_13);
                        String format = String.format(string, (i + 1)+"");
                        setToast(format);
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
        if (entity.getHasStdVer()){  // ?????????????????????
            Bundle bundle = new Bundle();
            bundle.putBoolean("hasStdVer",entity.getHasStdVer());
            bundle.putString("id", mHeadEntity.getProdId().getId()+"");
            bundle.putSerializable("existItem", (Serializable) ptList);
            bundle.putString(Constant.IntentKey.SELECT_TAG, llStandardReference.getTag() + "");
            IntentRouter.go(context, Constant.AppCode.LIMS_QualityStdVerRef, bundle);
        }else {
            if (null != entity.getAsId()){ //???????????????????????? ???????????????????????????  entity.getAsId()??????????????????id
                Bundle bundle = new Bundle();
                bundle.putBoolean("hasStdVer",entity.getHasStdVer());
                bundle.putString("id", entity.getAsId()+"");
                bundle.putSerializable("existItem", (Serializable) ptList);
                bundle.putString(Constant.IntentKey.SELECT_TAG, llStandardReference.getTag() + "");
                IntentRouter.go(context, Constant.AppCode.LIMS_QualityStdVerRef, bundle);
            }else {
                ToastUtils.show(context,context.getResources().getString(R.string.lims_inspection_application_detail_tips_5));
            }
        }

    }

    @Override
    public void getAvailableStdIdFailed(String errorMsg) {
        ToastUtils.show(context,context.getResources().getString(R.string.lims_inspection_application_detail_tips_4));
    }

    @Override
    public void getDefaultStandardByIdSuccess(TemporaryQualityStandardEntity entity) {
        if (null != entity){
            myDefaultQualityStandardEntity = entity;
            if (null != entity.getStdVersion()){
                presenterRouter.create(AvailableStdIdAPI.class).getDefaultItems(entity.getStdVersion().getId()+"");
            }else {
                //?????????????????? ???????????????????????????????????????????????????
                for (int i = 0; i < adapter.getList().size(); i++) {
                    deletePtIds.add(adapter.getList().get(i).getId()+"");
                }
                ptList.clear();
                adapter.setList(ptList);
            }
        }

    }

    @Override
    public void getDefaultStandardByIdFailed(String errorMsg) {
        ToastUtils.show(context,context.getResources().getString(R.string.lims_inspection_application_detail_tips_3));
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

            ptEntity.setInspectProjId(baseLongIdNameEntity);//????????????
            ptEntity.setStdVerId(stdVerIdEntity); // ????????????
            ptEntity.setInspStdVerCom(new Gson().toJson(entity.data)); //?????????????????????

            //?????????????????? ???????????????????????????????????????????????????
            for (int i = 0; i < adapter.getList().size(); i++) {
                deletePtIds.add(adapter.getList().get(i).getId()+"");
            }

            ptList.clear();
            ptList.add(ptEntity);
            adapter.setList(ptList);

        }
    }

    public void setIsFrom(String from){
        this.from = from;
        isEdit = from.equals("add");
    }

    public void setType(int type){
        this.type = type;
    }

    public void setTableType(TableTypeIdEntity tableType){
        this.tableType = tableType;
    }

    @Override
    public void getDefaultItemsFailed(String errorMsg) {
        ToastUtils.show(context,context.getResources().getString(R.string.lims_inspection_application_detail_tips_2));
    }

    @Override
    public void getDefaultInspProjByStdVerIdSuccess(InspectionDetailPtEntity entity) {

        ptList.add(entity);
        adapter.setList(ptList);
    }

    @Override
    public void getDefaultInspProjByStdVerIdFailed(String errorMsg) {
        ToastUtils.show(context,context.getResources().getString(R.string.lims_inspection_application_detail_tips_1));
    }

    @Override
    public void submitInspectApplicationSuccess(BAP5CommonEntity entity) {
        ((BaseActivity)context).onLoadSuccessAndExit(context.getResources().getString(R.string.lims_deal), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                deletePtIds.clear();
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

    public void setRequestWorkFlowListener(OnRequestWorkFlowListener mOnRequestWorkFlowListener){
        this.mOnRequestWorkFlowListener = mOnRequestWorkFlowListener;
    }

    @Override
    public void addTestRequestSaveSuccess(PendingEntity entity) {
        if (!TextUtils.isEmpty(from))
            from="pengding";
        ((BaseActivity)context).onLoadSuccessAndExit("????????????", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                deletePtIds.clear();
                if (workFlowType == 1){
                    EventBus.getDefault().post(new RefreshEvent());
                    ((BaseActivity)context).back();
                }else {
                    mOnRequestWorkFlowListener.requestWorkFlow(entity.id+"",entity.pendingId+"");
                }

            }
        });
    }

    @Override
    public void addTestRequestSaveFailed(String errorMsg) {
        ((BaseActivity)context).onLoadFailed(errorMsg);
    }

    @Override
    public void getBusiTypeByCodeSuccess(BusiTypeIdEntity entity) {
        //????????????????????????
        mHeadEntity.setBusiTypeId(entity);
        if (null != mHeadEntity.getBusiTypeId()) {
            ctBusinessType.setContent(StringUtil.isEmpty(mHeadEntity.getBusiTypeId().getName()) ? "--" : mHeadEntity.getBusiTypeId().getName());
        } else {
            ctBusinessType.setContent("--");
        }
    }

    @Override
    public void getBusiTypeByCodeFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }


    public interface OnRequestPtListener {
        void requestPtClick(boolean isEdit);
    }

    public interface OnRequestHeadListener{
        void requestHeadClick();
    }

    public interface OnRequestWorkFlowListener{
        void requestWorkFlow(String id, String pendingId);
    }
}
