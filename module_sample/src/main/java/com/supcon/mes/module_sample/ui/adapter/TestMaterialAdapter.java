package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class TestMaterialAdapter extends BaseListDataRecyclerViewAdapter<TestMaterialEntity> {
    public TestMaterialAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<TestMaterialEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<TestMaterialEntity>{

        @BindByTag("ctMateriel")
        CustomTextView ctMateriel;
        @BindByTag("ceBatchNumber")
        CustomEditText ceBatchNumber;
        @BindByTag("ceConsumption")
        CustomEditText ceConsumption;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_test_mateial;
        }

        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(v -> onItemChildViewClick(v,0));
        }

        @Override
        protected void update(TestMaterialEntity data) {
            if (null != data){
                //物料
                if (null != data.getProductId()){
                    if (!StringUtil.isEmpty(data.getProductId().getName()) && !StringUtil.isEmpty(data.getProductId().getCode())){
                        ctMateriel.setContent(data.getProductId().getName()+"("+data.getProductId().getCode()+")");
                    }else {
                        if (StringUtil.isEmpty(data.getProductId().getName()) && StringUtil.isEmpty(data.getProductId().getCode())){
                            ctMateriel.setContent("--");
                        }else {
                            if (StringUtil.isEmpty(data.getProductId().getCode())){
                                ctMateriel.setContent(data.getProductId().getName());
                            }else {
                                ctMateriel.setContent(data.getProductId().getCode());
                            }
                        }
                    }

                    //是否启用批次
                    if (data.getProductId().isEnableBatch()){
                        ceBatchNumber.setEditable(true);
                    }else {
                        ceBatchNumber.setEditable(false);
                        ceBatchNumber.setHint("");
                    }
                }else {
                    ctMateriel.setContent("--");
                }

                ceBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "" : data.getBatchCode());
                ceConsumption.setContent(data.getUseQty() == null ? "" : data.getUseQty().setScale(2, BigDecimal.ROUND_DOWN)+"");

                if (data.isSelect()){
                    item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_sel);
                }else {
                    item.setBackgroundResource(com.supcon.mes.module_lims.R.drawable.shape_quality_standard_nor);
                }
            }
        }
    }
}
