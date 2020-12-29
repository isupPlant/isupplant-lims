package com.supcon.mes.module_lims.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailEntity;
import com.supcon.mes.module_lims.model.bean.QualityStdConclusionEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecEntity;
import com.supcon.mes.module_lims.model.bean.TestReportEditPtEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author huodongsheng
 * on 2020/11/4
 * class name
 */
public class TestReportEditPtAdapter extends BaseListDataRecyclerViewAdapter {
    private List<QualityStdConclusionEntity> conclusionList;
    SinglePickController mSinglePickController;
    private List<String> stringList = new ArrayList<>();
    private Boolean needLab;
    private DispValueChangeListener mDispValueChangeListener;
    private ConclusionChangeListener mConclusionChangeListener;

    public TestReportEditPtAdapter(Context context) {
        super(context);
        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        if (viewType == 1) {
            return new ViewHolder(context);
        } else if (viewType == 2) {
            return new RangeViewHolder(context);
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

    public void setNeedLab(Boolean needLab) {
        this.needLab = needLab;
    }

    public void setConclusionOption(List<QualityStdConclusionEntity> conclusionList) {
        this.conclusionList = conclusionList;
    }

    class ViewHolder extends BaseRecyclerViewHolder<StdJudgeSpecEntity> {
        private String dispValue;
        @BindByTag("ctTestProject")
        CustomTextView ctTestProject;
        @BindByTag("ceDispValue")
        CustomEditText ceDispValue;
        @BindByTag("csDispValue")
        CustomSpinner csDispValue;
        @BindByTag("csTestConclusion")
        CustomSpinner csTestConclusion;
        @BindByTag("rangeImg")
        ImageView rangeImg;
        @BindByTag("llCeDispValue")
        LinearLayout llCeDispValue;
        @BindByTag("llEnumDispValue")
        LinearLayout llEnumDispValue;

        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_test_report_edit_pt;
        }

        @Override
        protected void initView() {
            super.initView();
            ceDispValue.setKeyTextColor(Color.parseColor("#666666"));
            csDispValue.setKeyTextColor(Color.parseColor("#666666"));
            csTestConclusion.setKeyTextColor(Color.parseColor("#666666"));
            if (needLab) {
                ceDispValue.setEditable(false);
                csDispValue.setEditable(false);
            } else {
                ceDispValue.setEditable(true);
                csDispValue.setEditable(true);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(rangeImg)
                    .throttleFirst(1000, TimeUnit.MICROSECONDS)
                    .subscribe(o -> {
                        setRangeClick();
                    });

            //结论
            csTestConclusion.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    stringList.clear();
                    for (QualityStdConclusionEntity conclusionEntity : conclusionList) {
                        stringList.add(conclusionEntity.getName());
                    }
                    stringList.add(0, "");
                    mSinglePickController.list(stringList)
                            .listener(new SinglePicker.OnItemPickListener() {
                                @Override
                                public void onItemPicked(int index, Object item) {
                                    StdJudgeSpecEntity entity = (StdJudgeSpecEntity) getItem(getAdapterPosition());
                                    entity.checkResult = stringList.get(index);
                                    notifyItemChanged(getAdapterPosition());
                                    if (null != mConclusionChangeListener) {
                                        mConclusionChangeListener.conclusionChangeClick();
                                    }
                                }
                            }).show();
                }
            });

            ceDispValue.findViewById(R.id.customDeleteIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ceDispValue.setContent("");
                    ((StdJudgeSpecEntity) getItem(getAdapterPosition())).dispValue = "";
                }
            });

            ceDispValue.editText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        dispValue = ceDispValue.editText().getText().toString();
                        ((StdJudgeSpecEntity) getList().get(getAdapterPosition())).dispValue = dispValue;

                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);      //隐藏软键盘
                        }

                        if (null != mDispValueChangeListener && getAdapterPosition() >= 0) {
                            mDispValueChangeListener.dispValueChange(dispValue, getAdapterPosition());
                        }
                        return true;
                    }
                    return false;
                }
            });

            RxView.clicks(itemViewDelBtn)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(itemViewDelBtn, 2);
                    });
        }
        private void setRangeClick(){
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
                    ToastUtils.show(context, context.getResources().getString(R.string.lims_not_content));
                }
            }
        }

        @Override
        protected void update(StdJudgeSpecEntity data) {
            //检验项目
            ctTestProject.setContent(data.reportName == null ? "" : data.reportName);
            //报出值
            if (data.valueKind != null) {
                if (data.valueKind.getId().equals(LimsConstant.ValueType.ENUM)) {
                    llCeDispValue.setVisibility(View.GONE);
                    llEnumDispValue.setVisibility(View.VISIBLE);
                } else {
                    llCeDispValue.setVisibility(View.VISIBLE);
                    llEnumDispValue.setVisibility(View.GONE);
                }
            } else {
                llCeDispValue.setVisibility(View.VISIBLE);
                llEnumDispValue.setVisibility(View.GONE);
            }
            ceDispValue.setContent(data.dispValue == null ? "" : data.dispValue);
            csDispValue.setContent(data.dispValue == null ? "" : data.dispValue);


            ceDispValue.editText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            ceDispValue.editText().setSingleLine();
            //结论
            csTestConclusion.setContent(data.checkResult == null ? "" : data.checkResult);


            if (!StringUtil.isEmpty(data.checkResult)) {
                for (QualityStdConclusionEntity conclusionEntity : conclusionList) {
                    if (conclusionEntity.getName().equals(data.checkResult == null ? "" : data.checkResult)) {
                        if ((conclusionEntity.getStdGrade().getId() == null ? "" : conclusionEntity.getStdGrade().getId())
                                .equals(LimsConstant.ConclusionType.UN_QUALIFIED)) {
                            csTestConclusion.setContentTextColor(context.getResources().getColor(R.color.warningRed));
                        } else {
                            csTestConclusion.setContentTextColor(context.getResources().getColor(R.color.lightGreen));
                        }

                    }
                }
            }


            if (data.isExpand) {
                rangeImg.setImageResource(R.drawable.ic_inspect_down_arrow);
            } else {
                rangeImg.setImageResource(R.drawable.ic_inspect_up_arrow);
            }
        }
    }

    class RangeViewHolder extends BaseRecyclerViewHolder<StdJudgeEntity> {

        @BindByTag("judgeRangeTv")
        CustomTextView judgeRangeTv;

        public RangeViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_report_range;
        }

        @Override
        protected void update(StdJudgeEntity data) {
            judgeRangeTv.setKey(data.resultValue + context.getResources().getString(R.string.lims_range));
            judgeRangeTv.setValue(data.dispValue);
        }
    }

    public void setDispValueChangeListener(DispValueChangeListener mDispValueChangeListener) {
        this.mDispValueChangeListener = mDispValueChangeListener;
    }

    public interface DispValueChangeListener {
        void dispValueChange(String value, int position);
    }

    public void setConclusionChangeListener(ConclusionChangeListener mConclusionChangeListener) {
        this.mConclusionChangeListener = mConclusionChangeListener;
    }

    public interface ConclusionChangeListener {
        void conclusionChangeClick();
    }
}
