package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.InspectionItemsEntity;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
public class InspectionItemAdapter extends BaseListDataRecyclerViewAdapter<InspectionItemsEntity> {
    private boolean isEdit = false;
    public InspectionItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionItemsEntity> getViewHolder(int viewType) {
        return new ViewHolder(context,parent);
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionItemsEntity>{

        @BindByTag("tvInspectionItems")
        CustomTextView tvInspectionItems;
        @BindByTag("tvUnit")
        CustomTextView tvUnit;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_inspection_items;
        }

        @Override
        protected void initListener() {
            super.initListener();
            if (isEdit){
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemChildViewClick(v,0);
                    }
                });
            }
        }

        @Override
        protected void update(InspectionItemsEntity data) {
            if (null != data.getStdVerComId()){
                //检验项目
                if (null != data.getStdVerComId().getComId()){
                    tvInspectionItems.setContent(StringUtil.isEmpty(data.getStdVerComId().getComId().getName()) ? "--" : data.getStdVerComId().getComId().getName());
                }else {
                    tvInspectionItems.setContent("--");
                }

                //计量单位
                tvUnit.setContent(StringUtil.isEmpty(data.getStdVerComId().getUnitName()) ? "--" : data.getStdVerComId().getUnitName());

            }else {
                tvInspectionItems.setContent("--");
                tvUnit.setContent("--");
            }

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }

            if (isEdit){
                iv_select.setVisibility(View.VISIBLE);
            }else {
                iv_select.setVisibility(View.GONE);
            }

        }
    }
}
