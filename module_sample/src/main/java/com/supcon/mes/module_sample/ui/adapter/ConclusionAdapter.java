package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.ConclusionEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/11
 * class name
 */
public class ConclusionAdapter extends BaseListDataRecyclerViewAdapter<ConclusionEntity> {
    public ConclusionAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ConclusionEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<ConclusionEntity>{

        @BindByTag("ivExpand")
        ImageView ivExpand;
        @BindByTag("rlRange")
        RelativeLayout rlRange;
        @BindByTag("rvRange")
        RecyclerView rvRange;
        @BindByTag("llRange")
        LinearLayout llRange;
        @BindByTag("tvConclusionColumnName")
        CustomVerticalSpinner tvConclusionColumnName;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            rvRange.setLayoutManager(new LinearLayoutManager(context));
            rvRange.setNestedScrollingEnabled(false);
            //rvRange.setAdapter();
        }

        @Override
        protected void initListener() {
            super.initListener();
            rlRange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llRange.getVisibility() == View.VISIBLE){
                        llRange.setVisibility(View.GONE);
                        ivExpand.setImageResource(R.drawable.ic_drop_down);
                    }else if (llRange.getVisibility() == View.GONE){
                        llRange.setVisibility(View.VISIBLE);
                        ivExpand.setImageResource(R.drawable.ic_drop_up);
                    }
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_conlusion;
        }

        @Override
        protected void update(ConclusionEntity data) {
            tvConclusionColumnName.setKey(data.getColumnName());
            //tvConclusionColumnName.set
        }
    }
}
