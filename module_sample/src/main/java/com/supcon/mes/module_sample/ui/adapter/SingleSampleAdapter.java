package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleEntity;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SingleSampleAdapter extends BaseListDataRecyclerViewAdapter<SampleEntity> {
    public SingleSampleAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleEntity>{

        @BindByTag("item")
        LinearLayout item;
        @BindByTag("ctSample")
        CustomTextView ctSample;
        @BindByTag("ctMateriel")
        CustomTextView ctMateriel;
        @BindByTag("ctBatchNumber")
        CustomTextView ctBatchNumber;
        @BindByTag("ctSampleSeparationType")
        CustomTextView ctSampleSeparationType;
        @BindByTag("ctSamplingPoint")
        CustomTextView ctSamplingPoint;
        @BindByTag("ctSampleType")
        CustomTextView ctSampleType;
        @BindByTag("ctRegisterTime")
        CustomTextView ctRegisterTime;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_single_sample;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(2000, TimeUnit.MICROSECONDS)
                    .subscribe(o->{
                        SampleEntity sampleEntity=getItem(getAdapterPosition());
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("sampleEntity",sampleEntity);
                        IntentRouter.go(context, Constant.Router.SINGLE_SAMPLE_RESULT_INPUT_ITEM,bundle);
                    });
        }

        @Override
        protected void update(SampleEntity data) {
            //样品名称 & 样品编码
            if (!StringUtil.isEmpty(data.getName()) && !StringUtil.isEmpty(data.getCode())){
                ctSample.setContent(data.getName()+"("+data.getCode()+")");
            }else {
                if (StringUtil.isEmpty(data.getName()) && StringUtil.isEmpty(data.getCode())){
                    ctSample.setContent("--");
                }else {
                    if (StringUtil.isEmpty(data.getName())){
                        ctSample.setContent(data.getCode());
                    }else {
                        ctSample.setContent(data.getName());
                    }
                }
            }

            //物料名称 & 物料编码
            if (null != data.getProductId()){
                if (!StringUtil.isEmpty(data.getProductId().getName()) && !StringUtil.isEmpty(data.getProductId().getCode())){
                    ctMateriel.setContent(data.getName()+"("+data.getProductId().getCode()+")");
                }else {
                    if (StringUtil.isEmpty(data.getProductId().getName()) && StringUtil.isEmpty(data.getProductId().getCode())){
                        ctMateriel.setContent("--");
                    }else {
                        if (StringUtil.isEmpty(data.getProductId().getName())){
                            ctMateriel.setContent(data.getProductId().getCode());
                        }else {
                            ctMateriel.setContent(data.getProductId().getName());
                        }
                    }
                }
            }else {
                ctMateriel.setContent("--");
            }

            //批号
            ctBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            //分样类型
            ctSampleSeparationType.setContent(null == data.getSperaType() ? "--" :
                    StringUtil.isEmpty(data.getSperaType().getValue()) ? "--" : data.getSperaType().getValue());

            //采样点
            ctSamplingPoint.setContent(null == data.getPsId() ? "--" :
                    StringUtil.isEmpty(data.getPsId().getName()) ? "--" : data.getPsId().getName());

            //样品类型
            ctSampleType.setContent(null == data.getSampleType() ? "--" :
                    StringUtil.isEmpty(data.getSampleType().getValue()) ? "--" : data.getSampleType().getValue());

            //登记时间
            ctRegisterTime.setContent(StringUtil.isEmpty(data.getRegisterTime()) ? "--" : data.getRegisterTime());

            if (data.isSelect()){
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_sel);
            }else {
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_nor);
            }
        }
    }
}