package com.supcon.mes.module_retention.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.bean.RecordEntity;

import java.util.concurrent.TimeUnit;

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
    public int selectPosition=-1;
    public boolean edit=false;
    class RecordViewHolder extends BaseRecyclerViewHolder<RecordEntity>{

        @BindByTag("planDateTv")
        CustomTextView planDateTv;
        @BindByTag("realDateTv")
        CustomTextView realDateTv;
        @BindByTag("stateTv")
        CustomTextView stateTv;
        @BindByTag("ll_item")
        LinearLayout ll_item;
        @BindByTag("ll_real")
        LinearLayout ll_real;
        @BindByTag("ll_state")
        LinearLayout ll_state;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        public RecordViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_record;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
                RxView.clicks(ll_item)
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .subscribe(o -> {
                            if (!edit){
                                selectPosition = getAdapterPosition();
                                notifyDataSetChanged();
                            }

                        });
                RxView.clicks(itemViewDelBtn)
                        .throttleFirst(500,TimeUnit.MILLISECONDS)
                        .subscribe(o -> {
                            if (edit)
                            onItemChildViewClick(itemView,1);
                        });
        }

        @Override
        protected void update(RecordEntity data) {
            planDateTv.setValue(data.planDate!=null? DateUtil.dateFormat(data.planDate):"");
            if (edit) {
                ll_real.setVisibility(View.GONE);
                ll_state.setVisibility(View.GONE);
            }
            else  {
                realDateTv.setValue(data.realDate != null ? DateUtil.dateFormat(data.realDate) : "");
                stateTv.setValue(data.retPlanState != null ? data.retPlanState.getValue() : "");
                if (getAdapterPosition() == selectPosition)
                    ll_item.setBackground(context.getResources().getDrawable(R.drawable.bg_record));
                else
                    ll_item.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
    }
}
