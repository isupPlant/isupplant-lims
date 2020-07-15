package com.supcon.mes.module_product.ui.inspection;

import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.controller.InspectionApplicationDetailController;
import com.supcon.mes.module_product.R;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name 产品检验申请详情
 */
@Router(value = Constant.AppCode.LIMS_ProductApplicationInspectionDetail)
@Controller(value = {InspectionApplicationDetailController.class})
public class ProductInspectionApplicationDetailActivity extends BaseControllerActivity {
    private String id;
    private String pendingId;

    @BindByTag("titleText")
    TextView titleText;
    @Override
    protected int getLayoutID() {
        return com.supcon.mes.module_lims.R.layout.activity_inspection_application_detail;
    }

    @Override
    protected void onInit() {
        super.onInit();

        id =  getIntent().getStringExtra("id");
        pendingId = getIntent().getStringExtra("pendingId");

        getController(InspectionApplicationDetailController.class).setId(id);
        getController(InspectionApplicationDetailController.class).setPendingId(pendingId);
        getController(InspectionApplicationDetailController.class).setType(BusinessType.PleaseCheck.PRODUCT_PLEASE_CHECK);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_product_inspection_application));


        //获取业务类型参照的数据
        getController(InspectionApplicationDetailController.class).requestBusinessTypeData();
    }
}
