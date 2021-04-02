package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_lims.model.bean.WareStoreEntity;
import com.supcon.mes.module_sample.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2021/2/3
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class WareStoreAdapter extends BaseListDataRecyclerViewAdapter<WareStoreEntity> {

    public WareStoreAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WareStoreEntity> getViewHolder(int viewType) {
        return new WareStoreViewHolder(context);
    }
    class WareStoreViewHolder extends BaseRecyclerViewHolder<WareStoreEntity>{

        @BindByTag("wareTv")
        CustomTextView wareTv;
        @BindByTag("wareStoreTv")
        CustomTextView wareStoreTv;
        public WareStoreViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_ware_store;
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
        protected void update(WareStoreEntity data) {
            if (data.warehouse!=null){
                wareTv.setValue(String.format("%s(s)",data.warehouse.getName(),data.warehouse.getCode()));
            }else {
                wareTv.setValue("--");
            }
            wareStoreTv.setValue(String.format("%s(s)",data.name,data.code));
        }
    }
}
