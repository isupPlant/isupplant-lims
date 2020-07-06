package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
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
    public InspectionApplicationAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionApplicationEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
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

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspection_application;
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

            //请检部门
            if (data.getApplyDeptId() == null){
                tvInspectionDepartment.setContent("--");
            }else {
                tvInspectionDepartment.setContent(StringUtil.isEmpty(data.getApplyDeptId().getName()) ? "--" : data.getApplyDeptId().getName());
            }

            //请检时间
            if (data.getApplyTime() == null){
                tvTime.setContent("--");
            }else {
                tvTime.setContent(DateUtil.dateFormat(data.getApplyTime(),"yyyy-MM-dd HH:mm:ss"));
            }
        }
    }
}
