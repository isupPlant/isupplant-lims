package com.supcon.mes.module_lims.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.utils.DateUtil;
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
import com.supcon.mes.module_lims.event.MaterialDateEvent;
import com.supcon.mes.module_lims.model.bean.BusinessTypeEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.bean.SamplingPointEntity;
import com.supcon.mes.module_lims.model.contract.BusinessTypeApi;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationDetailApi;
import com.supcon.mes.module_lims.presenter.BusinessTypeReferencePresenter;
import com.supcon.mes.module_lims.presenter.InspectionApplicationDetailPresenter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * author huodongsheng
 * on 2020/7/13
 * class name 检验申请详情Controller
 */
@Presenter(value = {InspectionApplicationDetailPresenter.class, BusinessTypeReferencePresenter.class})
public class InspectionApplicationDetailController extends BaseViewController implements InspectionApplicationDetailApi.View, BusinessTypeApi.View {

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;

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

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("ivNeedLaboratory")
    ImageView ivNeedLaboratory;

    @BindByTag("rlNeedLaboratory")
    RelativeLayout rlNeedLaboratory;



    private String id;
    private String pendingId;
    private String type;

    private InspectionApplicationDetailHeaderEntity mHeadEntity;


    private SinglePickController<String> mBusinessTypeController;
    private DatePickController datePickController;

    private List<String> businessTypeStrList = new ArrayList<>(); // 用于存放展示时业务类型的集合
    private List<BusinessTypeEntity> businessTypeList = new ArrayList<>();// 存放提交时业务类型的集合

