package com.supcon.mes.module_sample.ui;


import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.ModuleConfigEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.presenter.SampleResultCheckProjectDetailPresenter;
import com.supcon.mes.module_sample.ui.input.fragment.SampleEquipmentFragment;
import com.supcon.mes.module_sample.ui.input.fragment.SampleMaterialFragment;
import com.supcon.mes.module_sample.ui.input.fragment.SampleResultCheckProjectFragment;

import java.util.ArrayList;
import java.util.List;

@Router(Constant.AppCode.LIMS_SampleResultCheckProjectDetail)
@Presenter(value = {SampleResultCheckProjectDetailPresenter.class})
public class SampleResultCheckProjectDetailActivity extends BaseFragmentActivity {
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("ivSearchBtn")
    ImageView ivSearchBtn;
    @BindByTag("scanRightBtn")
    CustomImageButton scanRightBtn;
    @BindByTag("tabLayout")
    TabLayout tabLayout;
    @BindByTag("viewPage")
    ViewPager viewPage;

    private String[] title = new String[]{SupPlantApplication.getAppContext().getResources().getString(R.string.lims_project), SupPlantApplication.getAppContext().getResources().getString(R.string.lims_equipment), SupPlantApplication.getAppContext().getResources().getString(R.string.lims_material)};
    private List<Fragment> fragmentList = new ArrayList<>();
    private SampleResultCheckProjectFragment projectFragment;
    private SampleEquipmentFragment equipmentFragment;
    private SampleMaterialFragment materialFragment;
    private Long sampleId ;
    private String mTitle;

    private SystemConfigController mSystemConfigController;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_result_check_project_detail;
    }


    @Override
    protected void onInit() {
        super.onInit();

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        sampleId = getIntent().getLongExtra(Constant.IntentKey.LIMS_SAMPLE_ID,0);
        mTitle = getIntent().getStringExtra(Constant.IntentKey.LIMS_SAMPLE_PROJECT_NAME);

    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(mTitle);
        ivSearchBtn.setVisibility(View.GONE);
        scanRightBtn.setVisibility(View.GONE);
//        searchTitle.showScan(true);
//        ivProject = searchTitle.findViewById(R.id.ivSearchBtn);
//        ivProject.setImageResource(R.drawable.ic_lims_template);
//        searchTitle.setTitle("");

//        searchBtn = searchTitle.findViewById(R.id.scanRightBtn);
        leftBtn.setOnClickListener(v -> onBackPressed());

        projectFragment = new SampleResultCheckProjectFragment();
        fragmentList.add(projectFragment);

        equipmentFragment = new SampleEquipmentFragment();
        fragmentList.add(equipmentFragment);

        materialFragment = new SampleMaterialFragment();
        fragmentList.add(materialFragment);

        //??????????????????ViewPager???Fragment???????????????
        viewPage.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        viewPage.setOffscreenPageLimit(3);
        //???TabLayout???ViewPager??????
        tabLayout.setupWithViewPager(viewPage);

        projectFragment.sampleTesId = sampleId;
        equipmentFragment.sampleTesId = sampleId;
        materialFragment.sampleTesId = sampleId;

        mSystemConfigController = new SystemConfigController(context);
        mSystemConfigController.getModuleConfig(LimsConstant.ModuleCode.LIMS_FILE_ANALYSIS_MENU_CODE, LimsConstant.Keys.LIMSDC_OCD_LIMSDCUrl, new OnSuccessListener() {
            @Override
            public void onSuccess(Object result) {
                if (null != result){
                    try {
                        ModuleConfigEntity bean = (ModuleConfigEntity)result;
                        if (!TextUtils.isEmpty(bean.getLimsDCUrl())) {
                        }
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
//        RxView.clicks(ivProject)
//                .throttleFirst(300, TimeUnit.MILLISECONDS)
//                .subscribe(o -> openDrawLayout());


        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Fragment fragment = fragmentList.get(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }



    public void openDrawLayout() {
//        if (dl.isDrawerOpen(rightDrawer)) {
//            dl.closeDrawer(rightDrawer);
//        } else {
//            dl.openDrawer(rightDrawer);
//        }
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