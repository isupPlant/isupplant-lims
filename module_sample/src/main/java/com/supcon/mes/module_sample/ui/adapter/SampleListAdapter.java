package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.module_sample.R;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
public class SampleListAdapter extends BaseListDataRecyclerViewAdapter<SampleEntity> {
    public SampleListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleEntity>{

        @BindByTag("item")
        LinearLayout item;
        @BindByTag("tvSample")
        CustomTextView tvSample;
        @BindByTag("ctMateriel")
        CustomTextView ctMateriel;
        @BindByTag("ctBatchNumber")
        CustomTextView ctBatchNumber;
        @BindByTag("ctSampleSeparationType")
        CustomTextView ctSampleSeparationType;
        @BindByTag("ctSamplingPoint")
        CustomTextView ctSamplingPoint;
        @BindByTag("tvSampleType")
        TextView tvSampleType;
        @BindByTag("ctRegisterTime")
        CustomTextView ctRegisterTime;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_list;
        }

        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(v -> onItemChildViewClick(v,0));
        }

        @Override
        protected void initView() {
            super.initView();
            tvSample.contentView().setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        @Override
        protected void update(SampleEntity data) {
            //???????????? & ????????????
            if (!StringUtil.isEmpty(data.getName()) && !StringUtil.isEmpty(data.getCode())){
                tvSample.setContent(data.getName()+"("+data.getCode()+")");
            }else {
                if (StringUtil.isEmpty(data.getName()) && StringUtil.isEmpty(data.getCode())){
                    tvSample.setContent("--");
                }else {
                    if (StringUtil.isEmpty(data.getName())){
                        tvSample.setContent(data.getCode());
                    }else {
                        tvSample.setContent(data.getName());
                    }
                }
            }

            tvSampleType.setText(null == data.getSampleType() ? "--" :
                    StringUtil.isEmpty(data.getSampleType().getValue()) ? "--" : data.getSampleType().getValue());


            //???????????? & ????????????
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

            //??????
            ctBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            //????????????
            ctSampleSeparationType.setContent(null == data.getSperaType() ? "--" :
                    StringUtil.isEmpty(data.getSperaType().getValue()) ? "--" : data.getSperaType().getValue());

            //?????????
            ctSamplingPoint.setContent(null == data.getPsId() ? "--" :
                    StringUtil.isEmpty(data.getPsId().getName()) ? "--" : data.getPsId().getName());

//            //????????????
//            ctSampleType.setContent(null == data.getSampleType() ? "--" :
//                    StringUtil.isEmpty(data.getSampleType().getValue()) ? "--" : data.getSampleType().getValue());

            //????????????
            ctRegisterTime.setContent(StringUtil.isEmpty(data.getRegisterTime()) ? "--" : handleTime(data.getRegisterTime()));

            if (data.isSelect()){
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_sel);
            }else {
                item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_nor);
            }
        }
    }

    private String handleTime(String time){
        if (Util.isContainPoint(time)){
            if (!StringUtil.isEmpty(time)){
                int i = time.indexOf(".");
                String substring = time.substring(0, i);
                return substring;
            }
            return "";
        }
        return time;
    }
}
