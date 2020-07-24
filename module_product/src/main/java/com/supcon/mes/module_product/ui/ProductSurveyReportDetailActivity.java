package com.supcon.mes.module_product.ui;

import android.content.Intent;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_lims.controller.SurverReportDetailController;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_product.R;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
@Controller(value = {
        SurverReportDetailController.class
})
@Router(Constant.Router.PRODUCT_INSPREPORT_VIEW)
public class ProductSurveyReportDetailActivity extends BaseRefreshActivity {
    @BindByTag("titleText")
    TextView titleText;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_inpect_resport_detail;
    }

    SurveyReportEntity resportEntity;
    PendingEntity pendingEntity;
    @Override
    protected void onInit() {
        super.onInit();
        Intent intent=getIntent();
        resportEntity= (SurveyReportEntity) intent.getSerializableExtra("resportEntity");
        pendingEntity = (PendingEntity) intent.getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("产品检验报告单");
        getController(SurverReportDetailController.class).setRefreshController(this,refreshController);
    }

    @Override
    protected void initData() {
        super.initData();
        if (pendingEntity!=null){
            getController(SurverReportDetailController.class).setReportPending(1,pendingEntity);
        }else {
            getController(SurverReportDetailController.class).setReportHead(1,resportEntity);
        }
    }
}
