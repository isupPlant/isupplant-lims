package com.supcon.mes.module_incoming.ui.inspection;

import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_incoming.R;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.controller.InspectionApplicationDetailController;
import com.supcon.mes.module_lims.model.api.InspectionApplicationDetailAPI;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationDetailContract;
import com.supcon.mes.module_lims.presenter.InspectionApplicationDetailPresenter;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
@Router(value = "", viewCode = "purchInspectView,purchInspectEdit")
@Presenter(value = {InspectionApplicationDetailPresenter.class})
@Controller(value = {InspectionApplicationDetailController.class})
public class IncomingInspectionApplicationDetailActivity extends BaseRefreshActivity implements InspectionApplicationDetailContract.View{
    private String id;
    private String pendingId;
    private PendingEntity pendingEntity;

    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("ctSupplier")
    CustomTextView ctSupplier;

    @Override
    protected int getLayoutID() {
        return com.supcon.mes.module_lims.R.layout.activity_inspection_application_detail;
    }

    @Override
    protected void onInit() {
        super.onInit();
        id =  getIntent().getStringExtra("id");
        pendingId = getIntent().getStringExtra("pendingId");
        pendingEntity = (PendingEntity) getIntent().getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_incoming_inspection_application));
        ctSupplier.setVisibility(View.VISIBLE);

        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
        goRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();

        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != id && null != pendingId){
                    presenterRouter.create(InspectionApplicationDetailAPI.class).getInspectionDetailHeaderData(id,pendingId);
                }else {
                    //通过待办 获取检验申请单的id
                    presenterRouter.create(InspectionApplicationDetailAPI.class).getInspectionApplicationByPending(pendingEntity.modelId,pendingEntity.id);
                }

            }
        });

        getController(InspectionApplicationDetailController.class).setRefreshHeadData(new InspectionApplicationDetailController.OnRequestHeadListener() {
            @Override
            public void requestHeadClick() {
                goRefresh();
            }
        });
    }

    public void goRefresh(){
        refreshController.refreshBegin();
    }


    @Override
    public void getInspectionDetailHeaderDataSuccess(InspectionApplicationDetailHeaderEntity entity) {
        getController(InspectionApplicationDetailController.class).setHeardData(2,entity, new InspectionApplicationDetailController.OnRequestPtListener() {
            @Override
            public void requestPtClick(boolean isEdit) {
                //myEdit = isEdit;
                //请求pt
                presenterRouter.create(InspectionApplicationDetailAPI.class).getInspectionDetailPtData(LimsConstant.PleaseCheck.INCOMING_PLEASE_CHECK,isEdit,id);
            }
        });
    }

    @Override
    public void getInspectionDetailHeaderDataFailed(String errorMsg) {
        refreshController.refreshComplete();
    }

    @Override
    public void getInspectionDetailPtDataSuccess(InspectionDetailPtListEntity entity) {
        getController(InspectionApplicationDetailController.class).setPtData(entity.data.result);
        refreshController.refreshComplete();
    }

    @Override
    public void getInspectionDetailPtDataFailed(String errorMsg) {
        refreshController.refreshComplete();
    }

    @Override
    public void getInspectionApplicationByPendingSuccess(InspectionApplicationEntity entity) {
        if (null != entity){
            id = entity.getId()+"";
            presenterRouter.create(InspectionApplicationDetailAPI.class).getInspectionDetailHeaderData(entity.getId()+"",pendingEntity.id+"");
        }
    }


    @Override
    public void getInspectionApplicationByPendingFailed(String errorMsg) {
        refreshController.refreshComplete();
    }
}
