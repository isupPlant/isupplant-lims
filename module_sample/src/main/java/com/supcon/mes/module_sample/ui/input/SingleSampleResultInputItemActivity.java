package com.supcon.mes.module_sample.ui.input;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.ModuleConfigEntity;
import com.supcon.mes.middleware.model.bean.PopupWindowEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.constant.TemporaryData;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.module_lims.ui.popu.LIMSPopupWindow;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleRecordResultSubmitController;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;

import com.supcon.mes.module_sample.ui.input.fragment.SingleProjectFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/13
 * Email:wanghaidong1@supcon.com
 */


@Router(Constant.Router.SINGLE_SAMPLE_RESULT_INPUT_ITEM)
public class SingleSampleResultInputItemActivity extends BaseFragmentActivity {



    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;

    SampleEntity sampleEntity;

    @BindByTag("rl_save")
    RelativeLayout rl_save;
    @BindByTag("rl_submit")
    RelativeLayout rl_submit;
    @BindByTag("rl_calculation")
    RelativeLayout rl_calculation;
    SampleRecordResultSubmitController submitController;

    private LIMSPopupWindow mCustomPopupWindow;
    private List<PopupWindowEntity> popupWindowEntityList = new ArrayList<>();
    private SystemConfigController mSystemConfigController;
    private String specialResultStr = "";
    @Override
    protected int getLayoutID() {
        return R.layout.ac_single_sample_input_result_item;
    }


    @Override
    protected void onInit() {
        super.onInit();
        sampleEntity = (SampleEntity) getIntent().getSerializableExtra("sampleEntity");
        submitController=new SampleRecordResultSubmitController();
    }

    SingleProjectFragment fragment;
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(R.string.lims_result_input);
        fragment=new SingleProjectFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("sampleId",sampleEntity.getId());
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.ll_fragment,fragment).commit();

        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.ic_home_page_edit_more);

        PopupWindowEntity entity = new PopupWindowEntity();
        entity.setText(getString(R.string.lims_automatic_acquisition));

        PopupWindowEntity entity1 = new PopupWindowEntity();
        entity1.setText(getString(R.string.lims_file_analysis));

        PopupWindowEntity entity2 = new PopupWindowEntity();
        entity2.setText(getString(R.string.lims_serial_port_acquisition));

        popupWindowEntityList.clear();
        popupWindowEntityList.add(entity);
        popupWindowEntityList.add(entity1);
        popupWindowEntityList.add(entity2);
        mCustomPopupWindow = new LIMSPopupWindow(context,popupWindowEntityList);

        mSystemConfigController = new SystemConfigController(context);
        mSystemConfigController.getModuleConfig(LimsConstant.ModuleCode.LIMS_FILE_ANALYSIS_MENU_CODE, LimsConstant.Keys.LIMSDC_OCD_LIMSDCUrl, new OnSuccessListener() {
            @Override
            public void onSuccess(Object result) {
                if (null != result){
                    try {
                        ModuleConfigEntity bean = (ModuleConfigEntity)result;
                        specialResultStr = bean.getLimsDCUrl();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    EventBus.getDefault().post(new RefreshEvent());
                    back();
                });
        RxView.clicks(rl_save)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!fragment.change()){
                            ToastUtils.show(SingleSampleResultInputItemActivity.this,context.getResources().getString(R.string.lims_project_check_change));
                            return;
                        }
                        List<InspectionSubEntity> inspectionSubList = fragment.getInspectionSubList();
                        SampleRecordResultSubmitEntity submitEntity=new SampleRecordResultSubmitEntity("save",sampleEntity.getId(),inspectionSubList);
                        submitController.recordResultSubmit(SingleSampleResultInputItemActivity.this,1,submitEntity, TemporaryData.temporaryFileId);
                    }
                });
        RxView.clicks(rl_submit)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(o ->  {
                    List<InspectionSubEntity> inspectionSubList = fragment.getInspectionSubList();
                    SampleRecordResultSubmitEntity submitEntity=new SampleRecordResultSubmitEntity("submit",sampleEntity.getId(),inspectionSubList);
                    submitController.recordResultSubmit(SingleSampleResultInputItemActivity.this,1,submitEntity,TemporaryData.temporaryFileId);
                });
        RxView.clicks(rl_calculation)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        fragment.manualCalculate();
                    }
                });
        submitController.setSubmitOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer type) {
                if (type==1){//保存
                    fragment.goRefresh();
                }else if (type==2){//提交
                    EventBus.getDefault().post(new RefreshEvent());
                    back();
                }
            }
        });

        RxView.clicks(rightBtn)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mCustomPopupWindow.showPopupWindow(rightBtn);
                        mCustomPopupWindow.setOnItemClick(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position){
                                    case 0: //自动采集
                                        // TODO: 2021/3/31
                                        String url = "http://" + specialResultStr + "/lims-collection-web/ws/rs/analysisDataWS/getFormatDataByCollectCode";
                                        fragment.getFormatDataByCollectCode(url,sampleEntity.getCode());
                                        mCustomPopupWindow.dismiss();
                                        break;
                                    case 1: //文件解析
                                        IntentRouter.go(context, LimsConstant.AppCode.LIMS_SAMPLE_FILE_ANALYSE);
                                        mCustomPopupWindow.dismiss();
                                        break;
                                    case 2: //串口采集
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constant.IntentKey.SELECT_TAG,"Serial");
                                        IntentRouter.go(context, LimsConstant.AppCode.LIMS_SerialRef);
                                        mCustomPopupWindow.dismiss();
                                        break;
                                }
                            }
                        });
                    }
                });
    }

}
