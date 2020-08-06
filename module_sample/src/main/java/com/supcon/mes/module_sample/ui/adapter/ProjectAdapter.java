package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class ProjectAdapter extends BaseListDataRecyclerViewAdapter<InspectionSubEntity> {
    private List<InspectionItemColumnEntity> conclusionList;
    public ProjectAdapter(Context context) {
        super(context);
    }

    public void setConclusionList(List<InspectionItemColumnEntity> conclusionList){
        this.conclusionList = conclusionList;
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
        @BindByTag("expandTv")
        TextView expandTv;
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
                    }
                }else {
                    setVisible(true);
                }
            }else {
                setVisible(true);
            }
            ctRoundOffValue.setContent(StringUtil.isEmpty(data.getRoundValue()) ? "--" : data.getRoundValue()); //修约值
            ceReportedValue.setContent(StringUtil.isEmpty(data.getDispValue()) ? "--" : data.getDispValue()); //报出值

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
}
