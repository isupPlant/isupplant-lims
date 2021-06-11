package com.supcon.mes.module_sample.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class SampleProjectAdapter extends BaseListDataRecyclerViewAdapter<InspectionSubEntity> {

    //    private ConclusionAdapter conclusionAdapter;
    private LinearLayoutManager linearLayoutManager;

    private OriginalValueChangeListener mOriginalValueChangeListener;
    private String originalValue;
    private RecyclerView recyclerView;

    private SinglePickController mSinglePickController;

    public int selectPosition = -1;

    public SampleProjectAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        this.recyclerView = recyclerView;
        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionSubEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionSubEntity> {
        private ConclusionAdapter conclusionAdapter;
        @BindByTag("ctInspectionItems")
        CustomTextView ctInspectionItems;
        @BindByTag("ceOriginalValue")
        CustomTextView ceOriginalValue;
        @BindByTag("cpOriginalValue")
        CustomSpinner cpOriginalValue;
        @BindByTag("ctRoundOffValue")
        CustomTextView ctRoundOffValue;
        @BindByTag("ceReportedValue")
        CustomTextView ceReportedValue;
        @BindByTag("ivExpand")
        ImageView ivExpand;
        @BindByTag("llQualityStandard")
        LinearLayout llQualityStandard;
        @BindByTag("llEnclosure")
        LinearLayout llEnclosure;
        @BindByTag("rvConclusion")
        RecyclerView rvConclusion;
        @BindByTag("llCeOriginalValue")
        LinearLayout llCeOriginalValue;
        @BindByTag("llCpOriginalValue")
        LinearLayout llCpOriginalValue;
        @BindByTag("imageUpDown")
        ImageView imageUpDown;
        @BindByTag("imageFileView")
        ImageView imageFileView;
        @BindByTag("tvRepeatNumber")
        CustomTextView tvRepeatNumber;
        @BindByTag("llInspectionItems")
        LinearLayout llInspectionItems;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_sub_project;
        }

        @Override
        protected void initView() {
            super.initView();
            ceOriginalValue.setEditable(false);
            ceReportedValue.setEditable(false);

            //属性框设置key文字居中
            TextView textView = ceOriginalValue.findViewById(R.id.customKey);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            textView.setLayoutParams(params);
            textView = ctRoundOffValue.findViewById(R.id.customKey);
            params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            textView.setLayoutParams(params);
            textView = ceReportedValue.findViewById(R.id.customKey);
            params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            textView.setLayoutParams(params);

            imageUpDown.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(context);
            rvConclusion.setLayoutManager(linearLayoutManager);
            ((DefaultItemAnimator) rvConclusion.getItemAnimator()).setSupportsChangeAnimations(false);
            rvConclusion.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int childLayoutPosition = parent.getChildAdapterPosition(view);

                    if (childLayoutPosition == 0 && childLayoutPosition == conclusionAdapter.getItemCount() - 1) {
                        outRect.set(DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(0, context));
                    } else {
                        if (childLayoutPosition == 0) {
                            outRect.set(DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context));
                        } else if (childLayoutPosition == conclusionAdapter.getItemCount() - 1) {
                            outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(0, context));
                        } else {
                            outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context));
                        }
                    }
                }

            });

            ceOriginalValue.setKeyTextColor(Color.parseColor("#666666"));
            ceReportedValue.setKeyTextColor(Color.parseColor("#666666"));
            ctRoundOffValue.setKeyColor(Color.parseColor("#666666"));
            cpOriginalValue.setKeyTextColor(Color.parseColor("#666666"));
            tvRepeatNumber.setKeyColor(Color.parseColor("#666666"));

        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();


            RxView.clicks(llInspectionItems)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(o -> {
                        if (selectPosition == getAdapterPosition())
                            return;
                        selectPosition = getAdapterPosition();
                        onItemChildViewClick(itemView, 3);
                        notifyDataSetChanged();
                    });
            RxView.clicks(imageUpDown)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(imageUpDown, 1);
                    });
            RxView.clicks(imageFileView)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(imageFileView, 2);
                    });

            RxView.clicks(llQualityStandard)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            getList().get(getAdapterPosition()).setOpen(!getList().get(getAdapterPosition()).isOpen());
                            notifyItemChanged(getAdapterPosition());
                        }
                    });

            //枚举原始值改变时的监听
            cpOriginalValue.setOnChildViewClickListener((childView, action, obj) -> {
                String[] split = getList().get(getAdapterPosition()).getOptionNames().split(",");
                List<String> list = Arrays.asList(split);
                List<String> arrList = new ArrayList<>(list);
                arrList.add(0, "");
                mSinglePickController.list(arrList)
                        .listener((index, item) -> {
                            getItem(getAdapterPosition()).setOriginValue(arrList.get(index));
                            notifyItemChanged(getAdapterPosition());
                            if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0) {
                                mOriginalValueChangeListener.originalValueChange(getItem(getAdapterPosition()).getOriginValue(), getAdapterPosition());
                            }
                        }).show();
            });

        }

        @Override
        protected void update(InspectionSubEntity data) {
            if (data.getValueSource() == null || TextUtils.isEmpty(data.getValueSource().getId())) {
                BaseIdValueEntity valueSource = new BaseIdValueEntity();
                valueSource.setId("LIMSSample_valueSource/enter");
                data.setValueSource(valueSource);
            }

            if (selectPosition == getAdapterPosition()) {
                llInspectionItems.setBackground(context.getResources().getDrawable(R.drawable.shape_line_blue));
            } else {
                llInspectionItems.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            //检验项目
            ctInspectionItems.setContent(StringUtil.isEmpty(data.getComName()) ? "" : data.getComName());

            //原始值
            if (null != data.getValueKind()) { // 值类型
                if (!StringUtil.isEmpty(data.getValueKind().getId())) { // 值类型不为空
                    if (data.getValueKind().getId().equals(LimsConstant.ValueType.ENUM)) {
                        setVisible(false);
                        cpOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    } else if (data.getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)) {
                        setVisible(true);
                        ceOriginalValue.setEditable(false);
//                        ceOriginalValue.setHint("");
                        ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    } else {
                        setVisible(true);
//                        ceOriginalValue.setEditable(true);
                        if (StringUtil.isEmpty(data.getDefaultValue())) {
                            ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
//                            ceOriginalValue.setHint(context.getResources().getString(R.string.lims_input_original_value));
                        } else {
                            if (StringUtil.isEmpty(data.getOriginValue())) {  //原始值为空 并且默认值不为空  赋默认给原始 并去计算修约与报出
                                ceOriginalValue.setContent(data.getDefaultValue());
                                if (recyclerView.isComputingLayout()) {
                                    recyclerView.post(() -> {
                                        if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0) { //调用监听事件 去执行计算
                                            originalValue = data.getDefaultValue();
                                            mOriginalValueChangeListener.originalValueChange(originalValue, getAdapterPosition());
                                        }
                                    });
                                } else {
                                    if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0) { //调用监听事件 去执行计算
                                        originalValue = data.getDefaultValue();
                                        mOriginalValueChangeListener.originalValueChange(originalValue, getAdapterPosition());
                                    }
                                }
                            } else {
                                ceOriginalValue.setContent(data.getOriginValue());
                            }
                        }

                    }
                } else {
                    setVisible(true);
                }
            } else {
                setVisible(true);
            }
            ctRoundOffValue.setContent(StringUtil.isEmpty(data.getRoundValue()) ? "" : data.getRoundValue()); //修约值
            ceReportedValue.setContent(StringUtil.isEmpty(data.getDispValue()) ? "" : data.getDispValue()); //报出值
            tvRepeatNumber.setContent(data.getParallelNo() == null ? "" : data.getParallelNo().toString()); //重复号

            //判断当前分项中有没有 不合格的结论  有的话 为false
            //HashMap<String, Object> dispMap = data.getDispMap();
            List<ConclusionEntity> conclusionList = data.getConclusionList();
            for (int i = 0; i < conclusionList.size(); i++) {
                if (null != conclusionList.get(i).getFinalResult()) {
                    data.setConclusionState(!conclusionList.get(i).getFinalResult().equals(conclusionList.get(i).getColumnList().get(conclusionList.get(i).getColumnList().size() - 1).getResult()));
                    break;
                } else {
                    data.setConclusionState(true);
                }
            }


            if (data.isOpen()) {
                rvConclusion.setVisibility(View.VISIBLE);
                ivExpand.setImageResource(R.drawable.ic_drop_up);
            } else {
                rvConclusion.setVisibility(View.GONE);
                ivExpand.setImageResource(R.drawable.ic_drop_down);
            }

            conclusionAdapter = new ConclusionAdapter(context);
            conclusionAdapter.setOnConclusionChangeListener(new ConclusionAdapter.OnConclusionChangeListener() {
                @Override
                public void onConclusionChange(int position) {
                    notifyItemChanged(getAdapterPosition());
                }
            });


            if (data.getFileUploadMultiFileIds() != null && !data.getFileUploadMultiFileIds().isEmpty()) {
                new LimsFileUpLoadController()
                        .loadFile(data.getFileUploadMultiFileIds(), data.getFileUploadMultiFileNames())
                        .setFileOnSuccessListener(new OnSuccessListener<List<AttachmentSampleInputEntity>>() {
                            @Override
                            public void onSuccess(List<AttachmentSampleInputEntity> result) {
                                data.setAttachmentSampleInputEntities(result);
                            }
                        });
            }


            rvConclusion.setAdapter(conclusionAdapter);
            for (int i = 0; i < data.getConclusionList().size(); i++) {
                int n = 0;
                for (int j = 0; j < data.getConclusionList().get(i).getColumnList().size(); j++) {
                    if (data.getConclusionList().get(i).getColumnList().get(j).getLoad()) {
                        n++;
                    }
                }
                if (n == 1) {
                    data.getConclusionList().get(i).setOpen(true);
                }
            }
            conclusionAdapter.setData(data.getConclusionList(), data.getDispMap());
            conclusionAdapter.setList(data.getConclusionList());
            ceOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            cpOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            ceReportedValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            conclusionAdapter.notifyDataSetChanged();
            if (TextUtils.isEmpty(data.getOriginValue()) && !TextUtils.isEmpty(data.getDefaultValue())) {
                data.setOriginValue(data.getDefaultValue());

                //originValChange(data,ceOriginalValue,ctRoundOffValue,ceReportedValue,cpOriginalValue);//设置其他修约报出值

                if (recyclerView.isComputingLayout()) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0) { //调用监听事件 去执行计算
                                originalValue = data.getDefaultValue();
                                mOriginalValueChangeListener.originalValueChange(originalValue, getAdapterPosition());
                            }
                        }
                    });
                } else {
                    if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0) { //调用监听事件 去执行计算
                        originalValue = data.getDefaultValue();
                        mOriginalValueChangeListener.originalValueChange(originalValue, getAdapterPosition());
                    }
                }
            }
        }

        private void setVisible(boolean visible) {
            if (visible) {
                llCeOriginalValue.setVisibility(View.VISIBLE);
                llCpOriginalValue.setVisibility(View.GONE);
            } else {
                llCeOriginalValue.setVisibility(View.GONE);
                llCpOriginalValue.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setOriginalValueChangeListener(int position, String originValue) {
        getList().get(position).setOriginValue(originValue); //将输入的值设置为原始值
        if (null != mOriginalValueChangeListener && position >= 0) { //调用监听事件 去执行计算
            mOriginalValueChangeListener.originalValueChange(originValue, position);
        }
    }


    public void setOriginalValueChangeListener(OriginalValueChangeListener mOriginalValueChangeListener) {
        this.mOriginalValueChangeListener = mOriginalValueChangeListener;
    }

    public interface OriginalValueChangeListener {
        void originalValueChange(String value, int position);
    }

    public void setDispValueChangeListener(DispValueChangeListener mDispValueChangeListener) {
    }

    public interface DispValueChangeListener {
        void dispValueChange(String value, int position);
    }
}
