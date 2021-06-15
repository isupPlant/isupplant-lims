package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.yaobing.module_middleware.Utils.MyDateUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author : yaobing
 * @date : 2021/6/2 13:40
 */
public class SampleExamineCheckProjectAdapter extends BaseListDataRecyclerViewAdapter<SampleResultCheckProjectEntity> {
    public ArrayList<Long> id_selected = new ArrayList<>();
    public ArrayList<SampleResultCheckProjectEntity> selected_data = new ArrayList<>();
    public SampleExamineCheckProjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SampleResultCheckProjectEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SampleResultCheckProjectEntity> {
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
        @BindByTag("cb_select")
        CheckBox cb_select;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_examine_check_project;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(SampleResultCheckProjectEntity data) {

            data.setRowIndex(getLayoutPosition());
            data.setNeedCheck(true);
            data.setKey(data.getId());
            data.setChecked(true);

            int order = getLayoutPosition() + 1;
            tvOrderNumber.setText(context.getResources().getString(R.string.index) + order);
            tvCheckProject.setContent(((SampleResultCheckProjectEntity) data).getTestId().getName());
            repeat_number.setContent(String.valueOf(((SampleResultCheckProjectEntity) data).getParallelNo()));
            vision_number.setContent(null == ((SampleResultCheckProjectEntity) data).getTestId() ? "" : String.valueOf(((SampleResultCheckProjectEntity) data).getTestId().getBusiVersion()));
            check_method.setContent(null == ((SampleResultCheckProjectEntity) data).getTestMethodId() ? "" : ((SampleResultCheckProjectEntity) data).getTestMethodId().getMethod());
            operating_procedures.setContent(null == ((SampleResultCheckProjectEntity) data).getTestMethodId() ? "" : ((SampleResultCheckProjectEntity) data).getTestMethodId().getProcedureNo());
            sample_status.setContent(null == ((SampleResultCheckProjectEntity) data).getTestState() ? "" : ((SampleResultCheckProjectEntity) data).getTestState().getValue());
            check_person.setContent(null == ((SampleResultCheckProjectEntity) data).getTestStaffId() ? "" : ((SampleResultCheckProjectEntity) data).getTestStaffId().getName());
            check_time.setContent(null == ((SampleResultCheckProjectEntity) data).getTestTime() ? "" : MyDateUtils.getDateFromLong(((SampleResultCheckProjectEntity) data).getTestTime(), MyDateUtils.date_Format));
            memo.setContent(null == ((SampleResultCheckProjectEntity) data).getMemoField() ? "" : ((SampleResultCheckProjectEntity) data).getMemoField().toString());
            cb_select.setChecked(data.isSelect());

            if (data.isSelect()) {
                id_selected.add(data.getId());
                itemView.setBackground(context.getResources().getDrawable(R.drawable.shape_line_blue));
            }else {
                id_selected.remove(data.getId());
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {
                        id_selected.add(data.getId());
                        selected_data.add(data);
                        itemView.setBackground(context.getResources().getDrawable(R.drawable.shape_line_blue));
                    }else {
                        id_selected.remove(data.getId());
                        selected_data.remove(data);
                        itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                    list.get(getLayoutPosition()).setSelect(isChecked);
//                    notifyDataSetChanged();

                }
            });
            RxView.clicks(itemView)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Bundle bundle = new Bundle();
                            bundle.putLong(Constant.IntentKey.LIMS_SAMPLE_ID, data.getId());
                            bundle.putString(Constant.IntentKey.LIMS_SAMPLE_PROJECT_NAME, tvCheckProject.getContent());
                            IntentRouter.go(context, Constant.AppCode.LIMS_SampleResultCheckProjectDetail, bundle);
                        }
                    });

        }

    }

}
