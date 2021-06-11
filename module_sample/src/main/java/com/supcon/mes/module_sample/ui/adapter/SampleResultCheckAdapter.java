package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SanpleResultCheckItemEntity;
import com.supcon.mes.module_sample.ui.SampleResultCheckActivity;

/**
 * @author : yaobing
 * @date : 2021/6/2 13:40
 */
public class SampleResultCheckAdapter extends BaseListDataRecyclerViewAdapter<SanpleResultCheckItemEntity> {
    public int clickPosition = -1;
    public SampleResultCheckAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SanpleResultCheckItemEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SanpleResultCheckItemEntity> {
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
        protected void update(SanpleResultCheckItemEntity data) {
            tvSample.setContent(((SanpleResultCheckItemEntity) data).getName());
            int order = getLayoutPosition() + 1;
            tvorderNumber.setText(context.getResources().getString(R.string.index) + order);
            tvCode.setContent(((SanpleResultCheckItemEntity) data).getCode());
            tvSeparationType.setContent(null == ((SanpleResultCheckItemEntity) data).getSampleType() ? "" : ((SanpleResultCheckItemEntity) data).getSampleType().getValue());
            tvBatchNumber.setContent(((SanpleResultCheckItemEntity) data).getBatchCode());
            tvCheckPoint.setContent(("null".equals(String.valueOf(((SanpleResultCheckItemEntity) data).getPsId().getName())) ? "" : String.valueOf(((SanpleResultCheckItemEntity) data).getPsId().getName())));
            tvMaterialCode.setContent(("null".equals(String.valueOf(((SanpleResultCheckItemEntity) data).getProductId().getCode())) ? "" : String.valueOf(((SanpleResultCheckItemEntity) data).getProductId().getCode())));
            tvMaterialName.setContent(("null".equals(String.valueOf(((SanpleResultCheckItemEntity) data).getProductId().getName())) ? "" : String.valueOf(((SanpleResultCheckItemEntity) data).getProductId().getName())));
            tvQualityStandard.setContent(null == ((SanpleResultCheckItemEntity) data).getStdVerId() ? "" : ((SanpleResultCheckItemEntity) data).getStdVerId().getName());
            tvSampleType.setContent(null == ((SanpleResultCheckItemEntity) data).getSampleType() ? "" : ((SanpleResultCheckItemEntity) data).getSampleType().getValue());
            tvSampleStatus.setContent(null == ((SanpleResultCheckItemEntity) data).getSampleState() ? "" : ((SanpleResultCheckItemEntity) data).getSampleState().getValue());
            tvRegistrationTime.setContent(((SanpleResultCheckItemEntity) data).getRegisterTime());

            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                clickPosition = getLayoutPosition();
                bundle.putLong(Constant.IntentKey.LIMS_SAMPLE_ID, ((SanpleResultCheckItemEntity) data).getId());
                IntentRouter.go(context, Constant.AppCode.LIMS_SampleResultCheckProject, bundle);
            });
        }
    }

}
