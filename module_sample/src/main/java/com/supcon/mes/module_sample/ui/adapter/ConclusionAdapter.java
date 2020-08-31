package com.supcon.mes.module_sample.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_sample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/11
 * class name
 */
public class ConclusionAdapter extends BaseListDataRecyclerViewAdapter<ConclusionEntity> {
    private HashMap<String, Object> hashMap;

    private LinearLayoutManager linearLayoutManager;
    private SinglePickController mSinglePickController;
    private List<String> spinnerList = new ArrayList<>();

    private OnConclusionChangeListener mOnConclusionChangeListener;

    public ConclusionAdapter(Context context) {
        super(context);
        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);
    }

//    @Override
//    public int getItemCount() {
//        return list.size();
//    }

    @Override
    protected BaseRecyclerViewHolder<ConclusionEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }
    
//    @Override
//    public ConclusionEntity getItem(int position) {
//        return list.get(position);
//    }

    public void setData(List<ConclusionEntity> list, HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
//        this.list = list;
//        this.notifyDataSetChanged();
    }


    class ViewHolder extends BaseRecyclerViewHolder<ConclusionEntity> {
        private RangeAdapter rangeAdapter;
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

            linearLayoutManager = new LinearLayoutManager(context);
            rvRange.setLayoutManager(linearLayoutManager);
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

        }


        @Override
        protected void initListener() {
            super.initListener();
            rlRange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getList().get(getAdapterPosition()).setOpen(!getList().get(getAdapterPosition()).isOpen());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            tsConclusionColumnName.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    spinnerList.clear();
                    List<InspectionItemColumnEntity> columnList = getList().get(getAdapterPosition()).getColumnList();
                    for (int i = 0; i < columnList.size(); i++) {
                        spinnerList.add(columnList.get(i).getResult());
                    }
                    mSinglePickController.list(spinnerList)
                            .listener((index, item) -> {

                                getItem(getAdapterPosition()).setFinalResult(spinnerList.get(index));
                                //hashMap.put("finalResult", );
//                                for (String key : hashMap.keySet()) {
//                                    if (key.equals(list.get(getAdapterPosition()).getColumnKey())) {
//
//                                    }
//                                }
                                notifyItemChanged(getAdapterPosition());
                                if (null != mOnConclusionChangeListener){
                                    mOnConclusionChangeListener.onConclusionChange(getAdapterPosition());
                                }

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
            tsConclusionColumnName.setKey(StringUtil.isEmpty(data.getColumnName()) ? "" : data.getColumnName());
            tsConclusionColumnName.setContent(StringUtil.isEmpty(data.getFinalResult()) ? "" : data.getFinalResult());

            if (data.getFinalResult() !=null && data.getFinalResult().equals(data.getColumnList().get(0).getResult())) {
                data.setQualified(false);
            } else {
                data.setQualified(true);
            }

            if (data.isQualified()) {
                tsConclusionColumnName.setContentTextColor(Color.parseColor("#808080"));
            } else {
                tsConclusionColumnName.setContentTextColor(Color.parseColor("#B20404"));
            }

            if (data.isOpen()){
                llRange.setVisibility(View.VISIBLE);
                ivExpand.setImageResource(R.drawable.ic_drop_up);
            }else {
                llRange.setVisibility(View.GONE);
                ivExpand.setImageResource(R.drawable.ic_drop_down);
            }

            rangeAdapter = new RangeAdapter(context);
            rvRange.setAdapter(rangeAdapter);
            rangeAdapter.setData(data.getColumnList(),hashMap);
        }
    }

    public void setOnConclusionChangeListener(OnConclusionChangeListener mOnConclusionChangeListener){
        this.mOnConclusionChangeListener = mOnConclusionChangeListener;
    }

    public interface OnConclusionChangeListener{
        void onConclusionChange(int position);
    }
}
