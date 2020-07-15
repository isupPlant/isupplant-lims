package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;

/**
 * author huodongsheng
 * on 2020/7/10
 * class name
 */
public class QualityStandardReferenceAdapter extends BaseListDataRecyclerViewAdapter<QualityStandardReferenceEntity> {
    public QualityStandardReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<QualityStandardReferenceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<QualityStandardReferenceEntity>{

        @BindByTag("tvQualityStandard")
        CustomTextView tvQualityStandard;
        @BindByTag("tvVersionNumber")
        CustomTextView tvVersionNumber;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("ll_item")
        LinearLayout ll_item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_quality_standard_reference;
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
        }

        @Override
        protected void update(QualityStandardReferenceEntity data) {
            //质量标准
            if (null == data.getStdId()){
                tvQualityStandard.setContent("--");
            }else {
                tvQualityStandard.setContent(StringUtil.isEmpty(data.getStdId().getName()) ? "--" : data.getStdId().getName());
            }

            //版本号
            tvVersionNumber.setContent(StringUtil.isEmpty(data.getBusiVersion()) ? "--" : data.getBusiVersion());

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }

        }
    }
}
