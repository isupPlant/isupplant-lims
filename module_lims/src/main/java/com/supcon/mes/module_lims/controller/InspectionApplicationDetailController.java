package com.supcon.mes.module_lims.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.event.InspectionItemEvent;
import com.supcon.mes.module_lims.event.MaterialDateEvent;

import com.supcon.mes.module_lims.event.QualityStandardEvent;
import com.supcon.mes.module_lims.model.bean.AvailableStdEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtEntity;
import com.supcon.mes.module_lims.model.bean.PleaseCheckSchemeEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;
import com.supcon.mes.module_lims.model.bean.SamplingPointEntity;
import com.supcon.mes.module_lims.model.bean.StdIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdListEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;
import com.supcon.mes.module_lims.model.bean.SupplierReferenceEntity;
import com.supcon.mes.module_lims.model.bean.TemporaryQualityStandardEntity;
import com.supcon.mes.module_lims.model.contract.AvailableStdIdApi;
import com.supcon.mes.module_lims.model.contract.InspectionDetailReadyApi;
import com.supcon.mes.module_lims.presenter.AvailableStdPresenter;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import utilcode.util.StringUtils;


/**
 * author huodongsheng
 * on 2020/7/13
 * class name 检验申请详情Controller
 */

@Presenter(value = {InspectionDetailReadyPresenter.class, AvailableStdPresenter.class})
public class InspectionApplicationDetailController extends BaseViewController implements InspectionDetailReadyApi.View, AvailableStdIdApi.View {

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

    private TemporaryQualityStandardEntity myDefaultQualityStandardEntity;
    private InspectionApplicationDetailHeaderEntity mHeadEntity;
    private QualityStandardAdapter adapter;

    private SinglePickController<String> mBusinessTypeController;
    private DatePickController datePickController;

    private List<String> businessTypeStrList = new ArrayList<>(); // 用于存放展示时业务类型的集合
    private List<BusinessTypeEntity> businessTypeList = new ArrayList<>();// 存放提交时业务类型的集合
    private List<InspectionDetailPtEntity> ptList = new ArrayList<>();

    private int myQualityStandardPosition;
    private boolean isEdit = false;
    private int inspectionItemPosition;

