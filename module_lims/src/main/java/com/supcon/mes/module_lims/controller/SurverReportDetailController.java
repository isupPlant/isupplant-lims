package com.supcon.mes.module_lims.controller;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.base.controller.IRefreshController;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_lims.model.api.InspectReportDetailAPI;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.contract.InspectReportDetailContract;
import com.supcon.mes.module_lims.presenter.InspectReportDetailPresenter;
import com.supcon.mes.module_lims.ui.adapter.InspectReportDetailAdapter;
import com.supcon.mes.module_lims.utils.Util;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */

@Presenter(value = {
        InspectReportDetailPresenter.class
})
public class SurverReportDetailController extends BaseViewController implements InspectReportDetailContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("inspectNoTv")
    TextView inspectNoTv;
    @BindByTag("inspectbusiTypeTv")
    CustomTextView inspectbusiTypeTv;

    @BindByTag("inspectPsTv")
    CustomTextView inspectPsTv;

    @BindByTag("inspectMaterielTv")
    CustomTextView inspectMaterielTv;


    @BindByTag("inspectBatchTv")
    CustomTextView inspectBatchTv;

    @BindByTag("inspectQuantityTv")
    CustomTextView inspectQuantityTv;

    @BindByTag("inspectDeptTv")
    CustomTextView inspectDeptTv;

    @BindByTag("inspectCheckStaffTv")
    CustomTextView inspectCheckStaffTv;

    @BindByTag("inspectCheckDeptTv")
    CustomTextView inspectCheckDeptTv;

    @BindByTag("inspectCheckTimeTv")
    CustomTextView inspectCheckTimeTv;

    @BindByTag("inspectQualityStdTv")
    CustomTextView inspectQualityStdTv;

    @BindByTag("inspectCheckResultTv")
    CustomTextView inspectCheckResultTv;

    @BindByTag("contentView")
    RecyclerView contentView;
    InspectReportDetailAdapter adapter;
    public SurverReportDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void initView() {
        super.initView();

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(10, context)));

        adapter=new InspectReportDetailAdapter(context);
        contentView.setAdapter(adapter);

    }

    @Override
    public void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(1000, TimeUnit.MICROSECONDS)
                .subscribe(o->{
                    ((Activity)context).onBackPressed();
                });
    }
    private IRefreshController refreshController;
    public void setRefreshController(IRefreshController refreshController){
        this.refreshController=refreshController;
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
    }
    int type=-1;//产品、来料、其他用1，2，3来表示
    public void setReportHead(int type,SurveyReportEntity entity){
        this.type=type;
        inspectNoTv.setText(entity.getTableNo());
        inspectbusiTypeTv.setValue(entity.getBusiTypeId()!=null?entity.getBusiTypeId().getName():"");
//        inspectPsTv.setValue(entity.getInspectId()!=null?entity.getInspectId().);
        if (entity.getProdId()!=null){
            inspectMaterielTv.setValue(String.format("%s(%s)",entity.getProdId().getName(),entity.getProdId().getCode()));
        }
        inspectBatchTv.setValue(entity.getBatchCode());
        inspectQuantityTv.setValue(entity.getInspectId()!=null && entity.getInspectId().quantity!=null? Util.big2(entity.getInspectId().quantity):"");
        inspectDeptTv.setValue(entity.getInspectId()!=null && entity.getInspectId().getApplyDeptId()!=null?entity.getInspectId().getApplyDeptId().getName():"");
        inspectCheckStaffTv.setValue(entity.checkStaffId!=null ?entity.checkStaffId.getName():"");
        inspectCheckDeptTv.setValue(entity.getCheckDeptId()!=null ?entity.getCheckDeptId().getName():"");
        inspectCheckTimeTv.setValue(entity.getCheckTime()!=null? DateUtil.dateTimeFormat(entity.getCheckTime()):"");
        inspectQualityStdTv.setValue(entity.getStdVerId()!=null && entity.getStdVerId().getStdId()!=null?entity.getStdVerId().getStdId().getName():"");
        inspectCheckResultTv.setValue(entity.getCheckResult());

        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(InspectReportDetailAPI.class).getInspectReportDetails(type,entity.getId());
            }
        });
    }


    @Override
    public void getInspectReportDetailsSuccess(InspectReportDetailListEntity entity) {
        refreshController.refreshComplete();
        if (entity.data!=null && entity.data.result!=null){
            adapter.setList(entity.data.result);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getInspectReportDetailsFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}
