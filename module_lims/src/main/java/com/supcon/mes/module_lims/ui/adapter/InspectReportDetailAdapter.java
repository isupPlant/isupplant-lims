package com.supcon.mes.module_lims.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class InspectReportDetailAdapter extends BaseListDataRecyclerViewAdapter {
    public InspectReportDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        if (viewType == 1) {
            return new InsportReportDetailViewHolder(context);
        } else if (viewType == 2) {
            return new StdJudgeSpecViewHolder(context);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position, Object o) {
        if (o instanceof InspectReportDetailEntity) {
            InspectReportDetailEntity entity = (InspectReportDetailEntity) o;
            return entity.getTypeView();
        }
        return super.getItemViewType(position, o);
    }

    class InsportReportDetailViewHolder extends BaseRecyclerViewHolder<StdJudgeSpecEntity> {

        @BindByTag("reportNameTv")
        CustomTextView reportNameTv;
        @BindByTag("dispvalueTv")
        CustomTextView dispvalueTv;
        @BindByTag("checkResultTv")
        CustomTextView checkResultTv;
        @BindByTag("rangeImg")
        ImageView rangeImg;
        private boolean isExpand;

        public InsportReportDetailViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspect_project;
        }

        @Override
        protected void initView() {
            super.initView();

        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(rangeImg)
                    .throttleFirst(1000, TimeUnit.MICROSECONDS)
                    .subscribe(o -> {
                        int position = getAdapterPosition();
                        StdJudgeSpecEntity detailEntity = (StdJudgeSpecEntity) getItem(position);
                        if (detailEntity.getTypeView() == 1) {
                            List<StdJudgeEntity> stdJudgeSpecEntities = detailEntity.getSpec();
                            if (stdJudgeSpecEntities != null && !stdJudgeSpecEntities.isEmpty()) {
                                int size = stdJudgeSpecEntities.size();
                                if (!detailEntity.isExpand) {
                                    for (int i = 0; i < size; i++) {
                                        getList().add(position + i + 1, stdJudgeSpecEntities.get(i));
                                    }
                                } else {
                                    getList().removeAll(stdJudgeSpecEntities);
                                }
                                detailEntity.isExpand = !detailEntity.isExpand;
                                notifyDataSetChanged();
                            } else {
                                ToastUtils.show(context, "没有更多展开的内容了！");
                            }
                        }
                    });
        }

        @Override
        protected void update(StdJudgeSpecEntity data) {
            reportNameTv.setValue(data.reportName);
            dispvalueTv.setValue(data.dispValue);
            checkResultTv.setValue(data.checkResult);
            if ("不合格".equals(data.checkResult)){
                checkResultTv.setValueColor(Color.parseColor("#F70606"));
            }else {
                checkResultTv.setValueColor(Color.parseColor("#0BC8C1"));
            }
            if (data.isExpand) {
                rangeImg.setImageResource(R.drawable.ic_inspect_down_arrow);
            } else {
                rangeImg.setImageResource(R.drawable.ic_inspect_up_arrow);
            }
        }
    }

    class StdJudgeSpecViewHolder extends BaseRecyclerViewHolder<StdJudgeEntity> {

        @BindByTag("judgeRangeTv")
        CustomTextView judgeRangeTv;

        public StdJudgeSpecViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_report_range;
        }

        @Override
        protected void update(StdJudgeEntity data) {
            judgeRangeTv.setKey(data.resultValue + "范围");
            judgeRangeTv.setValue(data.dispValue);
        }
    }
}
