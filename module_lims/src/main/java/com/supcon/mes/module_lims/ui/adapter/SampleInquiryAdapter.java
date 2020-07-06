package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.SampleInquiryEntity;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name
 */
public class SampleInquiryAdapter extends BaseListDataRecyclerViewAdapter<SampleInquiryEntity> {
    public SampleInquiryAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleInquiryEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleInquiryEntity>{

        @BindByTag("tvSample")
        CustomTextView tvSample;
        @BindByTag("tvBatchNumber")
        CustomTextView tvBatchNumber;
        @BindByTag("tvSamplingPoint")
        CustomTextView tvSamplingPoint;
        @BindByTag("tvRegistrationTime")
        CustomTextView tvRegistrationTime;
        @BindByTag("iv_select")
        ImageView iv_select;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_inquiry;
        }

        @Override
        protected void update(SampleInquiryEntity data) {
            //样品
            tvSample.setContent(StringUtil.isEmpty(data.getCode()) ? "--" : data.getCode());

            //批号
            tvBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            //采样点
            if (null == data.getPsId()){
                tvSamplingPoint.setContent("--");
            }else {
                tvSamplingPoint.setContent(StringUtil.isEmpty(data.getPsId().getName()) ? "--" : data.getPsId().getName());
            }

            //登记时间
            tvRegistrationTime.setContent(data.getRegisterTime() == null ? "--" : DateUtil.dateFormat(data.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }

        }

    }
}
