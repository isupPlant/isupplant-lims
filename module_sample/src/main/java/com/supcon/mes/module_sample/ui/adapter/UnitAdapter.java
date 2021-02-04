package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseldEntity;
import com.supcon.mes.middleware.model.bean.Unit;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;
import com.supcon.mes.module_sample.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2021/2/3
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class UnitAdapter extends BaseListDataRecyclerViewAdapter<BaseSystemBackEntity> {
    public UnitAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BaseSystemBackEntity> getViewHolder(int viewType) {
        return new UnitViewHolder(context);
    }
    class UnitViewHolder extends BaseRecyclerViewHolder<BaseSystemBackEntity>{

        @BindByTag("sampleUnitTv")
        CustomTextView sampleUnitTv;
        public UnitViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_unit;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                       onItemChildViewClick(itemView,0);
                    });
        }

        @Override
        protected void update(BaseSystemBackEntity data) {
            sampleUnitTv.setValue(String.format("%s(s)",data.getName(),data.getCode()));
        }
    }
}
