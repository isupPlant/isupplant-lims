package com.supcon.mes.module_sample.ui.account;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleAccountEntity;
import com.supcon.mes.module_sample.model.contract.SampleAccountContract;
import com.supcon.mes.module_sample.presenter.SampleAccountPresenter;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleAccountEntity;
import com.supcon.mes.module_sample.model.contract.SampleAccountContract;
import com.supcon.mes.module_sample.presenter.SampleAccountPresenter;


/**
 * author huodongsheng
 * on 2020/10/15
 * class name
 */

@Presenter(value = {SampleAccountPresenter.class})
public class SampleAccountActivity extends BaseRefreshRecyclerActivity<SampleAccountEntity> implements SampleAccountContract.View {
    @Override
    protected IListAdapter<SampleAccountEntity> createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_account;
    }

    @Override
    public void getSampleAccountListSuccess(CommonListEntity entity) {

    }

    @Override
    public void getSampleAccountListFailed(String errorMsg) {

    }
}
