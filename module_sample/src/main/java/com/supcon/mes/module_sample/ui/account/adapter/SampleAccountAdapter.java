package com.supcon.mes.module_sample.ui.account.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleAccountEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/10/19
 * class name
 */
public class SampleAccountAdapter extends BaseListDataRecyclerViewAdapter<SampleAccountEntity> {
    public SampleAccountAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleAccountEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleAccountEntity> {

        @BindByTag("tvSample")
        TextView tvSample;
        @BindByTag("tvSampleState")
        TextView tvSampleState;
        @BindByTag("ctSamplingPoint")
        CustomTextView ctSamplingPoint;
        @BindByTag("ctBatchNumber")
        CustomTextView ctBatchNumber;
        @BindByTag("ctRegisterTime")
        CustomTextView ctRegisterTime;
        @BindByTag("ctBranchSampleType")
        CustomTextView ctBranchSampleType;
        @BindByTag("ctMateriel")
        CustomTextView ctMateriel;
        @BindByTag("ctSamplePurpose")
        CustomTextView ctSamplePurpose;
        @BindByTag("ctSampleType")
        CustomTextView ctSampleType;
        @BindByTag("ctQualityStandard")
        CustomTextView ctQualityStandard;
        @BindByTag("ctSampleRetentionState")
        CustomTextView ctSampleRetentionState;
        @BindByTag("llExpand")
        LinearLayout llExpand;
        @BindByTag("tvExpand")
        TextView tvExpand;
        @BindByTag("ivExpand")
        ImageView ivExpand;
        @BindByTag("rlExpand")
        RelativeLayout rlExpand;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_lims_sample_accountaa;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(rlExpand)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (llExpand.getVisibility() == View.GONE){
                                llExpand.setVisibility(View.VISIBLE);
                                tvExpand.setText(context.getResources().getString(R.string.lims_close));
                                ivExpand.setImageResource(R.drawable.ic_lims_close);
                            }else {
                                llExpand.setVisibility(View.GONE);
                                tvExpand.setText(context.getResources().getString(R.string.middleware_more));
                                ivExpand.setImageResource(R.drawable.ic_lims_open);
                            }
                            notifyItemChanged(getAdapterPosition());
                        }
                    });
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(SampleAccountEntity data) {
            if (null != data) {
                //样品
                if (!StringUtil.isEmpty(data.getName()) && !StringUtil.isEmpty(data.getCode())) {
                    tvSample.setText(data.getName() + "(" + data.getCode() + ")");
                } else {
                    if (StringUtil.isEmpty(data.getName()) && StringUtil.isEmpty(data.getCode())) {
                        tvSample.setText("--");
                    } else {
                        if (StringUtil.isEmpty(data.getName())) tvSample.setText(data.getCode());
                        else tvSample.setText(data.getName());
                    }
                }

                //样品状态
                tvSampleState.setText(data.getSampleState() == null ? "--" : StringUtil.isEmpty(data.getSampleState().getValue()) ? "--" : data.getSampleState().getValue());
                //采样点
                ctSamplingPoint.setContent(data.getPsId() == null ? "--" : StringUtil.isEmpty(data.getPsId().getName()) ? "--" : data.getPsId().getName());
                //批号
                ctBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());
                //登记时间
                ctRegisterTime.setContent(data.getRegisterTime() == null ? "--" : DateUtil.dateFormat(data.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
                //分样类型
                ctBranchSampleType.setContent(data.getSperaType() == null ? "--" : StringUtil.isEmpty(data.getSperaType().getValue()) ? "--" : data.getSperaType().getValue());
                //物料
                if (null != data.getProductId()){
                    if (!StringUtil.isEmpty(data.getProductId().getName()) && !StringUtil.isEmpty(data.getProductId().getCode())){
                        ctMateriel.setContent(data.getProductId().getName()+"("+data.getProductId().getCode()+")");
                    }else {
                        if (StringUtil.isEmpty(data.getProductId().getName()) && StringUtil.isEmpty(data.getProductId().getCode())){
                            ctMateriel.setContent("--");
                        }else {
                            if (StringUtil.isEmpty(data.getProductId().getName()))ctMateriel.setContent(data.getProductId().getCode());
                            else ctMateriel.setContent(data.getProductId().getName());
                        }
                    }
                }else {
                    ctMateriel.setContent("--");
                }
                //样品用途
                ctSamplePurpose.setContent(data.getUseMethod() == null ? "--" : StringUtil.isEmpty(data.getUseMethod().getValue()) ? "--" : data.getUseMethod().getValue());
                //样品类型
                ctSampleType.setContent(data.getSampleType() == null ? "--" : StringUtil.isEmpty(data.getSampleType().getValue()) ? "--" : data.getSampleType().getValue());
                //质量标准
                ctQualityStandard.setContent(data.getStdVerId() == null ? "--" : StringUtil.isEmpty(data.getStdVerId().getName()) ? "--" : data.getStdVerId().getName());
                //留样状态
                ctSampleRetentionState.setContent(data.getRetainState() == null ? "--" : StringUtil.isEmpty(data.getRetainState().getValue()) ? "--" : data.getRetainState().getValue());
            } else {
                tvSample.setText("--");
                tvSampleState.setText("--");
                ctSamplingPoint.setContent("--");
                ctBatchNumber.setContent("--");
                ctRegisterTime.setContent("--");
                ctBranchSampleType.setContent("--");
                ctMateriel.setContent("--");
                ctSamplePurpose.setContent("--");
                ctSampleType.setContent("--");
                ctQualityStandard.setContent("--");
                ctSampleRetentionState.setContent("--");
            }

        }
    }
}
