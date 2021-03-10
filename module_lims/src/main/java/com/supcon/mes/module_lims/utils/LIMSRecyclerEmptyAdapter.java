package com.supcon.mes.module_lims.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.adapter.RecyclerEmptyAdapter;
import com.supcon.mes.mbap.beans.EmptyViewEntity;
import com.supcon.mes.mbap.view.CustomEmptyView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.util.ScreenUtil;
import com.supcon.mes.module_lims.R;

import java.util.List;

/**
 * author huodongsheng
 * on 2021/3/9
 * class name
 */
public class LIMSRecyclerEmptyAdapter extends BaseListDataRecyclerViewAdapter<EmptyViewEntity> {
    public LIMSRecyclerEmptyAdapter(Context context) {
        super(context);
    }

    public LIMSRecyclerEmptyAdapter(Context context, List<EmptyViewEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<EmptyViewEntity> getViewHolder(int viewType) {
        return new EmptyViewHolder(context);
    }

    private class EmptyViewHolder extends BaseRecyclerViewHolder<EmptyViewEntity> {

        protected TextView emptyContent;

        public EmptyViewHolder(Context context) {
            super(context, parent);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_lims_empty;
        }

        @Override
        protected void initView() {
            super.initView();
            emptyContent = itemView.findViewById(R.id.emptyContent);

            int screenWidth = ScreenUtil.getScreenWidth(context);
            int screenHeight = ScreenUtil.getScreenHeight(context);
            ViewGroup.LayoutParams linearParams = itemView.getLayoutParams();
            linearParams.width = screenWidth;
            linearParams.height = screenHeight;
            itemView.setLayoutParams(linearParams);
        }

        @Override
        protected void initListener() {
            super.initListener();
//            customEmptyView.setOnChildViewClickListener(new OnChildViewClickListener() {
//                @Override
//                public void onChildViewClick(View childView, int action, Object obj) {
//                    onItemChildViewClick(childView,action,obj);
//                }
//            });
        }

        @Override
        protected void update(EmptyViewEntity data) {
            emptyContent.setText(data.contentText);
        }
    }
}
