package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        @BindByTag("ll_item")
        LinearLayout ll_item;
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
        @BindByTag("rl_select")
        RelativeLayout rl_select;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_inquiry;
        }

        @Override
        protected void initListener() {
            super.initListener();
            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(v,0);
                }
            });
//            rl_select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemChildViewClick(v,0);
//                }
//            });

        }

        @Override
        protected void update(SampleInquiryEntity data) {
            //??????
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

            //??????
            tvBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            //?????????
            if (null == data.getPsId()){
                tvSamplingPoint.setContent("--");
            }else {
                tvSamplingPoint.setContent(StringUtil.isEmpty(data.getPsId().getName()) ? "--" : data.getPsId().getName());
            }

            //????????????
            tvRegistrationTime.setContent(data.getRegisterTime() == null ? "--" : DateUtil.dateFormat(data.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            if (data.isThisCompany){
                iv_select.setVisibility(View.VISIBLE);
            }else {
                iv_select.setVisibility(View.INVISIBLE);
            }

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }

        }

    }
}
