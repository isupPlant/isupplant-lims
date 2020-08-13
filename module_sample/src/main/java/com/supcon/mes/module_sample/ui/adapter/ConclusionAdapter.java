package com.supcon.mes.module_sample.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.ConclusionEntity;
import com.supcon.mes.module_sample.model.bean.InspectionItemColumnEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/11
 * class name
 */
public class ConclusionAdapter extends BaseRecyclerViewAdapter<ConclusionEntity> {
    private List<ConclusionEntity> list;
    private HashMap<String, Object> hashMap;
    private RangeAdapter rangeAdapter;
    private SinglePickController mSinglePickController;
    private List<String> spinnerList = new ArrayList<>();
    public ConclusionAdapter(Context context) {
        super(context);
        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);
    }

    @Override
    protected BaseRecyclerViewHolder<ConclusionEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    @Override
    public ConclusionEntity getItem(int position) {
        return list.get(position);
    }

    public void setData(List<ConclusionEntity> list, HashMap<String, Object> hashMap){
        this.list = list;
        this.hashMap = hashMap;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends BaseRecyclerViewHolder<ConclusionEntity>{

        @BindByTag("ivExpand")
        ImageView ivExpand;
        @BindByTag("rlRange")
        RelativeLayout rlRange;
        @BindByTag("rvRange")
        RecyclerView rvRange;
        @BindByTag("llRange")
        LinearLayout llRange;
        @BindByTag("tsConclusionColumnName")
        CustomVerticalSpinner tsConclusionColumnName;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            rangeAdapter = new RangeAdapter(context);
            rvRange.setLayoutManager(new LinearLayoutManager(context));
            rvRange.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int childLayoutPosition = parent.getChildAdapterPosition(view);
                    if (childLayoutPosition == 0) {
                        outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(5, context));
                    } else {
                        outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(5, context));
                    }
                }
            });
            rvRange.setNestedScrollingEnabled(false);
            rvRange.setAdapter(rangeAdapter);
        }

        @Override
        protected void initListener() {
            super.initListener();
            rlRange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llRange.getVisibility() == View.VISIBLE){
                        llRange.setVisibility(View.GONE);
                        ivExpand.setImageResource(R.drawable.ic_drop_down);
                    }else if (llRange.getVisibility() == View.GONE){
                        llRange.setVisibility(View.VISIBLE);
                        ivExpand.setImageResource(R.drawable.ic_drop_up);
                    }
                }
            });


            tsConclusionColumnName.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    spinnerList.clear();
                    List<InspectionItemColumnEntity> columnList = list.get(getAdapterPosition()).getColumnList();
                    for (int i = 0; i < columnList.size(); i++) {
                        spinnerList.add(columnList.get(i).getResult());
                    }
                    mSinglePickController.list(spinnerList)
                            .listener((index, item) -> {

                                for (String key: hashMap.keySet()) {
                                    if (key.equals(list.get(getAdapterPosition()).getColumnKey())){
                                        hashMap.put(key,spinnerList.get(index));
                                    }
                                }
                                notifyItemChanged(getAdapterPosition());

                            }).show();
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_conlusion;
        }

        @Override
        protected void update(ConclusionEntity data) {
            tsConclusionColumnName.setKey(data.getColumnName());
            for (String key: hashMap.keySet()) {
                if (key.equals(data.getColumnKey())){
                    tsConclusionColumnName.setContent((String) hashMap.get(key));
                    tsConclusionColumnName.setSpinner((String) hashMap.get(key));
                }
            }

            rangeAdapter.setData(data.getColumnList(),hashMap);
        }
    }
}
