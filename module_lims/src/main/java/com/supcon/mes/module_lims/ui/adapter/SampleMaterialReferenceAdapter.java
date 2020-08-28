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
import com.supcon.mes.module_lims.model.bean.SampleMaterialEntity;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class SampleMaterialReferenceAdapter extends BaseListDataRecyclerViewAdapter<SampleMaterialEntity> {
    private boolean isRadio;

    public SampleMaterialReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleMaterialEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void isRadio(boolean radio) {
        this.isRadio = radio;
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleMaterialEntity>{

        @BindByTag("tvMaterialName")
        CustomTextView tvMaterialName;
        @BindByTag("tvNumber")
        CustomTextView tvNumber;
        @BindByTag("tvBatchNumber")
        CustomTextView tvBatchNumber;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("ll_item")
        LinearLayout ll_item;

        public ViewHolder(Context context) {
            super(context);
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
        protected int layoutId() {
            return R.layout.item_sample_material_reference;
        }

        @Override
        protected void update(SampleMaterialEntity data) {

            //物料名称 || 物料编码
            if (null == data.getProductId()){
                tvMaterialName.setContent("--");
            }else {
                if (!StringUtil.isEmpty(data.getProductId().getName()) && !StringUtil.isEmpty(data.getProductId().getCode())){
                    tvMaterialName.setContent(data.getProductId().getName()+"("+data.getProductId().getCode()+")");
                }else {
                    if (StringUtil.isEmpty(data.getProductId().getName()) && StringUtil.isEmpty(data.getProductId().getCode())){
                        tvMaterialName.setContent("--");
                    }else {
                        if (StringUtil.isEmpty(data.getProductId().getName())){
                            tvMaterialName.setContent(data.getProductId().getCode());
                        }else {
                            tvMaterialName.setContent(data.getProductId().getName());
                        }
                    }
                }
            }


            //编号
            tvNumber.setContent(StringUtil.isEmpty(data.getCode()) ? "--" : data.getCode());
            //生产批号
            tvBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            if (isRadio){
                iv_select.setVisibility(View.GONE);
            }else {
                iv_select.setVisibility(View.VISIBLE);
            }

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }
        }
    }
}
