package com.supcon.mes.module_sample.ui.input.fragment;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.mes.module_sample.R;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
public class InspectionSubItemFragment extends BaseRefreshRecyclerFragment<BaseEntity> {
    @Override
    protected IListAdapter<BaseEntity> createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_inspection_sub_item;
    }
}
