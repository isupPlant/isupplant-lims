package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;

import java.util.concurrent.TimeUnit;

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
        @BindByTag("ivIsQualified")
        ImageView ivIsQualified;
        @BindByTag("checkResultTv")
        CustomTextView checkResultTv;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_survey_report;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(2000, TimeUnit.MICROSECONDS)
                    .subscribe(o -> {
                        Bundle bundle=new Bundle();
                        SurveyReportEntity entity=getItem(getAdapterPosition());
                        bundle.putSerializable("resportEntity",entity);
                        if (entity.getTableNo().startsWith("manuReport_")){
                            IntentRouter.go(context, Constant.Router.PRODUCT_INSPREPORT_VIEW,bundle);
                        }else if(entity.getTableNo().startsWith("purchReport_")){
                            IntentRouter.go(context, Constant.Router.PURCH_INSPREPORT_VIEW,bundle);
                        } else if (entity.getTableNo().startsWith("otherReport_")){
                            IntentRouter.go(context, Constant.Router.OTHER_INSPREPORT_VIEW,bundle);
                        }
                    });
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
//            if (StringUtil.isEmpty(data.getCheckResult())){
//                ivIsQualified.setVisibility(View.GONE);
//            }else {
//                ivIsQualified.setVisibility(View.VISIBLE);
//                if (data.getCheckResult().equals("合格")){
//                    ivIsQualified.setImageResource(R.drawable.ic_qualified);
//                }else if (data.getCheckResult().equals("不合格")){
//                    ivIsQualified.setImageResource(R.drawable.ic_un_qualified);
//                }else {
//                    ivIsQualified.setVisibility(View.GONE);
//                }
//            }
            if ("不合格".equals(data.getCheckResult())){
                checkResultTv.setValueColor(Color.parseColor("#F70606"));
            }else {
                checkResultTv.setValueColor(Color.parseColor("#0BC8C1"));
            }
            checkResultTv.setValue(data.getCheckResult());

            //单据状态
            if (null == data.getPending()){
                tvEdit.setText("--");
            }else {
                tvEdit.setText(StringUtil.isEmpty(data.getPending().taskDescription) ? "--" : data.getPending().taskDescription);
            }
            if (tvEdit.getText().equals("编辑")){
                tvEdit.setTextColor(context.getResources().getColor(R.color.status_green));
            }else {
                tvEdit.setTextColor(context.getResources().getColor(R.color.status_blue));
            }
        }
    }
}
