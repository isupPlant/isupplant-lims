package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SanpleResultCheckItemEntity;

import utilcode.util.ToastUtils;

/**
 * @author : yaobing
 * @date : 2021/6/2 13:40
 * @desc :
 */
public class SampleResultCheckAdapter extends BaseListDataRecyclerViewAdapter<SanpleResultCheckItemEntity> {
    public SampleResultCheckAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindByTag("tvOrderNumber")
        TextView tvorderNumber;
        @BindByTag("tvSeparationType")
        CustomTextView tvSeparationType;
        @BindByTag("tvSample")
        CustomTextView tvSample;
        @BindByTag("tvCode")
        CustomTextView tvCode;
        @BindByTag("tvBatchNumber")
        CustomTextView tvBatchNumber;
        @BindByTag("tvCheckPoint")
        CustomTextView tvCheckPoint;
        @BindByTag("tvMaterialCode")
        CustomTextView tvMaterialCode;
        @BindByTag("tvMaterialName")
        CustomTextView tvMaterialName;
        @BindByTag("tvQualityStandard")
        CustomTextView tvQualityStandard;
        @BindByTag("tvSampleType")
        CustomTextView tvSampleType;
        @BindByTag("tvSampleStatus")
        CustomTextView tvSampleStatus;
        @BindByTag("tvRegistrationTime")
        CustomTextView tvRegistrationTime;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_result_check;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(Object data) {
            SanpleResultCheckItemEntity entity = (SanpleResultCheckItemEntity) data;
            tvSample.setContent(entity.getName());
            int order = getLayoutPosition()+1;
            tvorderNumber.setText(context.getResources().getString(R.string.index) + order);
            tvCode.setContent(entity.getCode());
            tvSeparationType.setContent(entity.getSampleType().getValue());
            tvBatchNumber.setContent(entity.getBatchCode());
            tvCheckPoint.setContent(("null" == String.valueOf(entity.getPsId().getName())? "" : String.valueOf(entity.getPsId().getName())));
            tvMaterialCode.setContent(("null" == String.valueOf(entity.getProductId().getCode())? "" : String.valueOf(entity.getProductId().getCode())));
            tvMaterialName.setContent(("null" == String.valueOf(entity.getProductId().getName())? "" : String.valueOf(entity.getProductId().getName())));
            tvQualityStandard.setContent(entity.getStdVerId().getName());
            tvSampleType.setContent(entity.getSampleType().getValue());
            tvSampleStatus.setContent(entity.getSampleState().getValue());
            tvRegistrationTime.setContent(entity.getRegisterTime());

            itemView.setOnClickListener(v -> {
                ToastUtils.showLong("pos" + getLayoutPosition() + entity.toString());

            });
        }
    }

}
