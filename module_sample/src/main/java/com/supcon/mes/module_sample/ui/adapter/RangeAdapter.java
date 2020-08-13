package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.InspectionItemColumnEntity;

import java.util.HashMap;
import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class RangeAdapter extends BaseRecyclerViewAdapter<InspectionItemColumnEntity> {
    private List<InspectionItemColumnEntity> list;
    private HashMap<String, Object> hashMap;

    public RangeAdapter(Context context) {
        super(context);
    }

    public void setData(List<InspectionItemColumnEntity> list, HashMap<String, Object> hashMap){
        this.list = list;
        this.hashMap = hashMap;
        this.notifyDataSetChanged();
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionItemColumnEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    @Override
    public InspectionItemColumnEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionItemColumnEntity>{

        @BindByTag("ctRange")
        CustomTextView ctRange;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_range;
        }

        @Override
        protected void update(InspectionItemColumnEntity data) {
            if (data.getLoad()){
                ctRange.setVisibility(View.VISIBLE);
            }else {
                ctRange.setVisibility(View.GONE);
            }
            ctRange.setKey(data.getColumnName());
            for (String key: hashMap.keySet()) {
                if (key.equals(data.getColumnKey())){
                    ctRange.setContent((String) hashMap.get(key));
                }
            }
        }
    }
}
