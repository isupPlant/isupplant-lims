package com.supcon.mes.module_sample.ui.input;


import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleRecordResultSubmitController;
import com.supcon.mes.module_sample.listener.InspectionSubRefreshListener;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;
import com.supcon.mes.module_sample.ui.input.fragment.EquipmentFragment;
import com.supcon.mes.module_sample.ui.input.fragment.InspectionProjectFragment;
import com.supcon.mes.module_sample.ui.input.fragment.MaterialFragment;
import com.supcon.mes.module_sample.ui.input.fragment.ProjectFragment;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/8/7
 * class name
 */
@Router(LimsConstant.AppCode.LIMS_InspectionItem)
public class ProjectInspectionItemsActivity extends BaseFragmentActivity {

    @BindByTag("tabLayout")
    TabLayout tabLayout;
    @BindByTag("viewPage")
    ViewPager viewPage;
    @BindByTag("dl")
    DrawerLayout dl;
    @BindByTag("rightDrawer")
    LinearLayout rightDrawer;
    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("rl_save")
    RelativeLayout rl_save;
    @BindByTag("rl_submit")
    RelativeLayout rl_submit;
    @BindByTag("rl_calculation")
    RelativeLayout rl_calculation;


    private String[] title = new String[]{SupPlantApplication.getAppContext().getResources().getString(R.string.lims_project), SupPlantApplication.getAppContext().getResources().getString(R.string.lims_equipment), SupPlantApplication.getAppContext().getResources().getString(R.string.lims_material)};
    private List<Fragment> fragmentList = new ArrayList<>();

    private ProjectFragment projectFragment;
    private EquipmentFragment equipmentFragment;
    private MaterialFragment materialFragment;
    private InspectionProjectFragment inspectionProjectFragment;

    private Long sampleId ;
    private Long sampleIdTestId;
    private String mTitle;

    private ImageView ivProject;

    private SampleRecordResultSubmitController controller;
    List<InspectionSubEntity> inspectionSubList;
    List<TestDeviceEntity> testDeviceList ;
    List<TestMaterialEntity> testMaterialList ;
    String equipmentDelete;
    String materialDelete ;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_project_inspection_items;
    }

    @Override
    protected void onInit() {
        super.onInit();

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        sampleId = getIntent().getLongExtra("sampleId",0);
        mTitle = getIntent().getStringExtra("title");
    }

    @Override
    protected void initView() {
        super.initView();

        searchTitle.showScan(false);
        ivProject = searchTitle.findViewById(R.id.ivSearchBtn);
        ivProject.setImageResource(R.drawable.ic_lims_template);
        searchTitle.setTitle("");
        leftBtn.setOnClickListener(v -> onBackPressed());

        projectFragment = new ProjectFragment();
        fragmentList.add(projectFragment);

        equipmentFragment = new EquipmentFragment();
        fragmentList.add(equipmentFragment);

        materialFragment = new MaterialFragment();
        fragmentList.add(materialFragment);

        //使用适配器将ViewPager与Fragment绑定在一起
        viewPage.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        viewPage.setOffscreenPageLimit(3);
        //将TabLayout与ViewPager绑定
        tabLayout.setupWithViewPager(viewPage);

        inspectionProjectFragment = new InspectionProjectFragment(sampleId,mTitle);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_inspection_items,inspectionProjectFragment)
                .commit();

        controller = new SampleRecordResultSubmitController();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(ivProject)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(o -> openDrawLayout());
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Fragment fragment = fragmentList.get(i);
                if (fragment instanceof ProjectFragment){
                    rl_calculation.setVisibility(View.VISIBLE);
                }else {
                    rl_calculation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

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
                .throttleFirst(300,TimeUnit.MILLISECONDS)
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

                        SampleRecordResultSubmitEntity entity = new SampleRecordResultSubmitEntity("save",
                                sampleId,sampleIdTestId,inspectionSubList,testDeviceList,testMaterialList,equipmentDelete,materialDelete);
                        controller.recordResultSubmit(ProjectInspectionItemsActivity.this,2,entity);
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

                        SampleRecordResultSubmitEntity entity = new SampleRecordResultSubmitEntity("submit",
                                sampleId,sampleIdTestId,inspectionSubList,testDeviceList,testMaterialList,equipmentDelete,materialDelete);
                        controller.recordResultSubmit(ProjectInspectionItemsActivity.this,2,entity);
                    }
                });
        controller.setSubmitOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                if (result == 1){//保存
                    inspectionProjectFragment.againRefresh();
                }else if (result == 2){//提交
                        inspectionProjectFragment.lookNext(new InspectionSubRefreshListener() {
                            @Override
                            public void refreshOver(List<InspectionItemsEntity> list) {
                                if (list.size() > 0){
                                    inspectionProjectFragment.refreshItem(0);
                                }else {
                                    EventBus.getDefault().post("refersh");
                                    onBackPressed();
                                }
                            }
                        });
                    }

            }
        });
    }



    public void openDrawLayout() {
        if (dl.isDrawerOpen(rightDrawer)) {
            dl.closeDrawer(rightDrawer);
        } else {
            dl.openDrawer(rightDrawer);
        }
    }

    public void notifyInspectionItemsRefresh(Long sampleIdTestId, String name){
        this.sampleIdTestId = sampleIdTestId;
        //设置标题为 当前选中的检验项目
        searchTitle.setTitle(name);
        //如果当前抽屉是打开的状态 就让他关闭
        if (dl.isDrawerOpen(rightDrawer)){
            dl.closeDrawer(rightDrawer);
        }
        //通知检验分项 刷新数据
        projectFragment.setSampleTesId(sampleIdTestId);
        equipmentFragment.setSampleTesId(sampleIdTestId);
        materialFragment.setSampleTesId(sampleIdTestId);
    }


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

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
