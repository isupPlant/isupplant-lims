package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.middleware.util.UrlUtil;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;

import java.util.concurrent.TimeUnit;

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
        @BindByTag("ivIsQualified")
        ImageView ivIsQualified;
        @BindByTag("checkResultTv")
        CustomTextView checkResultTv;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_survey_report;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(1000, TimeUnit.MICROSECONDS)
                    .subscribe(o -> {
                        Bundle bundle=new Bundle();
                        SurveyReportEntity entity=getItem(getAdapterPosition());
                        bundle.putSerializable("reportEntity",entity);
                        IntentRouter.go(context, Constant.Router.SAMPLE_REPORT_VIEW, bundle);
                    });

        }

        @Override
        protected void update(SurveyReportEntity data) {
            //单据编号
            tvOddNumbers.setText(StringUtil.isEmpty(data.getTableNo()) ? "--" : data.getTableNo());

            //检验结论
//            if (StringUtil.isEmpty(data.getTestResult())){
//                ivIsQualified.setVisibility(View.GONE);
//            }else {
//                ivIsQualified.setVisibility(View.VISIBLE);
//                if (data.getTestResult().equals("合格")){
//                    ivIsQualified.setImageResource(R.drawable.ic_qualified);
//                }else if (data.getTestResult().equals("不合格")){
//                    ivIsQualified.setImageResource(R.drawable.ic_un_qualified);
//                }else {
//                    ivIsQualified.setVisibility(View.GONE);
//                }
//            }
            if (!TextUtils.isEmpty(data.getTestResult()) && data.getTestResult().contains(context.getResources().getString(com.supcon.mes.module_lims.R.string.lims_unqualified))){
                checkResultTv.setValueColor(context.getResources().getColor(R.color.warningRed));
            }else {
                checkResultTv.setValueColor(context.getResources().getColor(R.color.lightGreen));
            }
            checkResultTv.setValue(data.getTestResult());
            //样品名称 && 批号 && 登记时间
            if (null == data.getSampleId()){
                tvSample.setContent("--");
                tvBatchNumber.setContent("--");
            }else {
                tvSample.setContent(String.format("%s(%S)",StringUtil.isEmpty(data.getSampleId().getName()) ? "--" : data.getSampleId().getName(),StringUtil.isEmpty(data.getSampleId().getName()) ? "--" : data.getSampleId().getCode()));
                tvBatchNumber.setContent(StringUtil.isEmpty(data.getSampleId().getBatchCode()) ? "--" : data.getSampleId().getBatchCode());
                tvRegistrationTime.setContent(null == data.getSampleId().getRegisterTime() ? "--" :
                        DateUtil.dateFormat(data.getSampleId().getRegisterTime(),"yyyy-MM-dd HH:mm:ss"));

                //物料名称
                if (null == data.getSampleId().getProductId()){
                    tvMateriel.setContent("--");
                }else {
                    tvMateriel.setContent(String.format("%s(%s)",StringUtil.isEmpty(data.getSampleId().getProductId().getName()) ? "--" : data.getSampleId().getProductId().getName(),StringUtil.isEmpty(data.getSampleId().getProductId().getName()) ? "--" : data.getSampleId().getProductId().getCode()));
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

            //单据状态
            if (null == data.getPending()){
                tvEdit.setText("--");
            }else {
                tvEdit.setText(StringUtil.isEmpty(data.getPending().taskDescription) ? "--" : data.getPending().taskDescription);
            }
            if (null != data.getPending() && null != data.getPending().openUrl){
                tvEdit.setTextColor(context.getResources().getColor(R.color.status_green));
            }else {
                tvEdit.setTextColor(context.getResources().getColor(R.color.status_blue));
            }


        }
    }
}
