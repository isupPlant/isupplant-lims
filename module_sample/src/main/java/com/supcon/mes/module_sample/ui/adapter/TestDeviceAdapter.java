package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class TestDeviceAdapter extends BaseListDataRecyclerViewAdapter<TestDeviceEntity> {
    public TestDeviceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<TestDeviceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<TestDeviceEntity>{

        @BindByTag("ctDeviceType")
        CustomTextView ctDeviceType;
        @BindByTag("ctDeviceCode")
        CustomTextView ctDeviceCode;
        @BindByTag("ctDeviceName")
        CustomTextView ctDeviceName;
        @BindByTag("ctDeviceState")
        CustomTextView ctDeviceState;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            item.setOnClickListener(v -> onItemChildViewClick(v,0));

            //设备类型
            ctDeviceType.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    if (action == -1){
                        onItemChildViewClick(ctDeviceType, -1);
                    }else {
                        onItemChildViewClick(ctDeviceType, 1);
                    }

                }
            });


            //设备编码
            ctDeviceCode.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    if (action == -1){
                        onItemChildViewClick(ctDeviceCode, -2);
                    }else {
                        onItemChildViewClick(ctDeviceCode, 2);
                    }
                }
            });

        }

        @Override
        protected int layoutId() {
            return R.layout.item_test_device;
        }

        @Override
        protected void update(TestDeviceEntity data) {
            if (data != null){
                ctDeviceType.setContent(data.getEamTypeId() == null ? "" : StringUtil.isEmpty(data.getEamTypeId().getName()) ? "" : data.getEamTypeId().getName());
                ctDeviceCode.setContent(data.getEamId() == null ? "" : StringUtil.isEmpty(data.getEamId().getCode()) ? "" : data.getEamId().getCode());
                ctDeviceName.setContent(data.getEamId() == null ? "" : StringUtil.isEmpty(data.getEamId().getName()) ? "" : data.getEamId().getName());
                ctDeviceState.setContent(data.getEamId() == null ? "" : data.getEamId().getState() == null ? "" : StringUtil.isEmpty(data.getEamId().getState().getValue()) ? "" : data.getEamId().getState().getValue());

                if (StringUtil.isEmpty(ctDeviceCode.getContent())){
                    ctDeviceCode.setKeyColor(Color.parseColor("#B20404"));
                }else {
                    ctDeviceCode.setKeyColor(Color.parseColor("#666666"));
                    ctDeviceCode.setContentTextColor(Color.parseColor("#333333"));
                }


                if (data.isSelect()){
                    item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_sel);
                }else {
                    item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_nor);
                }
            }

        }
    }
}
