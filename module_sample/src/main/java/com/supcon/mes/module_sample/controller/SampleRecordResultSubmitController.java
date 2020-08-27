package com.supcon.mes.module_sample.controller;

import android.text.TextUtils;
import android.view.View;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.custom.EnsureDialog;
import com.supcon.mes.module_sample.model.api.SampleRecordResultSubmitAPI;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSignEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;
import com.supcon.mes.module_sample.model.bean.SampleSignatureEntity;
import com.supcon.mes.module_sample.model.contract.SampleRecordResultSubmitContract;
import com.supcon.mes.module_sample.presenter.SampleRecordResultSubmitPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */

@Presenter(value = SampleRecordResultSubmitPresenter.class)
public class SampleRecordResultSubmitController extends BasePresenterController implements SampleRecordResultSubmitContract.View {
    Map<String,Object> paramsMap=new HashMap<>();
    private BaseActivity activity;
    EnsureDialog ensureDialog;

    /**
     *
     * @param activity
     * @param type 1为单样品结果录入   2为按样品结果录入
     * @param submitEntity
     */
    int type;
    public void recordResultSubmit(BaseActivity activity,int type,SampleRecordResultSubmitEntity submitEntity){
        this.activity=activity;
        this.type=type;
        paramsMap.put("dealMode",submitEntity.getDealMode());
        paramsMap.put("sampleId",submitEntity.getSampleId());
        paramsMap.put("sampleComListJson",submitEntity.getSampleComListJson());
        if (type==2){
            paramsMap.put("testDeviceListJson",submitEntity.getTestDeviceListJson());
            paramsMap.put("testMaterialListJson",submitEntity.getTestMaterialListJson());
            paramsMap.put("testDeviceDeleteIds",submitEntity.getTestDeviceDeleteIds());
            paramsMap.put("testMaterialDeleteIds",submitEntity.getTestMaterialDeleteIds());
        }
        paramsMap.put("signatureInfo","");
        if ("save".equals(submitEntity.getDealMode())){
            activity.onLoading("正在保存，请稍后...");
            presenterRouter.create(SampleRecordResultSubmitAPI.class).recordResultSubmit(paramsMap);
        }else if ("submit".equals(submitEntity.getDealMode())){
            if (checkSubmit(submitEntity.getSampleComListJson())) {
                activity.onLoading("正在提交，请稍后...");
                presenterRouter.create(SampleRecordResultSubmitAPI.class).recordResultSubmit(paramsMap);
            }
        }

    }
    private boolean checkSubmit(List<InspectionSubEntity> inspectionSubEntities){
        for (InspectionSubEntity inspectionSubEntity:inspectionSubEntities){
            if (!TextUtils.isEmpty(inspectionSubEntity.getDispValue())){
                setLoadDialog();
                return false;
            }
        }
        return true;
    }
    String buttonCode;
    private void setLoadDialog() {
        if (ensureDialog != null && ensureDialog.isShowing()) {
            return;
        }
        ensureDialog = new EnsureDialog(activity);
        ensureDialog.setCanceledOnTouchOutside(false);
        ensureDialog.show();
        ensureDialog.setOnPositiveClickListener(new EnsureDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                if (type==1){
                    buttonCode="LIMSSample_5.0.0.0_sample_recordBySample_BUTTON_sampleComSubmit";
                    presenterRouter.create(SampleRecordResultSubmitAPI.class).getSignatureEnabled("LIMSSample_5.0.0.0_sample_recordBySingleSample_BUTTON_sampleComSubmit");
                }else if (type==2){
                    buttonCode="LIMSSample_5.0.0.0_sample_recordBySingleSample_BUTTON_sampleComSubmit";
                }
                presenterRouter.create(SampleRecordResultSubmitAPI.class).getSignatureEnabled(buttonCode);
            }
        });
    }


    @Override
    public void recordResultSubmitSuccess(BAP5CommonEntity entity) {
        activity.onLoadSuccessAndExit("处理成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                if ("submit".equals(paramsMap.get("dealMode"))){
                    EventBus.getDefault().post(new RefreshEvent());
                    activity.back();
                }
            }
        });
    }

    @Override
    public void recordResultSubmitFailed(String errorMsg) {
        activity.onLoadFailed(errorMsg);
    }

    @Override
    public void getSignatureEnabledSuccess(SampleSignatureEntity entity) {
        activity.onLoading("正在提交，请稍后...");
        SampleRecordResultSignEntity signEntity=new SampleRecordResultSignEntity();
        signEntity.setButtonCode(buttonCode);
        signEntity.setSignatureType(entity.getSignatureType());
        signEntity.setFirstReason(entity.getViewName()+"(提交)");
        paramsMap.put("signatureInfo",signEntity);
        presenterRouter.create(SampleRecordResultSubmitAPI.class).recordResultSubmit(paramsMap);
    }

    @Override
    public void getSignatureEnabledFailed(String errorMsg) {
        ToastUtils.show(activity,errorMsg);
    }
}
