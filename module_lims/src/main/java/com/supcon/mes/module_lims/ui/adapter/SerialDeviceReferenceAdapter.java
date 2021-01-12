package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;

/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
public class SerialDeviceReferenceAdapter extends BaseListDataRecyclerViewAdapter<SerialDeviceEntity> {
    public SerialDeviceReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SerialDeviceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SerialDeviceEntity> {

        @BindByTag("tvDeviceName")
        CustomTextView tvDeviceName;
        @BindByTag("tvDeviceCode")
        CustomTextView tvDeviceCode;
        @BindByTag("tvDeviceType")
        CustomTextView tvDeviceType;
        @BindByTag("tvAcquisitionMode")
        CustomTextView tvAcquisitionMode;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("ll_item")
        LinearLayout ll_item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_serial_device_ref;
        }

        @Override
        protected void initListener() {
            super.initListener();
            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(v, 0);
                }
            });


        }

        @Override
        protected void update(SerialDeviceEntity data) {
            if (null != data) {
                tvDeviceName.setContent(data.getName());
                tvDeviceCode.setContent(data.getCode());
                tvDeviceType.setContent(data.getEamType().getName());
                tvAcquisitionMode.setContent(data.getSerialType().getValue());

                if (data.getSerialType().getId() == null ||  data.getSerialType().getId().equals("BaseSet_serialType/direct")){
                    iv_select.setVisibility(View.GONE);
                }else {
                    iv_select.setVisibility(View.VISIBLE);
                }

                if (data.isSelect()) {
                    iv_select.setImageResource(R.drawable.ic_check_yes);
                } else {
                    iv_select.setImageResource(R.drawable.ic_check_no);
                }

            }
        }
    }
}
