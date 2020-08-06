package com.supcon.mes.module_retention.ui.adapter;

import android.content.Context;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.bean.RecordEntity;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
public class RecordAdapter extends BaseListDataRecyclerViewAdapter<RecordEntity> {
    public RecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RecordEntity> getViewHolder(int viewType) {
        return new RecordViewHolder(context);
    }
    class RecordViewHolder extends BaseRecyclerViewHolder<RecordEntity>{

        @BindByTag("planDateTv")
        CustomTextView planDateTv;
        @BindByTag("realDateTv")
        CustomTextView realDateTv;
        @BindByTag("stateTv")
        CustomTextView stateTv;
        public RecordViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_record;
        }

        @Override
        protected void update(RecordEntity data) {
            planDateTv.setValue(data.planDate!=null? DateUtil.dateFormat(data.planDate):"");
            realDateTv.setValue(data.realDate!=null?DateUtil.dateFormat(data.realDate):"");
            stateTv.setValue(data.retPlanState!=null?data.retPlanState.getValue():"");
        }
    }
}
