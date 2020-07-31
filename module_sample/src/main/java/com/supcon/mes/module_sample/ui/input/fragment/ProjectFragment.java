package com.supcon.mes.module_sample.ui.input.fragment;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectApi;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectPresenter;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {InspectionSubProjectPresenter.class})
public class ProjectFragment extends BaseRefreshRecyclerFragment<InspectionSubEntity> implements InspectionSubProjectApi.View {
    @Override
    protected IListAdapter<InspectionSubEntity> createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_project;
    }


    @Override
    public void getInspectionSubProjectListSuccess(CommonListEntity entity) {

    }

    @Override
    public void getInspectionSubProjectListFailed(String errorMsg) {

    }
}
