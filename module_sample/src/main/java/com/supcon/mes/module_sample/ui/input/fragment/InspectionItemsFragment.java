package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.fragment.BaseFragment;

import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleRecordResultSubmitController;
import com.supcon.mes.module_sample.listener.InspectionSubRefreshListener;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputPADActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
public class InspectionItemsFragment extends BaseFragment {

    @BindByTag("tabLayout")
    TabLayout tabLayout;
    @BindByTag("viewPage")
    ViewPager viewPage;
    RelativeLayout rl_save;
    @BindByTag("rl_submit")
    RelativeLayout rl_submit;
    @BindByTag("rl_calculation")
    RelativeLayout rl_calculation;
    @BindByTag("tvSerial")
    TextView tvSerial;

    private String[] title = new String[]{context.getResources().getString(R.string.lims_project), context.getResources().getString(R.string.lims_equipment), context.getResources().getString(R.string.lims_material)};
    private List<Fragment> fragmentList = new ArrayList<>();
    SampleResultInputPADActivity activity;

    private ProjectFragment projectFragment;
    private EquipmentFragment equipmentFragment;
    private MaterialFragment materialFragment;
    private SampleRecordResultSubmitController controller;
    private Long sampleId,sampleTestId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SampleResultInputPADActivity) context;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_inspection_sub_item;
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initView() {
        super.initView();
        projectFragment = new ProjectFragment();
        fragmentList.add(projectFragment);

        equipmentFragment = new EquipmentFragment();
        fragmentList.add(equipmentFragment);

        materialFragment = new MaterialFragment();
        fragmentList.add(materialFragment);

        //使用适配器将ViewPager与Fragment绑定在一起
        viewPage.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
        //将TabLayout与ViewPager绑定
        tabLayout.setupWithViewPager(viewPage);

        controller = new SampleRecordResultSubmitController();

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        activity.setOnInspectionItemSubRefreshListener(new SampleResultInputPADActivity.OnInspectionItemSubRefreshListener() {
            @Override
            public void InspectionItemSubRefresh(Long sampleTesId) {
                if (null != sampleTesId){
                    projectFragment.setSampleTesId(sampleTesId);
                    equipmentFragment.setSampleTesId(sampleTesId);
                    materialFragment.setSampleTesId(sampleTesId);
                }
            }
        });

        RxView.clicks(rl_calculation)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        projectFragment.manualCalculate();
                    }
                });

        RxView.clicks(rl_save)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        List<InspectionSubEntity> recordList = projectFragment.getRecordList();
                        List<InspectionSubEntity> inspectionSubList = projectFragment.getInspectionSubList();

                        List<TestDeviceEntity> testDeviceList = equipmentFragment.getTestDeviceList();
                        List<TestDeviceEntity> deviceRecordList = equipmentFragment.getRecordList();

                        List<TestMaterialEntity> testMaterialList = materialFragment.getTestMaterialList();
                        List<TestMaterialEntity> materialRecordList = materialFragment.getRecordList();

                        if (recordList.toString().equals(inspectionSubList.toString())
                                && deviceRecordList.toString().equals(testDeviceList.toString())
                                && materialRecordList.toString().equals(testMaterialList.toString())){
                            ToastUtils.show(context,getResources().getString(R.string.lims_project_check_change));
                            return;
                        }



                        String equipmentDelete = equipmentFragment.getDeleteList();
                        String materialDelete = materialFragment.getDeleteList();
                        sampleId = activity.getSampleId();
                        sampleTestId = activity.getSampleTesId();
                        SampleRecordResultSubmitEntity entity = new SampleRecordResultSubmitEntity("save",
                                sampleId,sampleTestId,inspectionSubList,testDeviceList,testMaterialList,equipmentDelete,materialDelete);
                        controller.recordResultSubmit(activity,2,entity);
                    }
                });

        RxView.clicks(rl_submit)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        List<InspectionSubEntity> inspectionSubList = projectFragment.getInspectionSubList();
                        List<TestDeviceEntity> testDeviceList = equipmentFragment.getTestDeviceList();
                        List<TestMaterialEntity> testMaterialList = materialFragment.getTestMaterialList();
                        String equipmentDelete = equipmentFragment.getDeleteList();
                        String materialDelete = materialFragment.getDeleteList();
                        sampleId = activity.getSampleId();
                        sampleTestId = activity.getSampleTesId();
                        SampleRecordResultSubmitEntity entity = new SampleRecordResultSubmitEntity("submit",
                                sampleId,sampleTestId,inspectionSubList,testDeviceList,testMaterialList,equipmentDelete,materialDelete);
                        controller.recordResultSubmit(activity,2,entity);
                    }
                });

        controller.setSubmitOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                if (result == 1){//保存
                    if (null != activity.getInspectionProjectFragment()){
                        ((InspectionProjectFragment)activity.getInspectionProjectFragment()).againRefresh();
                    }
                }else if (result == 2){//提交
                    if (null != activity.getInspectionProjectFragment()){
                        ((InspectionProjectFragment)activity.getInspectionProjectFragment()).lookNext(new InspectionSubRefreshListener() {
                            @Override
                            public void refreshOver(List<InspectionItemsEntity> list) {
                                if (list.size()>0){
                                    ((InspectionProjectFragment)activity.getInspectionProjectFragment()).refreshItem(0);
                                }else {
                                    //刷新样品数据
                                    activity.sampleRefresh();
                                }
                            }
                        });
                    }
                }

            }
        });

        tvSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SampleFragment.selectPosition==-1){
                    ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG,tvSerial.getTag().toString());
                IntentRouter.go(context, LimsConstant.AppCode.LIMS_SerialRef);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
    }


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
