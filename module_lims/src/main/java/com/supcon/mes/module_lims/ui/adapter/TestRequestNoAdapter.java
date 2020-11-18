package com.supcon.mes.module_lims.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.TestRequestNoEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/11/16
 * class name
 */
public class TestRequestNoAdapter extends BaseListDataRecyclerViewAdapter<TestRequestNoEntity> {
    public TestRequestNoAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<TestRequestNoEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<TestRequestNoEntity>{

        @BindByTag("ctBillNumber")
        CustomTextView ctBillNumber;
        @BindByTag("ctBusinessType")
        CustomTextView ctBusinessType;
        @BindByTag("ctSamplingPoint")
        CustomTextView ctSamplingPoint;
        @BindByTag("ctMateriel")
        CustomTextView ctMateriel;
        @BindByTag("ctBatchNumber")
        CustomTextView ctBatchNumber;
        @BindByTag("ctStatus")
        CustomTextView ctStatus;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("llItem")
        LinearLayout llItem;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_test_request_no;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(llItem)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onItemChildViewClick(llItem,1);
                        }
                    });
        }

        @Override
        protected void update(TestRequestNoEntity data) {
            ctBillNumber.setContent(StringUtil.isEmpty(data.getTableNo()) ? "" : data.getTableNo());
            ctBusinessType.setContent(data.getBusiTypeId() == null ? "" :
                    StringUtil.isEmpty(data.getBusiTypeId().getName()) ? "" :
                            data.getBusiTypeId().getName());
            ctSamplingPoint.setContent(data.getPsId() == null ? "" :
                    StringUtil.isEmpty(data.getPsId().getName()) ? "" :
                    data.getPsId().getName());

            if (data.getProdId() != null){
                if (StringUtil.isEmpty(data.getProdId().getName()) && StringUtil.isEmpty(data.getProdId().getCode())){
                    ctMateriel.setContent("");
                }else {
                    if (!StringUtil.isEmpty(data.getProdId().getName()) && !StringUtil.isEmpty(data.getProdId().getCode())){
                        ctMateriel.setContent(data.getProdId().getName()+"("+data.getProdId().getCode()+")");
                    }else {
                        if (StringUtil.isEmpty(data.getProdId().getName())){
                            ctMateriel.setContent(data.getProdId().getCode());
                        }else {
                            ctMateriel.setContent(data.getProdId().getName());
                        }
                    }

                }
            }else {
                ctMateriel.setContent("");
            }

            ctBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "" : data.getBatchCode());

            ctStatus.setContent(data.getCheckState() == null ? "" :
                    StringUtil.isEmpty(data.getCheckState().getValue()) ? "" :
                            data.getCheckState().getValue());

            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }
        }
    }
}
