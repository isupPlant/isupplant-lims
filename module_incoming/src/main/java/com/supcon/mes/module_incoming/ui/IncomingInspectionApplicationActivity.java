package com.supcon.mes.module_incoming.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_incoming.R;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationApi;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 来料检验申请列表
 */

@Router(Constant.AppCode.LIMS_IncomingApplicationInspection)
public class IncomingInspectionApplicationActivity extends BaseRefreshRecyclerActivity<InspectionApplicationEntity> implements InspectionApplicationApi.View {

    @Override
    protected IListAdapter createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_incoming_inspection_application;
    }

    @Override
    public void getInspectionApplicationListSuccess(InspectionApplicationListEntity entity) {

    }

    @Override
    public void getInspectionApplicationListFailed(String errorMsg) {

    }
}
