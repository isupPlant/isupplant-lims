package com.supcon.mes.module_other.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationApi;
import com.supcon.mes.module_other.R;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 其他检验申请列表
 */

@Router(Constant.AppCode.LIMS_OtherApplicationInspection)
public class OtherInspectionApplicationActivity extends BaseRefreshRecyclerActivity<InspectionApplicationEntity> implements InspectionApplicationApi.View {
    @Override
    protected IListAdapter createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_other_inspection_application;
    }

    @Override
    public void getInspectionApplicationListSuccess(InspectionApplicationListEntity entity) {

    }

    @Override
    public void getInspectionApplicationListFailed(String errorMsg) {

    }
}
