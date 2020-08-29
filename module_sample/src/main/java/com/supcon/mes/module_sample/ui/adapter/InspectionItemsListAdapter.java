package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;

/**
 * author huodongsheng
 * on 2020/7/30
 * class name
 */
public class InspectionItemsListAdapter extends BaseListDataRecyclerViewAdapter<InspectionItemsEntity> {
    public InspectionItemsListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionItemsEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionItemsEntity>{

        @BindByTag("tvInspectionItems")
        CustomTextView tvInspectionItems;
        @BindByTag("tvRepeatNumber")
        CustomTextView tvRepeatNumber;
        @BindByTag("ctVersionNumber")
        CustomTextView ctVersionNumber;
        @BindByTag("tvState")
        TextView tvState;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspection_item;
        }

        @Override
        protected void initView() {
            super.initView();
            tvInspectionItems.contentView().setSingleLine(true);
        }

        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(v -> onItemChildViewClick(v,0));
        }

        @Override
        protected void update(InspectionItemsEntity data) {
            //检验项目
            tvInspectionItems.setContent(null == data.getTestId() ? "--" :
                    StringUtil.isEmpty(data.getTestId().getName()) ? "--" : data.getTestId().getName());

            //重复号
            tvRepeatNumber.setContent(null == data.getParallelNo() ? "--" : data.getParallelNo()+"");

            //版本号
            ctVersionNumber.setContent(null == data.getTestId() ? "--" :
                    StringUtil.isEmpty(data.getTestId().getBusiVersion()) ? "--" : data.getTestId().getBusiVersion());

            //状态
            tvState.setText(null == data.getTestState() ? "--" :
                    StringUtil.isEmpty(data.getTestState().getValue()) ? "--" : data.getTestState().getValue());

            if (data.isSelect()){
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_sel);
            }else {
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_nor);
            }
        }
    }
}
