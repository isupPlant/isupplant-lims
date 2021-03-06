package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;

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

        @BindByTag("ctNumber")
        CustomTextView ctNumber;
        @BindByTag("ctMateriel")
        CustomTextView ctMateriel;
        @BindByTag("ceBatchNumber")
        CustomTextView ceBatchNumber;
        @BindByTag("ceConsumption")
        CustomEditText ceConsumption;
        @BindByTag("item")
        LinearLayout item;
        @BindByTag("ctMaterielUnit")
        CustomTextView ctMaterielUnit;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_test_mateial;
        }

        @Override
        protected void initView() {
            super.initView();
            ctMateriel.contentView().setSingleLine(true);
            ceBatchNumber.setKeyTextColor(Color.parseColor("#666666"));
            ceConsumption.setKeyTextColor(Color.parseColor("#666666"));
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(v -> onItemChildViewClick(v,0));

//            RxTextView.textChanges(ceBatchNumber.editText())
//                    .skipInitialValue()
//                    .subscribe(new Consumer<CharSequence>() {
//                        @Override
//                        public void accept(CharSequence charSequence) throws Exception {
//                            getList().get(getAdapterPosition()).setBatchCode(charSequence.toString());
//                        }
//                    });
//            ceBatchNumber.setOnChildViewClickListener(new OnChildViewClickListener() {
//                @Override
//                public void onChildViewClick(View childView, int action, Object obj) {
//                    if (action == -1){
//                        getList().get(getAdapterPosition()).setBatchCode("");
//                    }
//                }
//            });

            //??????????????????
            ceConsumption.editText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            RxTextView.textChanges(ceConsumption.editText())
                    .skipInitialValue()
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence s) throws Exception {
                            //?????????.???????????????2???????????????
                            if (s.toString().contains(".")) {
                                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                    s = s.toString().subSequence(0,
                                            s.toString().indexOf(".") + 3);
                                    ceConsumption.setContent(s.toString());
                                    ceConsumption.editText().setSelection(s.length()); //??????????????????
                                }
                            }
                            //??????"."???????????????,????????????????????????0
                            if (s.toString().trim().substring(0).equals(".")) {
                                s = "0" + s;
                                ceConsumption.setContent(s.toString());
                                ceConsumption.editText().setSelection(2);
                            }

                            //?????????????????????0,????????????????????????".",?????????????????????
                            if (s.toString().startsWith("0")
                                    && s.toString().trim().length() > 1) {
                                if (!s.toString().substring(1, 2).equals(".")) {
                                    ceConsumption.editText().setText(s.subSequence(0, 1));
                                    ceConsumption.editText().setSelection(1);
                                    return;
                                }
                            }

                            if (s.length() > 0){
                                ceConsumption.setKeyTextColor(Color.parseColor("#666666"));
                                ceConsumption.setContentTextColor(Color.parseColor("#333333"));
                                getList().get(getAdapterPosition()).setUseQty(new BigDecimal(s.toString()).setScale(2, BigDecimal.ROUND_DOWN));

                            }else {
                                ceConsumption.setKeyTextColor(Color.parseColor("#B20404"));
                                getList().get(getAdapterPosition()).setUseQty(null);
                            }
                        }
                    });

//            ceConsumption.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus){
//                        ceConsumption.setContent("");
//                    }
////                    else {
////                        if (TextUtils.isEmpty(ceConsumption.getContent())){
////                            ceConsumption.setContent(0);
////                        }
////                    }
//                }
//            });


        }

        @Override
        protected void update(TestMaterialEntity data) {
            if (null != data){
                //??????
                if (null != data.getProductId()){
                    if (!StringUtil.isEmpty(data.getProductId().getName()) && !StringUtil.isEmpty(data.getProductId().getCode())){
                        ctMateriel.setContent(data.getProductId().getName()+"("+data.getProductId().getCode()+")");

                    }else {
                        if (StringUtil.isEmpty(data.getProductId().getName()) && StringUtil.isEmpty(data.getProductId().getCode())){
                            ctMateriel.setContent("");
                        }else {
                            if (StringUtil.isEmpty(data.getProductId().getCode())){
                                ctMateriel.setContent(data.getProductId().getName());
                            }else {
                                ctMateriel.setContent(data.getProductId().getCode());
                            }
                        }
                    }

                    if (null != data.getProductId().getMainUnit()){
                        ctMaterielUnit.setValue(StringUtil.isEmpty(data.getProductId().getMainUnit().getName()) ? "" :  data.getProductId().getMainUnit().getName());
                    }else {
                        ctMaterielUnit.setValue("");
                    }


//                    //??????????????????
//                    if (data.getProductId().isEnableBatch()){
//                        ceBatchNumber.setEditable(true);
//                    }else {
//                        ceBatchNumber.setEditable(false);
//                        ceBatchNumber.setHint("");
//                    }
                }else {
                    ctMateriel.setContent("--");
                    ctMaterielUnit.setValue("");
                }
                ctNumber.setContent(StringUtil.isEmpty(data.getMatCode()) ? "" : data.getMatCode());
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
