package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.SupplierReferenceEntity;
import com.supcon.mes.module_lims.model.bean.VendorIdEntity;

/**
 * author huodongsheng
 * on 2020/7/20
 * class name
 */
public class SupplierReferenceAdapter extends BaseListDataRecyclerViewAdapter<VendorIdEntity> {
    public SupplierReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<VendorIdEntity> getViewHolder(int viewType) {
        return new ViewHolder(context,parent);
    }

    class ViewHolder extends BaseRecyclerViewHolder<VendorIdEntity>{

        @BindByTag("tvName")
        CustomTextView tvName;
        @BindByTag("tvCode")
        CustomTextView tvCode;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_supplier_reference;
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
        protected void update(VendorIdEntity data) {
            tvName.setContent(StringUtil.isEmpty(data.getName()) ? "--" : data.getName());
            tvCode.setContent(StringUtil.isEmpty(data.getCode()) ? "--" : data.getCode());
        }
    }
}
