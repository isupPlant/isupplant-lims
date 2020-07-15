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
import com.supcon.mes.module_lims.model.bean.MaterialReferenceEntity;

/**
 * author huodongsheng
 * on 2020/7/9
 * class name
 */
public class MaterialReferenceAdapter extends BaseListDataRecyclerViewAdapter<MaterialReferenceEntity> {
    public MaterialReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<MaterialReferenceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<MaterialReferenceEntity>{

        @BindByTag("tvMaterialName")
        CustomTextView tvMaterialName;
        @BindByTag("tvSpec")
        CustomTextView tvSpec;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("ll_item")
        LinearLayout ll_item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_material_reference;
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
        protected void update(MaterialReferenceEntity data) {
            // 物料名称
            if (StringUtil.isEmpty(data.getName()) && StringUtil.isEmpty(data.getCode())){
                tvMaterialName.setContent("--");
            }else {
                if (!StringUtil.isEmpty(data.getName()) && !StringUtil.isEmpty(data.getCode())){
                    tvMaterialName.setContent(data.getName()+"("+data.getCode()+")");
                }else {
                    if (StringUtil.isEmpty(data.getName())){
                        tvMaterialName.setContent(data.getCode());
                    }else {
                        tvMaterialName.setContent(data.getName());
                    }
                }
            }


            //规格型号
            if (StringUtil.isEmpty(data.getSpecifications()) && StringUtil.isEmpty(data.getModel())){
                tvSpec.setContent("--");
            }else {
                if (!StringUtil.isEmpty(data.getSpecifications()) && !StringUtil.isEmpty(data.getModel())){
                    tvSpec.setContent(data.getSpecifications()+"("+data.getModel()+")");
                }else {
                    if (StringUtil.isEmpty(data.getSpecifications())){
                        tvSpec.setContent(data.getModel());
                    }else {
                        tvSpec.setContent(data.getSpecifications());
                    }
                }
            }

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }

        }
    }
}
