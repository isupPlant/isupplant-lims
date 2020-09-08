package com.supcon.mes.module_other.ui;

import android.content.Intent;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_lims.controller.SurverReportDetailController;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_other.R;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
@Controller(value = {
        SurverReportDetailController.class
})
@Router(value = Constant.Router.OTHER_INSPREPORT_VIEW,viewCode = "otherInspReportEdit")
public class OtherSurveyReportDetailActivity extends BaseRefreshActivity {
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
        titleText.setText(getString(R.string.lims_other_inspection_report));
        getController(SurverReportDetailController.class).setRefreshController(this,refreshController);
    }

    @Override
    protected void initData() {
        super.initData();
        if (pendingEntity!=null){
            getController(SurverReportDetailController.class).setReportPending(3,pendingEntity);
        }else {
            getController(SurverReportDetailController.class).setReportHead(3,resportEntity);
        }
    }
}
