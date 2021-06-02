package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.module_sample.R;

/**
 * @author : yaobing
 * @date : 2021/6/2 13:40
 * @desc :
 */
public class SampleResultCheckAdapter extends BaseListDataRecyclerViewAdapter {
    public SampleResultCheckAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder {

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_result_check;
        }

        @Override
        protected void update(Object data) {

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}
