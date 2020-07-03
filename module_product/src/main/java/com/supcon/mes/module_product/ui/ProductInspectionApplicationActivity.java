package com.supcon.mes.module_product.ui;

import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationApi;
import com.supcon.mes.module_lims.presenter.InspectionApplicationPresenter;
import com.supcon.mes.module_product.R;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 产品检验申请列表
 */

@Router(Constant.AppCode.LIMS_ProductApplicationInspection)
@Presenter(value = {InspectionApplicationPresenter.class})

public class ProductInspectionApplicationActivity extends BaseRefreshRecyclerActivity<InspectionApplicationEntity> implements InspectionApplicationApi.View {


    @Override
    protected int getLayoutID() {
        return R.layout.activity_product_inspection_application;
    }

    @Override
    protected IListAdapter createAdapter() {
        return null;
    }

    @Override
    public void getInspectionApplicationListSuccess(InspectionApplicationListEntity entity) {

    }

    @Override
    public void getInspectionApplicationListFailed(String errorMsg) {

    }
}
