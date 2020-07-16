package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.InspectResportDetailEntity;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class InspectReportDetailAdapter extends BaseListDataRecyclerViewAdapter<InspectResportDetailEntity> {
    public InspectReportDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectResportDetailEntity> getViewHolder(int viewType) {
        return new InsportReportDetailViewHolder(context);
    }
    class InsportReportDetailViewHolder extends BaseRecyclerViewHolder<InspectResportDetailEntity>{

        @BindByTag("reportNameTv")
        CustomTextView reportNameTv;
        @BindByTag("dispvalueTv")
        CustomTextView dispvalueTv;
        @BindByTag("checkResultTv")
        CustomTextView checkResultTv;
        public InsportReportDetailViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspect_project;
        }

        @Override
        protected void update(InspectResportDetailEntity data) {
            reportNameTv.setValue(data.reportName);
            dispvalueTv.setValue(data.dispValue);
            checkResultTv.setValue(data.checkResult);
        }
    }
}
