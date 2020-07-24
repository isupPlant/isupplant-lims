package com.supcon.mes.module_product.ui.inspection;

import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.controller.InspectionApplicationDetailController;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationDetailApi;
import com.supcon.mes.module_lims.presenter.InspectionApplicationDetailPresenter;
import com.supcon.mes.module_product.R;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name 产品检验申请详情
 */
@Router(value = Constant.AppCode.LIMS_ProductApplicationInspectionDetail)
@Presenter(value = {InspectionApplicationDetailPresenter.class})
@Controller(value = {InspectionApplicationDetailController.class})
public class ProductInspectionApplicationDetailActivity extends BaseRefreshActivity implements InspectionApplicationDetailApi.View{
    private String id;
    private String pendingId;

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
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_product_inspection_application));
        ctSupplier.setVisibility(View.GONE);

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
                presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionApplicationDetailApi.class).getInspectionDetailHeaderData(id,pendingId);
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
        getController(InspectionApplicationDetailController.class).setHeardData(1,entity, new InspectionApplicationDetailController.OnRequestPtListener() {
            @Override
            public void requestPtClick(boolean isEdit) {
                //请求pt
                presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionApplicationDetailApi.class).getInspectionDetailPtData(BusinessType.PleaseCheck.PRODUCT_PLEASE_CHECK,isEdit,id);
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

}
