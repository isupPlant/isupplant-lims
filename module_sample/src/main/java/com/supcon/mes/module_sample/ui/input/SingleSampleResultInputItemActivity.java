package com.supcon.mes.module_sample.ui.input;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleRecordResultSubmitController;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;

import com.supcon.mes.module_sample.ui.input.fragment.SingleProjectFragment;

import org.greenrobot.eventbus.EventBus;
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

    SampleEntity sampleEntity;

    @BindByTag("rl_save")
    RelativeLayout rl_save;
    @BindByTag("rl_submit")
    RelativeLayout rl_submit;
    @BindByTag("rl_calculation")
    RelativeLayout rl_calculation;
    SampleRecordResultSubmitController submitController;
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
        titleText.setText(R.string.lims_single_sample_result_input);
        fragment=new SingleProjectFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("sampleId",sampleEntity.getId());
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.ll_fragment,fragment).commit();

    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });
        RxView.clicks(rl_save)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        List<InspectionSubEntity> inspectionSubList = fragment.getInspectionSubList();
                        SampleRecordResultSubmitEntity submitEntity=new SampleRecordResultSubmitEntity("save",sampleEntity.getId(),inspectionSubList);
                        submitController.recordResultSubmit(SingleSampleResultInputItemActivity.this,1,submitEntity);
                    }
                });
        RxView.clicks(rl_submit)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(o ->  {
                    List<InspectionSubEntity> inspectionSubList = fragment.getInspectionSubList();
                    SampleRecordResultSubmitEntity submitEntity=new SampleRecordResultSubmitEntity("submit",sampleEntity.getId(),inspectionSubList);
                    submitController.recordResultSubmit(SingleSampleResultInputItemActivity.this,2,submitEntity);
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
    }

}
