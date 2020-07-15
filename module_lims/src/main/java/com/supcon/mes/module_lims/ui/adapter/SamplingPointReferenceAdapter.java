package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.SamplingPointEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
public class SamplingPointReferenceAdapter extends BaseListDataRecyclerViewAdapter<SamplingPointEntity> {


    public SamplingPointReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SamplingPointEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SamplingPointEntity>{

        @BindByTag("tvName")
        TextView tvName;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sampling_point_reference;
        }

        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(item,0);
                }
            });
        }

        @Override
        protected void update(SamplingPointEntity data) {
            tvName.setText(StringUtil.isEmpty(data.getName()) ? "--" : data.getName());
        }
    }
}