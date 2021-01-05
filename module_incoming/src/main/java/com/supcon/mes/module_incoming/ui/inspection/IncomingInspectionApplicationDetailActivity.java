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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.DeploymentAPI;
import com.supcon.mes.middleware.model.bean.DeploymentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.middleware.model.contract.DeploymentContract;
import com.supcon.mes.middleware.presenter.DeploymentPresenter;
import com.supcon.mes.module_incoming.R;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.controller.InspectionApplicationDetailController;
import com.supcon.mes.module_lims.model.api.InspectionApplicationDetailAPI;
import com.supcon.mes.module_lims.model.api.TableTypeAPI;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationDetailContract;
import com.supcon.mes.module_lims.model.contract.TableTypeContract;
import com.supcon.mes.module_lims.presenter.InspectionApplicationDetailPresenter;
import com.supcon.mes.module_lims.presenter.TableTypePresenter;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
@Router(value = "", viewCode = "purchInspectView,purchInspectEdit")
@Presenter(value = {InspectionApplicationDetailPresenter.class,  TableTypePresenter.class})
@Controller(value = {InspectionApplicationDetailController.class})
public class IncomingInspectionApplicationDetailActivity extends BaseRefreshActivity implements InspectionApplicationDetailContract.View,
 TableTypeContract.View {
    //DeploymentContract.View,DeploymentPresenter.class,
    private String id;
    private String pendingId;
    private String from;
    private PendingEntity pendingEntity;
    private WorkFlowButtonInfo info;

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
        from = getIntent().getStringExtra("from") == null ? "" : getIntent().getStringExtra("from");
        info = (WorkFlowButtonInfo) getIntent().getSerializableExtra("info");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_incoming_inspection_application));
        ctSupplier.setVisibility(View.VISIBLE);

        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);
        //如果不是新增 就去请求接口
        if (from.equals("add")){
            //presenterRouter.create(DeploymentAPI.class).getCurrentDeployment("purchInspectWorkFlow");
            presenterRouter.create(TableTypeAPI.class).getTableTypeByCode("purch");
            getController(InspectionApplicationDetailController.class).setIsFrom(from);
            getController(InspectionApplicationDetailController.class).setType(2);
        }else {
            goRefresh();
        }
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

        getController(InspectionApplicationDetailController.class).setRequestWorkFlowListener(new InspectionApplicationDetailController.OnRequestWorkFlowListener() {
            @Override
            public void requestWorkFlow(String mId, String mPendingId) {
                id = mId;
                pendingId = mPendingId;
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

//    @Override
//    public void getCurrentDeploymentSuccess(DeploymentEntity entity) {
//        if (null != entity){
//            getController(InspectionApplicationDetailController.class).startWorkFlow(entity.id,"TaskEvent_13rgkoh");
//        }
//    }
//
//    @Override
//    public void getCurrentDeploymentFailed(String errorMsg) {
//        ToastUtils.show(context,errorMsg);
//    }

    @Override
    public void getTableTypeByCodeSuccess(TableTypeIdEntity entity) {
        getController(InspectionApplicationDetailController.class).startWorkFlow(info.getDeploymentId(),"TaskEvent_13rgkoh");
        getController(InspectionApplicationDetailController.class).setTableType(entity);
    }

    @Override
    public void getTableTypeByCodeFailed(String errorMsg) {
        ToastUtils.show(context,errorMsg);
    }
}
