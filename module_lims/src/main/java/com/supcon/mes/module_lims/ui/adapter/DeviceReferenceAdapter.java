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
import com.supcon.mes.module_lims.model.bean.DeviceReferenceEntity;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
public class DeviceReferenceAdapter extends BaseListDataRecyclerViewAdapter<DeviceReferenceEntity> {
    public DeviceReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<DeviceReferenceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<DeviceReferenceEntity>{


        @BindByTag("tvName")
        CustomTextView tvName;
        @BindByTag("tvDeviceState")
        CustomTextView tvDeviceState;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_device_reference;
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
        protected void update(DeviceReferenceEntity data) {
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

                tvDeviceState.setContent(data.getState() == null ? "--" : StringUtil.isEmpty(data.getState().getValue()) ? "--" : data.getState().getValue());

            }else {
                tvName.setContent("--");
                tvDeviceState.setContent("--");
            }

        }
    }
}
