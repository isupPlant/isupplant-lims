package com.supcon.mes.module_incoming.ui;

import android.content.Intent;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_incoming.R;
import com.supcon.mes.module_lims.controller.SurverReportDetailController;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
@Controller(value = {
        SurverReportDetailController.class
})
@Router(value = Constant.Router.PURCH_INSPREPORT_VIEW,viewCode = "purchInspReportEdit")
public class PurchSurveyReportDetailActivity extends BaseRefreshActivity {
    @BindByTag("titleText")
    TextView titleText;
    PendingEntity pendingEntity;
    @Override
    protected int getLayoutID() {
        return R.layout.ac_inpect_resport_detail;
    }

    SurveyReportEntity resportEntity;
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
        titleText.setText("来料检验报告单");
        getController(SurverReportDetailController.class).setRefreshController(this,refreshController);
    }

    @Override
    protected void initData() {
        super.initData();
        if (pendingEntity!=null){
            getController(SurverReportDetailController.class).setReportPending(2,pendingEntity);
        }else {
            getController(SurverReportDetailController.class).setReportHead(2,resportEntity);
        }
    }
}
