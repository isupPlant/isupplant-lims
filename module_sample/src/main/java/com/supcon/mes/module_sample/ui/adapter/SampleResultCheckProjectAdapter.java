package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.bean.SanpleResultCheckItemEntity;
import com.yaobing.module_middleware.Utils.MyDateUtils;

/**
 * @author : yaobing
 * @date : 2021/6/2 13:40
 * @desc :
 */
public class SampleResultCheckProjectAdapter extends BaseListDataRecyclerViewAdapter<SanpleResultCheckItemEntity> {
    public SampleResultCheckProjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindByTag("tvOrderNumber")
        TextView tvOrderNumber;
        @BindByTag("tvCheckProject")
        CustomTextView tvCheckProject;
        @BindByTag("repeat_number")
        CustomTextView repeat_number;
        @BindByTag("vision_number")
        CustomTextView vision_number;
        @BindByTag("check_method")
        CustomTextView check_method;
        @BindByTag("operating_procedures")
        CustomTextView operating_procedures;
        @BindByTag("sample_status")
        CustomTextView sample_status;
        @BindByTag("check_person")
        CustomTextView check_person;
        @BindByTag("check_time")
        CustomTextView check_time;
        @BindByTag("memo")
        CustomTextView memo;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_result_check_project;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(Object data) {
                SampleResultCheckProjectEntity entity = (SampleResultCheckProjectEntity) data;
                int order = getLayoutPosition()+1;
                tvOrderNumber.setText(context.getResources().getString(R.string.index)+ order);
                tvCheckProject.setContent(entity.getTestId().getName());
                repeat_number.setContent(String.valueOf(entity.getParallelNo()));
                vision_number.setContent(null == entity.getTestId() ? "" : String.valueOf(entity.getTestId().getBusiVersion()));
                check_method.setContent(null == entity.getTestMethodId() ? "" : entity.getTestMethodId().getMethod());
                operating_procedures.setContent(null == entity.getTestMethodId() ? "" : entity.getTestMethodId().getProcedureNo());
                sample_status.setContent(null == entity.getTestState() ? "" : entity.getTestState().getValue());
                check_person.setContent(null == entity.getTestStaffId() ? "" : entity.getTestStaffId().getName());
                check_time.setContent(null == entity.getTestTime() ? "" : MyDateUtils.getDateFromLong(entity.getTestTime(),MyDateUtils.date_Format));
                memo.setContent(null == entity.getMemoField() ? "" : entity.getMemoField().toString());
        }
    }

}
