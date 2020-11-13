package com.supcon.mes.module_lims.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/11/12
 * class name
 */
public class StdVerComReferenceAdapter extends BaseListDataRecyclerViewAdapter<StdVerComIdEntity> {
    public StdVerComReferenceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<StdVerComIdEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<StdVerComIdEntity>{

        @BindByTag("tvInspectionItems")
        CustomTextView tvInspectionItems;
        @BindByTag("tvUnit")
        CustomTextView tvUnit;
        @BindByTag("iv_select")
        ImageView iv_select;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_std_ver_com_ref;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(item)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                          onItemChildViewClick(item,1);
                        }
                    });
        }

        @Override
        protected void update(StdVerComIdEntity data) {
            tvInspectionItems.setContent(StringUtil.isEmpty(data.getReportName()) ? "--" : data.getReportName());
            tvUnit.setContent(StringUtil.isEmpty(data.getUnitName()) ? "--" : data.getUnitName());
            if (data.isSelect()){
                iv_select.setImageResource(R.drawable.ic_check_yes);
            }else {
                iv_select.setImageResource(R.drawable.ic_check_no);
            }
        }
    }
}
