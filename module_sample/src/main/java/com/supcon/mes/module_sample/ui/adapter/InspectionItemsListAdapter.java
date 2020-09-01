package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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

            if (null != data.getTestState()){
                if (data.getTestState().getValue().equals("待检验")){
                    tvState.setTextColor(Color.parseColor("#1F82D2"));
                }else if (data.getTestState().getValue().equals("部分已检")){
                    tvState.setTextColor(Color.parseColor("#15B9B8"));
                } else if (data.getTestState().getValue().equals("已检验")){
                    tvState.setTextColor(Color.parseColor("#1A9D17"));
                }else if (data.getTestState().getValue().equals("待复核")){
                    tvState.setTextColor(Color.parseColor("#D2881F"));
                }else if (data.getTestState().getValue().equals("已复核")){
                    tvState.setTextColor(Color.parseColor("#3634A3"));
                }else if (data.getTestState().getValue().equals("已拒绝")){
                    tvState.setTextColor(Color.parseColor("#D25A1F"));
                }else if (data.getTestState().getValue().equals("已取消")){
                    tvState.setTextColor(Color.parseColor("#E15774"));
                }

            }


            if (data.isSelect()){
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_sel);
            }else {
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_nor);
            }
        }
    }
}
