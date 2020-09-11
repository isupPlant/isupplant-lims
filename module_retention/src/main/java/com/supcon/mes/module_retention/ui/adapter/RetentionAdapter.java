package com.supcon.mes.module_retention.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_retention.IntentRouter;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;

import java.util.concurrent.TimeUnit;


/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
public class RetentionAdapter extends BaseListDataRecyclerViewAdapter<RetentionEntity> {
    public RetentionAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RetentionEntity> getViewHolder(int viewType) {
        return new RetentionViewHolder(context);
    }

    class RetentionViewHolder extends BaseRecyclerViewHolder<RetentionEntity> {

        @BindByTag("tvOddNumbers")
        TextView tvOddNumbers;
        @BindByTag("tvEdit")
        TextView tvEdit;
        @BindByTag("sampleTv")
        CustomTextView sampleTv;
        @BindByTag("materialTv")
        CustomTextView materialTv;
        @BindByTag("batchCodeTv")
        CustomTextView batchCodeTv;
        @BindByTag("keeperTv")
        CustomTextView keeperTv;
        @BindByTag("pSiteTv")
        CustomTextView pSiteTv;
        @BindByTag("retainDateTv")
        CustomTextView retainDateTv;

        public RetentionViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            tvEdit.setTextColor(context.getResources().getColor(com.supcon.mes.module_lims.R.color.status_blue));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_retention;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(2000, TimeUnit.MICROSECONDS)
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("retentionEntity", getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.RETENTION_VIEW, bundle);
                    });
        }

        @Override
        protected void update(RetentionEntity data) {
            tvOddNumbers.setText(data.tableNo);
            if (data.sampleId != null && data.sampleId.getId() != null) {
                sampleTv.setValue(String.format("%s(%s)", data.sampleId.getName(), data.sampleId.getCode()));
                pSiteTv.setValue(data.sampleId.getPsId() != null && data.sampleId.getPsId().getId() != null ? data.sampleId.getPsId().getName() : "");
            }
            if (data.productId != null && data.productId.getId() != null) {
                materialTv.setValue(String.format("%s(%s)", data.productId.getName(), data.productId.getCode()));
            }
            batchCodeTv.setValue(data.batchCode);
            keeperTv.setValue(data.keeperId != null && data.keeperId.getId() != null ? data.keeperId.getName() : "");
            retainDateTv.setValue(data.retainDate != null ? DateUtil.dateFormat(data.retainDate) : "");
            if (!TextUtils.isEmpty(data.pending.openUrl)) {
                if (data.pending.openUrl.contains("retentionEdit")) {
                    tvEdit.setText(context.getResources().getString(R.string.lims_edit));
                } else if (data.pending.openUrl.contains("retentionView")) {
                    tvEdit.setText(context.getResources().getString(R.string.lims_approve));
                }
            }else {
                tvEdit.setText(data.pending.taskDescription);
            }
        }
    }
}
