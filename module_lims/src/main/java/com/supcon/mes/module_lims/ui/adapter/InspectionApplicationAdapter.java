package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;

/**
 * author huodongsheng
 * on 2020/7/3
 * class name 检验申请公用adapter
 */
public class InspectionApplicationAdapter extends BaseListDataRecyclerViewAdapter<InspectionApplicationEntity> {
    private int type;
    public InspectionApplicationAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionApplicationEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setType(int type){
        this.type = type;
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionApplicationEntity>{

        @BindByTag("tvOddNumbers")
        TextView tvOddNumbers;
        @BindByTag("tvEdit")
        TextView tvEdit;
        @BindByTag("tvGoods")
        CustomTextView tvGoods;
        @BindByTag("tvBatchNumber")
        CustomTextView tvBatchNumber;
        @BindByTag("tvExaminer")
        CustomTextView tvExaminer;
        @BindByTag("tvInspectionDepartment")
        CustomTextView tvInspectionDepartment;
        @BindByTag("tvTime")
        CustomTextView tvTime;
        @BindByTag("item")
        LinearLayout item;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspection_application;
        }

        @Override
        protected void initListener() {
            super.initListener();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(item,0);
                }
            });
        }

        @Override
        protected void update(InspectionApplicationEntity data) {
            tvOddNumbers.setText(StringUtil.isEmpty(data.getTableNo()) ? "--" : data.getTableNo());
            //物品名称+编码
            if (data.getProdId() == null){
                tvGoods.setContent("--");
            }else {
                if (!StringUtil.isEmpty(data.getProdId().getName()) && !StringUtil.isEmpty(data.getProdId().getCode())){
                    tvGoods.setContent(data.getProdId().getName()+"("+data.getProdId().getCode()+")");
                }else {
                    if (!StringUtil.isEmpty(data.getProdId().getName())){
                        tvGoods.setContent(data.getProdId().getName());
                    }else {
                        tvGoods.setContent(data.getProdId().getCode());
                    }
                }
            }

            //批号
            tvBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            //请检人
            if (data.getApplyStaffId() == null){
                tvExaminer.setContent("--");
            }else {
                tvExaminer.setContent(StringUtil.isEmpty(data.getApplyStaffId().getName()) ? "--" : data.getApplyStaffId().getName());
            }


            if (type == 2){
                //请检部门
                if (data.getCreateDepartment() == null){
                    tvInspectionDepartment.setContent("--");
                }else {
                    tvInspectionDepartment.setContent(StringUtil.isEmpty(data.getCreateDepartment().getName()) ? "--" : data.getCreateDepartment().getName());
                }
            }else {
                if (data.getApplyDeptId() == null){
                    tvInspectionDepartment.setContent("--");
                }else {
                    tvInspectionDepartment.setContent(StringUtil.isEmpty(data.getApplyDeptId().getName()) ? "--" : data.getApplyDeptId().getName());
                }
            }


            //请检时间
            if (data.getApplyTime() == null){
                tvTime.setContent("--");
            }else {
                tvTime.setContent(DateUtil.dateFormat(data.getApplyTime(),"yyyy-MM-dd HH:mm:ss"));
            }

            //单据状态
            if (null == data.getPending()){
                tvEdit.setText("--");
            }else {
                tvEdit.setText(StringUtil.isEmpty(data.getPending().taskDescription) ? "--" : data.getPending().taskDescription);
            }
            if (tvEdit.getText().equals("编辑")){
                tvEdit.setTextColor(Color.parseColor("#1E82D2"));
            }else {
                tvEdit.setTextColor(Color.parseColor("#46B479"));
            }
        }
    }
}
