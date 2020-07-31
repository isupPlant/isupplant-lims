package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class ProjectAdapter extends BaseListDataRecyclerViewAdapter<InspectionSubEntity> {

    public ProjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionSubEntity> getViewHolder(int viewType) {
        return null;
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionSubEntity>{

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return 0;
        }

        @Override
        protected void update(InspectionSubEntity data) {

        }
    }
}