    public InspectionApplicationDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionDetailReadyApi.class).getIfUpload();
        //获取业务类型参照的数据
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionDetailReadyApi.class).getBusinessTypeList();
    }

    @Override
    public void initView() {
        super.initView();
        searchTitle.showScan(false);
        searchTitle.getSearchBtn().setVisibility(View.GONE);

        mBusinessTypeController = new SinglePickController<>((Activity) context);
        mBusinessTypeController.textSize(18);

        datePickController = new DatePickController((Activity) context);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);

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
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG,ctSupplier.getTag() + "");
                IntentRouter.go(context, Constant.AppCode.LIMS_CmcPartRef, bundle);
            }
        });

        //（点击物料）物料信息参照监听
        ctMateriel.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("radio", true);
                IntentRouter.go(context, Constant.AppCode.LIMS_MaterialRef, bundle);
            }
        });

        //物料为空时，计量单位和批号均为不可编辑状态，并且清空原有的值
        RxTextView.textChanges(ctMateriel.contentView())
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if (TextUtils.isEmpty(charSequence.toString().trim())) {
                            ctMaterielUnit.setContent("");
                            ceMaterielBatchNumber.setContent("");
                            ceMaterielBatchNumber.setEditable(false);
                        } else {
                            if (isEdit){
                                ceMaterielBatchNumber.setEditable(true);
                            }else {
                                ceMaterielBatchNumber.setEditable(false);
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
                            bundle.putBoolean("isEdit",isEdit);
                            IntentRouter.go(context,Constant.AppCode.LIMS_InspectComDataByStdVerId,bundle);
                        }else {
                            ToastUtils.show(context,"请先选择一项质量标准");
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

    public void setHeardData(InspectionApplicationDetailHeaderEntity entity, OnRequestPtListener mOnRequestPtListener) {
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
        llStandardReference.setVisibility(View.GONE);
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
        llStandardReference.setVisibility(View.VISIBLE);
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
                }
            }
        } else if (selectDataEvent.getEntity() instanceof SamplingPointEntity) {  //采样点 eventBus 回调
            SamplingPointEntity samplingPointEntity = (SamplingPointEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctSamplingPoint.getTag() + "")) {
                    ctSamplingPoint.setValue(samplingPointEntity.getName());
                }
            }
        }else if (selectDataEvent.getEntity() instanceof SupplierReferenceEntity){ //供应商 eventBus 回调
            SupplierReferenceEntity supplierReferenceEntity = (SupplierReferenceEntity)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctSupplier.getTag() + "")){
                    ctSupplier.setValue(supplierReferenceEntity.getName());
                }
            }

        }else if (selectDataEvent.getEntity() instanceof InspectionItemEvent){ // 检验项目 eventBus 回调
            InspectionItemEvent entity = (InspectionItemEvent) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(llInspectionItems.getTag() + "")){
                    String gsonString = GsonUtil.gsonString(entity.getList());
                    adapter.getList().get(inspectionItemPosition).setInspStdVerCom(gsonString);
                }
            }

        }else if (selectDataEvent.getEntity() instanceof ContactEntity) {  //请检人 eventBus 回调
            ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())) {
                if (selectDataEvent.getSelectTag().equals(ctCheckPeople.getTag() + "")) {
                    ctCheckPeople.setValue(contactEntity.name);
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
                        StdIdEntity stdIdEntity = new StdIdEntity();
                        stdIdEntity.setId(list.get(i).getStdId().getId());//质量标准ID
                        stdIdEntity.setStandard(StringUtil.isEmpty(list.get(i).getStdId().getStandard()) ? "" : list.get(i).getStdId().getStandard());//执行标准
                        stdIdEntity.setName(StringUtil.isEmpty(list.get(i).getStdId().getName()) ? "" : list.get(i).getStdId().getName());//质量标准
                        stdVerIdEntity.setStdId(stdIdEntity);
                        stdVerIdEntity.setId(list.get(i).getId()); //质量标准外层ID
                        entity.setStdVerId(stdVerIdEntity);
                        ptList.add(entity);
                    }
                    adapter.setList(ptList);
                }
            }

        } else if (selectDataEvent.getEntity() instanceof PleaseCheckSchemeEntity) {
            PleaseCheckSchemeEntity pleaseCheckSchemeEntity = (PleaseCheckSchemeEntity) selectDataEvent.getEntity();
            if (selectDataEvent.getSelectTag().equals("pleaseCheckScheme")) {
                BaseLongIdNameEntity entity = new BaseLongIdNameEntity();
                entity.setId(pleaseCheckSchemeEntity.getId());
                entity.setName(pleaseCheckSchemeEntity.getName());
                adapter.getList().get(myQualityStandardPosition).setInspectProjId(entity);
                adapter.notifyDataSetChanged();
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
                mHeadEntity.getProdId().setId(event.getData().getId());
                mHeadEntity.getProdId().setCode(StringUtils.isEmpty(event.getData().getCode()) ? "" : event.getData().getCode());
                mHeadEntity.getProdId().setName(StringUtil.isEmpty(event.getData().getName()) ? "" : event.getData().getName());
                mHeadEntity.getProdId().setMainUnit(event.getData().getMainUnit());
                presenterRouter.create(com.supcon.mes.module_lims.model.api.AvailableStdIdApi.class).getDefaultStandardById(mHeadEntity.getProdId().getId()+"");

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getAvailableStdIdSuccess(AvailableStdEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putString("id", entity.getAsId()+"");
        bundle.putSerializable("existItem", (Serializable) ptList);
        bundle.putString(Constant.IntentKey.SELECT_TAG, llStandardReference.getTag() + "");
        IntentRouter.go(context, Constant.AppCode.LIMS_QualityStdVerRef, bundle);
    }

    @Override
    public void getAvailableStdIdFailed(String errorMsg) {
        ToastUtils.show(context,"获取可用标准失败");
    }

    @Override
    public void getDefaultStandardByIdSuccess(TemporaryQualityStandardEntity entity) {
        if (null != entity){
            myDefaultQualityStandardEntity = entity;
            presenterRouter.create(com.supcon.mes.module_lims.model.api.AvailableStdIdApi.class).getDefaultItems(entity.getStdVersion().getId()+"");
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
            ptEntity.setInspStdVerCom(GsonUtil.gsonString(entity.data)); //对应的检验项目

            ptList.clear();
            ptList.add(ptEntity);
            adapter.setList(ptList);
        }
    }

    @Override
    public void getDefaultItemsFailed(String errorMsg) {
        ToastUtils.show(context,"无法获取默认质量标准对应的检验项目");
    }


    public interface OnRequestPtListener {
        void requestPtClick(boolean isEdit);
    }
}
