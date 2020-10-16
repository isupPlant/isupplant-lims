package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author huodongsheng
 * on 2020/7/17
 * class name
 */
public class QualityStandardAdapter extends BaseListDataRecyclerViewAdapter<InspectionDetailPtEntity> {
    private boolean isEdit = false;
    public QualityStandardAdapter(Context context, List<InspectionDetailPtEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionDetailPtEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setList(List<InspectionDetailPtEntity> list){
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }
    


    class ViewHolder extends BaseRecyclerViewHolder<InspectionDetailPtEntity>{

        @BindByTag("ctQualityStandard")
        CustomTextView ctQualityStandard;
        @BindByTag("ctStandards")
        CustomTextView ctStandards;
        @BindByTag("ctVersionNumber")
        CustomTextView ctVersionNumber;
        @BindByTag("ctApplicationScheme")
        CustomTextView ctApplicationScheme;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_quality_standard;
        }


        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(v,0);
                }
            });
            ctApplicationScheme.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    if (action == -1){
                        getItem(getAdapterPosition()).setInspectProjId(new BaseLongIdNameEntity());
                        notifyItemChanged(getAdapterPosition());
                    }else {
                        if (isEdit){
                            onItemChildViewClick(childView,1);
                        }
                    }

                }
            });

            RxView.clicks(itemViewDelBtn)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(itemViewDelBtn, 2);
                    });
        }

        @Override
        protected void update(InspectionDetailPtEntity data) {
            if (null != data){
                if (null != data.getStdVerId()){
                    //版本号
                    ctVersionNumber.setContent(StringUtil.isEmpty(data.getStdVerId().getBusiVersion()) ? "--" : data.getStdVerId().getBusiVersion());
                    //质量标准 执行标准
                    if (null != data.getStdVerId().getStdId()){
                        ctQualityStandard.setContent(StringUtil.isEmpty(data.getStdVerId().getStdId().getName()) ? "--" : data.getStdVerId().getStdId().getName());
                        ctStandards.setContent(StringUtil.isEmpty(data.getStdVerId().getStdId().getStandard()) ? "--" : data.getStdVerId().getStdId().getStandard());
                    }else {
                        ctQualityStandard.setContent("--");
                        ctStandards.setContent("--");
                    }

                }else {
                    ctQualityStandard.setContent("--");
                    ctStandards.setContent("--");
                    ctVersionNumber.setContent("--");
                }

                //请检方案
                if (null != data.getInspectProjId()){
                    ctApplicationScheme.setContent(StringUtil.isEmpty(data.getInspectProjId().getName()) ? "--" : data.getInspectProjId().getName());
                }else {
                    ctApplicationScheme.setContent("--");
                }


            }else {
                ctQualityStandard.setContent("--");
                ctStandards.setContent("--");
                ctVersionNumber.setContent("--");
                ctApplicationScheme.setContent("--");
            }

            if (data.isSelect()){
                item.setBackgroundResource(R.drawable.shape_quality_standard_sel);
            }else {
                item.setBackgroundResource(R.drawable.shape_quality_standard_nor);
            }

            if (isEdit){
                ctApplicationScheme.setEditable(true);
            }else {
                ctApplicationScheme.setEditable(false);
            }
        }
    }
}
