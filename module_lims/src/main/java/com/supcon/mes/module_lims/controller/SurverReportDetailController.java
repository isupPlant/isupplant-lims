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
import com.supcon.mes.module_lims.model.api.StdJudgeSpecAPI;
import com.supcon.mes.module_lims.model.bean.InspectHeadReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.contract.InspectReportDetailContract;
import com.supcon.mes.module_lims.model.contract.StdJudgeSpecContract;
import com.supcon.mes.module_lims.presenter.InspectReportDetailPresenter;
import com.supcon.mes.module_lims.presenter.StdJudgeSpecPresenter;
import com.supcon.mes.module_lims.ui.adapter.InspectReportDetailAdapter;
import com.supcon.mes.module_lims.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */

@Presenter(value = {
        InspectReportDetailPresenter.class,
        StdJudgeSpecPresenter.class
})
public class SurverReportDetailController extends BaseViewController implements InspectReportDetailContract.View, StdJudgeSpecContract.View {

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

    @BindByTag("inspectUnitTv")
    CustomTextView inspectUnitTv;


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
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(InspectReportDetailAPI.class).getInspectHeadReport(entity.getId());
            }
        });
    }


    private void setInspectHead(InspectHeadReportEntity entity){
        inspectNoTv.setText(entity.inspectId!=null?entity.inspectId.getTableNo():"");
        inspectbusiTypeTv.setValue(entity.busiTypeId!=null?entity.busiTypeId.getName():"");
        inspectPsTv.setValue(entity.inspectId!=null && entity.inspectId.psId!=null?entity.inspectId.psId.getName():"");
        if (entity.prodId!=null){
            inspectMaterielTv.setValue(String.format("%s(%s)",entity.prodId.getName(),entity.prodId.getCode()));
            inspectUnitTv.setValue(entity.prodId.getMainUnit()!=null?entity.prodId.getMainUnit().getName():"");
        }
        inspectBatchTv.setValue(entity.batchCode);
        inspectQuantityTv.setValue(entity.inspectId!=null && entity.inspectId.quantity!=null? Util.big2(entity.inspectId.quantity):"");
        inspectDeptTv.setValue(entity.inspectId!=null && entity.inspectId.getApplyDeptId()!=null?entity.inspectId.getApplyDeptId().getName():"");
        inspectCheckStaffTv.setValue(entity.checkStaffId!=null ?entity.checkStaffId.getName():"");
        inspectCheckDeptTv.setValue(entity.checkDeptId!=null ?entity.checkDeptId.getName():"");
        inspectCheckTimeTv.setValue(entity.checkTime!=null? DateUtil.dateTimeFormat(entity.checkTime):"");
        inspectQualityStdTv.setValue(entity.stdVerId!=null ?entity.stdVerId.getName():"");
        inspectCheckResultTv.setValue(entity.checkResult);
    }
    @Override
    public void getInspectHeadReportSuccess(InspectHeadReportEntity entity) {
        if (entity!=null){
            setInspectHead(entity);
            presenterRouter.create(InspectReportDetailAPI.class).getInspectReportDetails(type,entity.id);
            Map<String,Object> params=new HashMap<>();
            params.put("inspectReportId",entity.id);
            params.put("pageNo",1);
            params.put("pageSize",65535);
            presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(params);
        }
    }

    @Override
    public void getInspectHeadReportFailed(String errorMsg) {
        refreshController.refreshComplete();
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

    @Override
    public void getReportComListSuccess(StdJudgeSpecListEntity entity) {
        if (entity.data!=null)
            adapter.setStdJudgeSpecEntities(entity.data.result);
    }

    @Override
    public void getReportComListFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }

}
