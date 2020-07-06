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
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name 检验报告公用adapter
 */
public class SurveyReportAdapter extends BaseListDataRecyclerViewAdapter<SurveyReportEntity> {
    public SurveyReportAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SurveyReportEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SurveyReportEntity>{

        @BindByTag("tvOddNumbers")
        TextView tvOddNumbers;
        @BindByTag("tvEdit")
        TextView tvEdit;
        @BindByTag("tvInspectionRequestNo")
        CustomTextView tvInspectionRequestNo;
        @BindByTag("tvGoods")
        CustomTextView tvGoods;
        @BindByTag("tvBatchNumber")
        CustomTextView tvBatchNumber;
        @BindByTag("tvCreator")
        CustomTextView tvCreator;
        @BindByTag("tvInspectionDepartment")
        CustomTextView tvInspectionDepartment;
        @BindByTag("tvTime")
        CustomTextView tvTime;
        @BindByTag("tvTestConclusion")
        CustomTextView tvTestConclusion;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_survey_report;
        }

        @Override
        protected void update(SurveyReportEntity data) {
            //单据编号
            tvOddNumbers.setText(StringUtil.isEmpty(data.getTableNo()) ? "--" : data.getTableNo());

            //请检单号
            if (null == data.getInspectId()){
                tvInspectionRequestNo.setContent("--");
            }else {
                tvInspectionRequestNo.setContent(StringUtil.isEmpty(data.getInspectId().getTableNo()) ? "--" : data.getInspectId().getTableNo());
            }

            //物品
            if (null == data.getProdId()){
                tvGoods.setContent("--");
            }else {
                tvGoods.setContent(StringUtil.isEmpty(data.getProdId().getName()) ? "--" : data.getProdId().getName());
            }

            //批号
            tvBatchNumber.setContent(StringUtil.isEmpty(data.getBatchCode()) ? "--" : data.getBatchCode());

            //制单人
            if (null == data.getCreateStaff()){
                tvCreator.setContent("--");
            }else {
                tvCreator.setContent(StringUtil.isEmpty(data.getCreateStaff().getName()) ? "--" : data.getCreateStaff().getName());
            }

            //请检部门
            if (null == data.getCheckDeptId()){
                tvInspectionDepartment.setContent("--");
            }else {
                tvInspectionDepartment.setContent(StringUtil.isEmpty(data.getCheckDeptId().getName()) ? "--" : data.getCheckDeptId().getName());
            }

            //请检时间
            if (null == data.getCheckTime()){
                tvTime.setContent("--");
            }else {
                tvTime.setContent(DateUtil.dateFormat(data.getCheckTime(),"yyyy-MM-dd HH:mm:ss"));
            }

            //检验结论
            tvTestConclusion.setContent(StringUtil.isEmpty(data.getCheckResult()) ? "--" : data.getCheckResult());
        }
    }
}
