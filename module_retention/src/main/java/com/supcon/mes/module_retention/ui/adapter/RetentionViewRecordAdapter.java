package com.supcon.mes.module_retention.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.bean.RecordViewEntity;

/**
 * Created by wanghaidong on 2020/8/28
 * Email:wanghaidong1@supcon.com
 */
public class RetentionViewRecordAdapter extends BaseListDataRecyclerViewAdapter<RecordViewEntity> {
    public RetentionViewRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RecordViewEntity> getViewHolder(int viewType) {
        return new RetentionViewRecordHolder(context);
    }
    class RetentionViewRecordHolder extends BaseRecyclerViewHolder<RecordViewEntity>{

        @BindByTag("observeItemTv")
        CustomTextView observeItemTv;
        @BindByTag("observeValueTv")
        CustomTextView observeValueTv;
        @BindByTag("observeResultTv")
        CustomTextView observeResultTv;
        @BindByTag("memoFieldTv")
        CustomVerticalTextView memoFieldTv;
        public RetentionViewRecordHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_view_record;
        }

        @Override
        protected void update(RecordViewEntity data) {
            observeItemTv.setValue(data.observeItem);
            observeValueTv.setValue(data.observeValue);

            if (data.observeResult!=null){
                if ("LIMSRetain_observeResult/qualified".equals(data.observeResult.getId()))
                    observeResultTv.setValueColor(Color.parseColor("#0BC8C1"));
                else
                    observeResultTv.setValueColor(Color.parseColor("#F70606"));
                observeResultTv.setValue(data.observeResult.getValue());
            }else {
                observeResultTv.setValue("");
            }

            memoFieldTv.setValue(data.memoField);
        }
    }
}
