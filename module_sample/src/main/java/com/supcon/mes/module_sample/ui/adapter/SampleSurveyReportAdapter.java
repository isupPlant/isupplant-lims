package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_sample.R;

/**
 * author huodongsheng
 * on 2020/7/9
 * class name
 */
public class SampleSurveyReportAdapter extends BaseListDataRecyclerViewAdapter<SurveyReportEntity> {
    public SampleSurveyReportAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SurveyReportEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SurveyReportEntity>{

        @BindByTag("tvOddNumbers")
        TextView tvOddNumbers;
        @BindByTag("tvEdit")
        TextView tvEdit;
        @BindByTag("tvSample")
        CustomTextView tvSample;
        @BindByTag("tvMateriel")
        CustomTextView tvMateriel;
        @BindByTag("tvBatchNumber")
        CustomTextView tvBatchNumber;
        @BindByTag("tvRegistrationTime")
        CustomTextView tvRegistrationTime;
        @BindByTag("tvCreator")
        CustomTextView tvCreator;
        @BindByTag("tvSamplingPoint")
        CustomTextView tvSamplingPoint;
        @BindByTag("tvTestConclusion")
        CustomTextView tvTestConclusion;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_survey_report;
        }

        @Override
        protected void update(SurveyReportEntity data) {
            //单据编号
            tvOddNumbers.setText(StringUtil.isEmpty(data.getTableNo()) ? "--" : data.getTableNo());

            //样品名称 && 批号 && 登记时间
            if (null == data.getSampleId()){
                tvSample.setContent("--");
                tvBatchNumber.setContent("--");
            }else {
                tvSample.setContent(StringUtil.isEmpty(data.getSampleId().getName()) ? "--" : data.getSampleId().getName());
                tvBatchNumber.setContent(StringUtil.isEmpty(data.getSampleId().getBatchCode()) ? "--" : data.getSampleId().getBatchCode());
                tvRegistrationTime.setContent(null == data.getSampleId().getRegisterTime() ? "--" :
                        DateUtil.dateFormat(data.getSampleId().getRegisterTime(),"yyyy-MM-dd HH:mm:ss"));

                //物料名称
                if (null == data.getSampleId().getProductId()){
                    tvMateriel.setContent("--");
                }else {
                    tvMateriel.setContent(StringUtil.isEmpty(data.getSampleId().getProductId().getName()) ? "--" : data.getSampleId().getProductId().getName());
                }

                //采样点
                if (null == data.getSampleId().getPsId()){
                    tvSamplingPoint.setContent("--");
                }else {
                    tvSamplingPoint.setContent(StringUtil.isEmpty(data.getSampleId().getPsId().getName()) ? "--" : data.getSampleId().getPsId().getName());
                }
            }

            //制单人
            if (null == data.getCreateStaff()){
                tvCreator.setContent("--");
            }else {
                tvCreator.setContent(StringUtil.isEmpty(data.getCreateStaff().getName()) ? "--" : data.getCreateStaff().getName());
            }


            //检验结论
            tvTestConclusion.setContent(StringUtil.isEmpty(data.getTestResult()) ? "--" : data.getTestResult());

            //单据状态
            if (null == data.getPending()){
                tvEdit.setText("--");
            }else {
                tvEdit.setText(StringUtil.isEmpty(data.getPending().taskDescription) ? "--" : data.getPending().taskDescription);
            }
            if (tvEdit.getText().equals("编辑")){
                tvEdit.setTextColor(Color.parseColor("#ff1e82d2"));
            }else {
                tvEdit.setTextColor(Color.parseColor("#03DAC5"));
            }


        }
    }
}
