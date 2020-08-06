package com.supcon.mes.module_sample.ui.input.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.app.annotation.BindByTag;
import com.supcon.common.view.base.fragment.BaseFragment;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
public class InspectionSubItemFragment extends BaseFragment {

    @BindByTag("tabLayout")
    TabLayout tabLayout;
    @BindByTag("viewPage")
    ViewPager viewPage;

    private String[] title = new String[]{"项目", "设备", "材料"};
    private List<Fragment> fragmentList = new ArrayList<>();
    SampleResultInputActivity activity;

    private ProjectFragment projectFragment;
    private EquipmentFragment equipmentFragment;
    private MaterialFragment materialFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SampleResultInputActivity) context;
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

    }

    @Override
    protected void initListener() {
        super.initListener();
        activity.setOnInspectionItemSubRefreshListener(new SampleResultInputActivity.OnInspectionItemSubRefreshListener() {
            @Override
            public void InspectionItemSubRefresh(Long sampleTesId) {
                if (null != sampleTesId){
                    projectFragment.setSampleTesId(sampleTesId);
                    equipmentFragment.setSampleTesId(sampleTesId);
                    materialFragment.setSampleTesId(sampleTesId);
                }
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