    public InspectionApplicationDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionApplicationDetailApi.class).getIfUpload();
    }

    @Override
    public void initView() {
        super.initView();
        searchTitle.showScan(false);

        if (type.equals(BusinessType.PleaseCheck.PRODUCT_PLEASE_CHECK) || (type.equals(BusinessType.PleaseCheck.OTHER_PLEASE_CHECK))){
            ctSupplier.setVisibility(View.GONE);
        }else {
            ctSupplier.setVisibility(View.VISIBLE);
        }


        mBusinessTypeController = new SinglePickController<>((Activity)context);
        mBusinessTypeController.textSize(18);

        datePickController = new DatePickController((Activity)context);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionApplicationDetailApi.class).getInspectionDetailHeaderData(id,pendingId);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).onBackPressed();
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
                showSpinnerSelector(ctBusinessType,businessTypeStrList);
            }
        });

        //请检人选择监听
        ctCheckPeople.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                if (action != -1){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                    bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                    bundle.putString(Constant.IntentKey.SELECT_TAG, ctCheckPeople.getTag()+"");
                    IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                }else {
                    ctCheckPeople.setValue("");
                }
            }
        });

        //请检部门选择监听
        ctCheckDepartment.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG,ctCheckDepartment.getTag()+"");
                IntentRouter.go(context, Constant.Router.DEPART_SELECT,bundle);
            }
        });

        //采样点参照监听
        ctSamplingPoint.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG,ctSamplingPoint.getTag()+"");
                IntentRouter.go(context,Constant.AppCode.LIMS_PickSiteRefPart,bundle);
            }
        });

        //（点击物料）物料信息参照监听
        ctMateriel.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("radio",true);
                IntentRouter.go(context,Constant.AppCode.LIMS_MaterialRef,bundle);
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
//        if (StringUtil.isEmpty(ctMateriel.getValue().trim())){
//            ToastUtils.show(context,"请先选择物料选项");
//        }
//        RxView.clicks(ceMaterielBatchNumber.editText())


    }

    @Override
    public void getInspectionDetailHeaderDataSuccess(InspectionApplicationDetailHeaderEntity entity) {
        if (null != entity){
            mHeadEntity = entity;
            if (null != entity.getPending() && null != entity.getPending().openUrl){
                if (entity.getPending().openUrl.contains(BusinessType.WorkType.PRODUCT_INSPECT_EDIT)){
                    setCanEdit();
                }else if (entity.getPending().openUrl.contains(BusinessType.WorkType.PRODUCT_INSPECT_VIEW)){ //表示不可编辑
                    setCannotEdit();
                }
            }else {
                setCannotEdit();
            }
            setMakingRules(entity);
        }
    }

    @Override
    public void getInspectionDetailHeaderDataFailed(String errorMsg) {

    }

    @Override
    public void getInspectionDetailPtDataSuccess(InspectionDetailPtListEntity entity) {

    }

    @Override
    public void getInspectionDetailPtDataFailed(String errorMsg) {

    }

    @Override
    public void getIfUploadSuccess(IfUploadEntity entity) {
        if (null != entity){
            if (!entity.getDealSuccessFlag()){
                rlNeedLaboratory.setVisibility(View.GONE);
            }else {
                rlNeedLaboratory.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void getIfUploadFailed(String errorMsg) {

    }

    public void setId(String id){
        this.id = id;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setPendingId(String pendingId){
        this.pendingId = pendingId;
    }

    private void setCannotEdit(){
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

    private void setCanEdit(){
        cdCheckTime.setEditable(true);
        ctCheckPeople.setEditable(true);
        ctCheckDepartment.setEditable(true);
        ctSamplingPoint.setEditable(true);
        ctBusinessType.setEditable(true);
        ctSupplier.setEditable(true);
        ctMateriel.setEditable(true);
        ceMaterielNum.setEditable(true);
        ceMaterielBatchNumber.setEditable(true);
    }

    private void setMakingRules(InspectionApplicationDetailHeaderEntity entity){
        if (null != entity){
            if (null != entity.getTableTypeId()){ //单据来源类型不为空时，表头物料编码、批号不可编辑
                ctMateriel.setEditable(false);
                ceMaterielBatchNumber.setEditable(false);
            }
            //请检时间
            cdCheckTime.setContent(entity.getApplyTime() == null ? "--" : DateUtil.dateFormat(entity.getApplyTime(),"yyyy-MM-dd HH:mm:ss"));
            //请检人
            if (null != entity.getApplyStaffId()){
                ctCheckPeople.setContent(StringUtil.isEmpty(entity.getApplyStaffId().getName()) ? "--" : entity.getApplyStaffId().getName());
            }else {
                ctCheckPeople.setContent("--");
            }
            //请检部门
            if (null != entity.getApplyDeptId()){
                ctCheckDepartment.setContent(StringUtil.isEmpty(entity.getApplyDeptId().getName()) ? "--" :entity.getApplyDeptId().getName());
            }else {
                ctCheckDepartment.setContent("--");
            }
            //采样点
            if (null != entity.getPsId()){
                ctSamplingPoint.setContent(StringUtil.isEmpty(entity.getPsId().getName()) ? "--" : entity.getPsId().getName());
            }else {
                ctSamplingPoint.setContent("--");
            }
            //业务类型
            if (null != entity.getBusiTypeId()){
                ctBusinessType.setContent(StringUtil.isEmpty(entity.getBusiTypeId().getName()) ? "--" : entity.getBusiTypeId().getName());
            }else {
                ctBusinessType.setContent("--");
            }
            //物料编码 名称 计量单位
            if (null != entity.getProdId()){
                if (!StringUtil.isEmpty(entity.getProdId().getCode()) && !StringUtil.isEmpty(entity.getProdId().getName())){
                    ctMateriel.setContent(entity.getProdId().getName()+"("+entity.getProdId().getCode()+")");
                }else {
                    if (StringUtil.isEmpty(entity.getProdId().getName()) && StringUtil.isEmpty(entity.getProdId().getCode())){
                        ctMateriel.setContent("--");
                    }else {
                        if (!StringUtil.isEmpty(entity.getProdId().getName())){
                            ctMateriel.setContent(entity.getProdId().getName());
                        }else if (!StringUtil.isEmpty(entity.getProdId().getCode())){
                            ctMateriel.setContent(entity.getProdId().getCode());
                        }
                    }

                }

                if (null != entity.getProdId().getSampleUnit()){
                    ctMaterielUnit.setContent(StringUtil.isEmpty(entity.getProdId().getMainUnit().getName()) ? "--" : entity.getProdId().getMainUnit().getName());
                }else {
                    ctMaterielUnit.setContent("--");
                }

            }else {
                ctMateriel.setContent("--");
                ctMaterielUnit.setContent("--");
            }

            //数量
            ceMaterielNum.setContent(null == entity.getQuantity() ? "--" : entity.getQuantity().setScale(2, BigDecimal.ROUND_DOWN)+"");
            //批号
            ceMaterielBatchNumber.setContent(StringUtil.isEmpty(entity.getBatchCode()) ? "--" : entity.getBatchCode());
            //供应商

            //是否实验室检验
            setNeedLaboratory();


        }
    }

    private void setNeedLaboratory(){
        if (mHeadEntity.getNeedLab()){
            ivNeedLaboratory.setBackgroundResource(R.drawable.ic_check_yes);
        }else {
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

    public void requestBusinessTypeData(){
        presenterRouter.create(com.supcon.mes.module_lims.model.api.BusinessTypeApi.class).getBusinessTypeList();
    }

    @Override
    public void getBusinessTypeListSuccess(BusinessTypeListEntity entity) {
        businessTypeStrList.clear();
        businessTypeList.clear();
        if (null != entity){
            if (null != entity.data){
                if (null != entity.data.result){
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
        ToastUtils.show(context,"getBusinessTypeListFailed"+errorMsg);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectDataEvent(SelectDataEvent selectDataEvent){  //部门选择 eventBus 回调
        if (selectDataEvent.getEntity() instanceof DepartmentEntity){
            DepartmentEntity departmentEntity = (DepartmentEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctCheckDepartment.getTag()+"")){
                    ctCheckDepartment.setValue(departmentEntity.getName());
                }
            }
        } else if (selectDataEvent.getEntity() instanceof SamplingPointEntity){  //采样点 eventBus 回调
            SamplingPointEntity samplingPointEntity =  (SamplingPointEntity)selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctSamplingPoint.getTag()+"")){
                    ctSamplingPoint.setValue(samplingPointEntity.getName());
                }
            }
        } else if (selectDataEvent.getEntity() instanceof ContactEntity){  //请检人 eventBus 回调
            ContactEntity contactEntity = (ContactEntity) selectDataEvent.getEntity();
            if (!TextUtils.isEmpty(selectDataEvent.getSelectTag())){
                if (selectDataEvent.getSelectTag().equals(ctCheckPeople.getTag()+"")){
                    ctCheckPeople.setValue(contactEntity.name);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectMaterial(MaterialDateEvent event){
        if (event.isRadio()){
            if (null != event.getData()){
                if (!StringUtil.isEmpty(event.getData().getCode()) && !StringUtil.isEmpty(event.getData().getName())){
                    ctMateriel.setContent(event.getData().getName()+"("+event.getData().getCode()+")");
                }else {
                    if (StringUtil.isEmpty(event.getData().getName()) && StringUtil.isEmpty(event.getData().getCode())){
                        ctMateriel.setContent("--");
                    }else {
                        if (!StringUtil.isEmpty(event.getData().getName())){
                            ctMateriel.setContent(event.getData().getName());
                        }else if (!StringUtil.isEmpty(event.getData().getCode())){
                            ctMateriel.setContent(event.getData().getCode());
                        }
                    }

                }
                if (null != event.getData().getMainUnit()){
                    ctMaterielUnit.setContent(StringUtil.isEmpty(event.getData().getMainUnit().getName()) ? "--" : event.getData().getMainUnit().getName());
                }else {
                    ctMaterielUnit.setContent("--");
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
