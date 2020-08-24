package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_sample.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class ProjectAdapter extends BaseListDataRecyclerViewAdapter<InspectionSubEntity> {

    private ConclusionAdapter conclusionAdapter;

    private OriginalValueChangeListener mOriginalValueChangeListener;
    private String originalValue;
    private boolean originalFocus;


    public ProjectAdapter(Context context) {
        super(context);

    }

    @Override
    protected BaseRecyclerViewHolder<InspectionSubEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionSubEntity>{

        @BindByTag("ctInspectionItems")
        CustomTextView ctInspectionItems;
        @BindByTag("ceOriginalValue")
        CustomEditText ceOriginalValue;
        @BindByTag("cpOriginalValue")
        CustomSpinner cpOriginalValue;
        @BindByTag("ctRoundOffValue")
        CustomTextView ctRoundOffValue;
        @BindByTag("ceReportedValue")
        CustomEditText ceReportedValue;
        @BindByTag("ivExpand")
        ImageView ivExpand;
        @BindByTag("llQualityStandard")
        LinearLayout llQualityStandard;
        @BindByTag("imageUpDown")
        ImageView imageUpDown;
        @BindByTag("llEnclosure")
        LinearLayout llEnclosure;
        @BindByTag("rvConclusion")
        RecyclerView rvConclusion;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspection_sub_project;
        }

        @Override
        protected void initView() {
            super.initView();
            conclusionAdapter = new ConclusionAdapter(context);
            rvConclusion.setLayoutManager(new LinearLayoutManager(context));
            rvConclusion.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int childLayoutPosition = parent.getChildAdapterPosition(view);
                    if (childLayoutPosition == 0) {
                        outRect.set(DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context));
                    } else if(childLayoutPosition == conclusionAdapter.getItemCount()-1){
                        outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(0, context));
                    } else {
                        outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context));
                    }
                }
            });
            rvConclusion.setNestedScrollingEnabled(false);
            rvConclusion.setAdapter(conclusionAdapter);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(llQualityStandard)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (rvConclusion.getVisibility() == View.VISIBLE){
                                rvConclusion.setVisibility(View.GONE);
                                ivExpand.setImageResource(R.drawable.ic_drop_down);
                            }else if (rvConclusion.getVisibility() == View.GONE){
                                rvConclusion.setVisibility(View.VISIBLE);
                                ivExpand.setImageResource(R.drawable.ic_drop_up);
                            }


                        }
                    });

            //原始值数值变化监听
            RxTextView.textChanges(ceOriginalValue.editText())
                    .skipInitialValue()
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence charSequence) throws Exception {
                            originalValue = charSequence.toString();
                        }
                    });
            //原始值焦点监听
            ceOriginalValue.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    originalFocus = hasFocus;
                        if (null != mOriginalValueChangeListener){
                            mOriginalValueChangeListener.originalValueChange(originalFocus,originalValue,getAdapterPosition());
                        }
                    }
            });

        }

        @Override
        protected void update(InspectionSubEntity data) {
            //检验项目
            ctInspectionItems.setContent(StringUtil.isEmpty(data.getComName()) ? "--" : data.getComName());

            //原始值
            if(null != data.getValueKind()){ // 值类型
                if (!StringUtil.isEmpty(data.getValueKind().getValue())){ // 值类型不为空
                    if (data.getValueKind().getValue().equals("枚举")){
                        setVisible(false);
                    }else if (data.getValueKind().getValue().equals("计算")){
                        setVisible(true);
                        ceOriginalValue.setEditable(false);
                        ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "--" : data.getOriginValue());
                    }else {
                        setVisible(true);
                        ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "--" : data.getOriginValue());
                    }
                }else {
                    setVisible(true);
                }
            }else {
                setVisible(true);
            }
            ctRoundOffValue.setContent(StringUtil.isEmpty(data.getRoundValue()) ? "--" : data.getRoundValue()); //修约值
            ceReportedValue.setContent(StringUtil.isEmpty(data.getDispValue()) ? "--" : data.getDispValue()); //报出值

            conclusionAdapter.setData(data.getConclusionList(),data.getDispMap());
        }

        private void setVisible(boolean visible){
            if (visible){
                ceOriginalValue.setVisibility(View.VISIBLE);
                cpOriginalValue.setVisibility(View.GONE);
            }else {
                ceOriginalValue.setVisibility(View.GONE);
                cpOriginalValue.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setOriginalValueChangeListener(OriginalValueChangeListener mOriginalValueChangeListener){
        this.mOriginalValueChangeListener = mOriginalValueChangeListener;
    }

    public interface OriginalValueChangeListener{
        void originalValueChange(boolean hasFocus, String value, int position);
    }
}
