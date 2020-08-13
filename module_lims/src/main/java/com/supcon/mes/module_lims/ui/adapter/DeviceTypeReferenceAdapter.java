package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.DeviceTypeReferenceEntity;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
public class DeviceTypeReferenceAdapter extends BaseListDataRecyclerViewAdapter<DeviceTypeReferenceEntity> {
    public DeviceTypeReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<DeviceTypeReferenceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<DeviceTypeReferenceEntity>{

        @BindByTag("tvName")
        CustomTextView tvName;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_device_type_reference;
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
        protected void update(DeviceTypeReferenceEntity data) {
            if (null != data){
                if (!StringUtil.isEmpty(data.getName()) && !StringUtil.isEmpty(data.getCode())){
                    tvName.setContent(data.getName()+"(" + data.getCode() +")");
                }else {
                    if (StringUtil.isEmpty(data.getName()) && StringUtil.isEmpty(data.getCode())){
                        tvName.setContent("--");
                    }else {
                        if (StringUtil.isEmpty(data.getName())){
                            tvName.setContent(data.getCode());
                        }else {
                            tvName.setContent(data.getName());
                        }
                    }
                }
            }else {
                tvName.setContent("--");
            }
        }
    }
}
