package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleExamineEntity;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;

import java.util.ArrayList;

/**
 * @author : yaobing
 * @date : 2021/6/2 13:40
 */
public class SampleExamineAdapter extends BaseListDataRecyclerViewAdapter<SampleExamineEntity> {

    public ArrayList<Long> id_selected = new ArrayList<>();
    public ArrayList<SampleExamineEntity> selected_data = new ArrayList<>();

    public int clickPosition = -1;
    public SampleExamineAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleExamineEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleExamineEntity> {
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
        @BindByTag("cb_select")
        CheckBox cb_select;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_result_check_multi;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(SampleExamineEntity data) {
            tvSample.setContent(((SampleExamineEntity) data).getName());
            int order = getLayoutPosition() + 1;
            tvorderNumber.setText(context.getResources().getString(R.string.index) + order);
            tvCode.setContent(((SampleExamineEntity) data).getCode());
            tvSeparationType.setContent(null == ((SampleExamineEntity) data).getSampleType() ? "" : ((SampleExamineEntity) data).getSampleType().getValue());
            tvBatchNumber.setContent(((SampleExamineEntity) data).getBatchCode());
            tvCheckPoint.setContent(("null".equals(String.valueOf(((SampleExamineEntity) data).getPsId().getName())) ? "" : String.valueOf(((SampleExamineEntity) data).getPsId().getName())));
            tvMaterialCode.setContent(("null".equals(String.valueOf(((SampleExamineEntity) data).getProductId().getCode())) ? "" : String.valueOf(((SampleExamineEntity) data).getProductId().getCode())));
            tvMaterialName.setContent(("null".equals(String.valueOf(((SampleExamineEntity) data).getProductId().getName())) ? "" : String.valueOf(((SampleExamineEntity) data).getProductId().getName())));
            tvQualityStandard.setContent(null == ((SampleExamineEntity) data).getStdVerId() ? "" : ((SampleExamineEntity) data).getStdVerId().getName());
            tvSampleType.setContent(null == ((SampleExamineEntity) data).getSampleType() ? "" : ((SampleExamineEntity) data).getSampleType().getValue());
            tvSampleStatus.setContent(null == ((SampleExamineEntity) data).getSampleState() ? "" : ((SampleExamineEntity) data).getSampleState().getValue());
            tvRegistrationTime.setContent(((SampleExamineEntity) data).getRegisterTime());

            cb_select.setChecked(data.isSelect());
            if (data.isSelect()) {
                id_selected.add(data.getId());
                itemView.setBackground(context.getResources().getDrawable(R.drawable.shape_line_blue));
            }else {
                id_selected.remove(data.getId());
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {
                        id_selected.add(data.getId());
                        selected_data.add(data);
                        itemView.setBackground(context.getResources().getDrawable(R.drawable.shape_line_blue));
                    }else {
                        id_selected.remove(data.getId());
                        selected_data.remove(data);
                        itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                    list.get(getLayoutPosition()).setSelect(isChecked);
//                    notifyDataSetChanged();

                }
            });
            itemView.setOnClickListener(v -> {
//                Bundle bundle = new Bundle();
//                clickPosition = getLayoutPosition();
//                bundle.putLong(Constant.IntentKey.LIMS_SAMPLE_ID, ((SampleExamineEntity) data).getId());
//                IntentRouter.go(context, Constant.AppCode.LIMS_SampleResultCheckProject, bundle);
            });
        }
    }

}
