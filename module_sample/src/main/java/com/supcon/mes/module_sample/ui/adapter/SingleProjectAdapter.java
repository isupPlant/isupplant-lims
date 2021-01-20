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
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomEditText;
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

import javax.script.Invocable;
import javax.script.ScriptEngine;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/13
 * Email:wanghaidong1@supcon.com
 */
public class SingleProjectAdapter extends BaseListDataRecyclerViewAdapter<InspectionSubEntity> {

    //    private ConclusionAdapter conclusionAdapter;
    private LinearLayoutManager linearLayoutManager;

    private OriginalValueChangeListener mOriginalValueChangeListener;
    private DispValueChangeListener mDispValueChangeListener;
    private String originalValue;
    private String dispValue;
    private boolean dispValueFocus;

    public boolean change;
    private SinglePickController mSinglePickController;
    private RecyclerView recyclerView;
    public int selectPosition=-1;
    public SingleProjectAdapter(Context context,RecyclerView recyclerView) {
        super(context);
        this.recyclerView = recyclerView;
        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionSubEntity> getViewHolder(int viewType) {
        return new SingleProjectAdapter.ViewHolder(context);
    }



    class ViewHolder extends BaseRecyclerViewHolder<InspectionSubEntity>{
        private ConclusionAdapter conclusionAdapter;
        @BindByTag("ctInspectionProject")
        CustomTextView ctInspectionProject;
        @BindByTag("busiVersionTv")
        CustomTextView busiVersionTv;
        @BindByTag("tvRepeatNumber")
        CustomTextView tvRepeatNumber;
        @BindByTag("ctInspectionItems")
        CustomTextView ctInspectionItems;
        @BindByTag("ceOriginalValue")
        CustomEditText ceOriginalValue;
        @BindByTag("cpOriginalValue")
        CustomSpinner cpOriginalValue;
        @BindByTag("ctRoundOffValue")
        CustomTextView ctRoundOffValue;
        @BindByTag("ceReportedValue")
        CustomEditText ceReportedValue;
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
        @BindByTag("llInspectionItems")
        LinearLayout llInspectionItems;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_inspection_single_project;
        }

        @Override
        protected void initView() {
            super.initView();
            linearLayoutManager = new LinearLayoutManager(context);
            rvConclusion.setLayoutManager(linearLayoutManager);
            ((DefaultItemAnimator)rvConclusion.getItemAnimator()).setSupportsChangeAnimations(false);
            rvConclusion.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int childLayoutPosition = parent.getChildAdapterPosition(view);

                    if (childLayoutPosition == 0 && childLayoutPosition == conclusionAdapter.getItemCount()-1) {
                        outRect.set(DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(0, context));
                    } else {
                        if (childLayoutPosition == 0){
                            outRect.set(DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context));
                        }else if(childLayoutPosition == conclusionAdapter.getItemCount()-1){
                            outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(0, context));
                        } else {
                            outRect.set(DisplayUtil.dip2px(0, context), 0, DisplayUtil.dip2px(0, context), DisplayUtil.dip2px(10, context));
                        }
                    }
                }

            });

            ceOriginalValue.setKeyTextColor(Color.parseColor("#666666"));
            ctRoundOffValue.setKeyColor(Color.parseColor("#666666"));
            cpOriginalValue.setKeyTextColor(Color.parseColor("#666666"));

        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            RxView.clicks(llInspectionItems)
                    .throttleFirst(1,TimeUnit.SECONDS)
                    .subscribe(o -> {
                        if (selectPosition==getAdapterPosition())
                            return;
                        selectPosition=getAdapterPosition();
                        onItemChildViewClick(itemView,3);
                        notifyDataSetChanged();
                    });
            RxView.clicks(imageUpDown)
                    .throttleFirst(2000,TimeUnit.MILLISECONDS)
                    .subscribe(o->{
                        onItemChildViewClick(imageUpDown,1);
                    });
            RxView.clicks(imageFileView)
                    .throttleFirst(2000,TimeUnit.MILLISECONDS)
                    .subscribe(o->{
                        onItemChildViewClick(imageFileView,2);
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




//            //原始值数值变化监听
//            RxTextView.textChanges(ceOriginalValue.editText())
//                    .skipInitialValue()
//                    .subscribe(new Consumer<CharSequence>() {
//                        @Override
//                        public void accept(CharSequence charSequence) throws Exception {
//                            if (StringUtil.isEmpty(charSequence.toString())){
//                                getItem(getAdapterPosition()).setOriginValue("");
//                                getItem(getAdapterPosition()).setRoundValue("");
//                                getItem(getAdapterPosition()).setDispValue("");
//
//                            }
//
//                        }
//                    });


//            //原始值焦点监听
//            ceOriginalValue.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    originalFocus = hasFocus;
//                    if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){
//                        mOriginalValueChangeListener.originalValueChange(originalFocus,originalValue,getAdapterPosition());
//                    }
//                }
//            });

            ceOriginalValue.editText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE){
                        change = true;
                        originalValue = ceOriginalValue.editText().getText().toString();
                        getList().get(getAdapterPosition()).setOriginValue(originalValue); //将输入的值设置为原始值

                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()){
                            imm.hideSoftInputFromWindow(v.getWindowToken(),0);      //隐藏软键盘
                        }

                        if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){ //调用监听事件 去执行计算
                            mOriginalValueChangeListener.originalValueChange(originalValue,getAdapterPosition());
                        }
                        return true;
                    }
                    return false;
                }
            });

            ceOriginalValue.findViewById(R.id.customDeleteIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ceOriginalValue.setContent(null);
                    ctRoundOffValue.setValue(null);
                    ceReportedValue.setContent(null);
                    InspectionSubEntity subEntity=getItem(getAdapterPosition());
                    subEntity.setOriginValue(null);
                    subEntity.setRoundValue(null);
                    subEntity.setDispValue(null);
                }
            });


            ceReportedValue.findViewById(R.id.customDeleteIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ceReportedValue.setContent("");
                    getItem(getAdapterPosition()).setDispValue("");
                }
            });



//            //报出值数值变化监听
//            RxTextView.textChanges(ceReportedValue.editText())
//                    .skipInitialValue()
//                    .subscribe(new Consumer<CharSequence>() {
//                        @Override
//                        public void accept(CharSequence charSequence) throws Exception {
//                            dispValue = charSequence.toString();
//                            getList().get(getAdapterPosition()).setDispValue(dispValue);
//
//                        }
//                    });
//
//            //报出值焦点监听
//            ceReportedValue.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    dispValueFocus = hasFocus;
//                    if (null != mDispValueChangeListener && getAdapterPosition() >= 0){
//                        mDispValueChangeListener.dispValueChange(dispValueFocus,dispValue,getAdapterPosition());
//                    }
//                }
//            });

            ceReportedValue.editText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE){
                        change = true;
                        dispValue = ceReportedValue.editText().getText().toString();
                        getList().get(getAdapterPosition()).setDispValue(dispValue);

                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()){
                            imm.hideSoftInputFromWindow(v.getWindowToken(),0);      //隐藏软键盘
                        }

                        if (null != mDispValueChangeListener && getAdapterPosition() >= 0){
                            mDispValueChangeListener.dispValueChange(dispValue,getAdapterPosition());
                        }
                        return true;
                    }

                    return false;
                }
            });

            //枚举原始值改变时的监听
            cpOriginalValue.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    String[] split = getList().get(getAdapterPosition()).getOptionNames().split(",");
                    List<String> list = Arrays.asList(split);
                    List<String> arrList = new ArrayList<>(list);
                    arrList.add(0,"");
                    mSinglePickController.list(arrList)
                            .listener(new SinglePicker.OnItemPickListener() {
                                @Override
                                public void onItemPicked(int index, Object item) {
                                    change = true;
                                    getItem(getAdapterPosition()).setOriginValue(arrList.get(index));
                                    notifyItemChanged(getAdapterPosition());
                                    if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){
                                        mOriginalValueChangeListener.originalValueChange(getItem(getAdapterPosition()).getOriginValue(),getAdapterPosition());
                                    }
                                }
                            }).show();
                }
            });

        }

        @Override
        protected void update(InspectionSubEntity data) {
            if (data.getValueSource()==null || TextUtils.isEmpty(data.getValueSource().getId())) {
                BaseIdValueEntity valueSource = new BaseIdValueEntity();
                valueSource.setId("LIMSSample_valueSource/enter");
                data.setValueSource(valueSource);
            }
            if (selectPosition==getAdapterPosition()){
                llInspectionItems.setBackground(context.getResources().getDrawable(R.drawable.shape_line_blue));
            }else {
                llInspectionItems.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            //检验项目
            ctInspectionProject.setValue(data.getSampleTestId() != null && data.getSampleTestId().getTestId() != null ? data.getSampleTestId().getTestId().getName() : "");
            busiVersionTv.setValue(data.getSampleTestId() != null && data.getSampleTestId().getTestId() != null ? data.getSampleTestId().getTestId().getBusiVersion() : "");
            ctInspectionItems.setContent(StringUtil.isEmpty(data.getComName()) ? "" : data.getComName());
            tvRepeatNumber.setValue(data.getParallelNo() != null ? data.getParallelNo().toString() : "");
            //检验分项
            ctInspectionItems.setContent(StringUtil.isEmpty(data.getComName()) ? "" : data.getComName());
            ceOriginalValue.editText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            ceOriginalValue.editText().setSingleLine();

            ceReportedValue.editText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            ceReportedValue.editText().setSingleLine();
            //原始值
            if(null != data.getValueKind()){ // 值类型
                if (!StringUtil.isEmpty(data.getValueKind().getId())){ // 值类型不为空
                    if (data.getValueKind().getId().equals(LimsConstant.ValueType.ENUM)){
                        setVisible(false);
                        cpOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    }else if (data.getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)){
                        setVisible(true);
                        ceOriginalValue.setEditable(false);
                        ceOriginalValue.setHint("");
                        ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    }else {
                        setVisible(true);
                        ceOriginalValue.setEditable(true);
                        if (StringUtil.isEmpty(data.getDefaultValue())){
                            ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                            ceOriginalValue.setHint(context.getResources().getString(R.string.lims_input_original_value));
                        }else {
                            if (StringUtil.isEmpty(data.getOriginValue())){  //原始值为空 并且默认值不为空  赋默认给原始 并去计算修约与报出
                                ceOriginalValue.setContent(data.getDefaultValue());
                                if (recyclerView.isComputingLayout()){
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){ //调用监听事件 去执行计算
                                                originalValue = data.getDefaultValue();
                                                mOriginalValueChangeListener.originalValueChange(originalValue,getAdapterPosition());
                                            }
                                        }
                                    });
                                }else {
                                    if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){ //调用监听事件 去执行计算
                                        originalValue = data.getDefaultValue();
                                        mOriginalValueChangeListener.originalValueChange(originalValue,getAdapterPosition());
                                    }
                                }
                            }else {
                                ceOriginalValue.setContent(data.getOriginValue());
                                ceOriginalValue.setHint(context.getResources().getString(R.string.lims_input_original_value));
                            }
                        }
                    }
                }else {
                    setVisible(true);
                }
            }else {
                setVisible(true);
            }
            ctRoundOffValue.setContent(StringUtil.isEmpty(data.getRoundValue()) ? "--" : data.getRoundValue()); //修约值
            ceReportedValue.setContent(StringUtil.isEmpty(data.getDispValue()) ? "" : data.getDispValue()); //报出值

            //判断当前分项中有没有 不合格的结论  有的话 为false
            //HashMap<String, Object> dispMap = data.getDispMap();
            List<ConclusionEntity> conclusionList = data.getConclusionList();
            for (int i = 0; i < conclusionList.size(); i++) {
                if (null != conclusionList.get(i).getFinalResult()){
                    if (conclusionList.get(i).getFinalResult().equals(conclusionList.get(i).getColumnList().get(conclusionList.get(i).getColumnList().size()-1).getResult())){
                        data.setConclusionState(false);
                    }else {
                        data.setConclusionState(true);
                    }
                    break;
                }else {
                    data.setConclusionState(true);
                }

//                for (String key : dispMap.keySet()){
//                    if (key.equals(conclusionList.get(i).getColumnKey())){
//
//                    }
//                }
            }

            if (data.isConclusionState()){
                ceReportedValue.setKeyTextColor(Color.parseColor("#666666"));
                ceReportedValue.setContentTextColor(Color.parseColor("#333333"));
            }else {
                ceReportedValue.setKeyTextColor(Color.parseColor("#B20404"));
                ceReportedValue.setContentTextColor(Color.parseColor("#B20404"));
            }

            if (data.isOpen()){
                rvConclusion.setVisibility(View.VISIBLE);
                ivExpand.setImageResource(R.drawable.ic_drop_up);
            }else {
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
                for (int j = 0; j < data.getConclusionList().get(i).getColumnList().size(); j++){
                    if (data.getConclusionList().get(i).getColumnList().get(j).getLoad()){
                        n ++;
                    }
                }
                if (n == 1){
                    data.getConclusionList().get(i).setOpen(true);
                }
            }
            conclusionAdapter.setData(data.getConclusionList(), data.getDispMap());
            conclusionAdapter.setList(data.getConclusionList());
            ceOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            cpOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            ceReportedValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            conclusionAdapter.notifyDataSetChanged();
            if (TextUtils.isEmpty(data.getOriginValue()) && !TextUtils.isEmpty(data.getDefaultValue())){
                data.setOriginValue(data.getDefaultValue());

                //originValChange(data,ceOriginalValue,ctRoundOffValue,ceReportedValue,cpOriginalValue);

                if (recyclerView.isComputingLayout()){
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){ //调用监听事件 去执行计算
                                originalValue = data.getDefaultValue();
                                mOriginalValueChangeListener.originalValueChange(originalValue,getAdapterPosition());
                            }
                        }
                    });
                }else {
                    if (null != mOriginalValueChangeListener && getAdapterPosition() >= 0){ //调用监听事件 去执行计算
                        originalValue = data.getDefaultValue();
                        mOriginalValueChangeListener.originalValueChange(originalValue,getAdapterPosition());
                    }
                }

            }

        }

        private void setVisible(boolean visible){
            if (visible){
                llCeOriginalValue.setVisibility(View.VISIBLE);
                llCpOriginalValue.setVisibility(View.GONE);
            }else {
                llCeOriginalValue.setVisibility(View.GONE);
                llCpOriginalValue.setVisibility(View.VISIBLE);
            }
        }
    }
    public void setOriginalValueChangeListener(int position, String originValue) {
        change=true;
        getList().get(position).setOriginValue(originValue); //将输入的值设置为原始值
        if (null != mOriginalValueChangeListener && position >= 0) { //调用监听事件 去执行计算
            mOriginalValueChangeListener.originalValueChange(originValue, position);
        }
    }
    ScriptEngine engine;

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    public void originValChange(InspectionSubEntity inspectionSubEntity,CustomEditText ceOriginalValue,CustomTextView ctRoundOffValue,CustomEditText ceReportedValue,CustomSpinner cpOriginalValue) {
        boolean clearFalg = false;
        if (!StringUtil.isEmpty(inspectionSubEntity.getOriginValue())) {
            if (inspectionSubEntity.getValueKind().getId().equals(LimsConstant.ValueType.NUMBER) || inspectionSubEntity.getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)) {
                //数值、计算类型
                //获取修约值
                Object roundValue = null;
                try {
                    Invocable invoke = (Invocable) engine;
                    roundValue = invoke.invokeFunction("roundingValue", inspectionSubEntity.getOriginValue(), inspectionSubEntity.getDigitType(), inspectionSubEntity.getCarrySpace(), inspectionSubEntity.getCarryType(), inspectionSubEntity.getCarryFormula());
                    roundValue = checkValue(roundValue+"");
                    inspectionSubEntity.setRoundValue(roundValue + "");
                    inspectionSubEntity.setDispValue(roundValue + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //高低限判断
                if (inspectionSubEntity.getLimitType() != null) {
                    try {
                        Invocable invoke = (Invocable) engine;
                        Object dispValue = invoke.invokeFunction("sectionJudgment", inspectionSubEntity.getOriginValue(), inspectionSubEntity.getLimitType().getId(), inspectionSubEntity.getMaxValue(), inspectionSubEntity.getMinValue());
                        if (dispValue != "reject") {
                            if ((dispValue + "").equals(inspectionSubEntity.getOriginValue()) ) {
                                inspectionSubEntity.setDispValue(dispValue + "");
                            } else {
                                inspectionSubEntity.setDispValue(roundValue + "");
                            }
                        } else {
                            clearFalg = true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                inspectionSubEntity.setOriginValue(inspectionSubEntity.getOriginValue());
                inspectionSubEntity.setRoundValue(inspectionSubEntity.getOriginValue());
                inspectionSubEntity.setDispValue(inspectionSubEntity.getOriginValue());
            }
        } else {
            clearFalg = true;
        }

        if (clearFalg) {
            inspectionSubEntity.setOriginValue(null);
            inspectionSubEntity.setRoundValue(null);
            inspectionSubEntity.setDispValue(null);
        }
        ceOriginalValue.setContent(inspectionSubEntity.getOriginValue());
        ctRoundOffValue.setContent(inspectionSubEntity.getRoundValue());
        ceReportedValue.setContent(inspectionSubEntity.getOriginValue());
        if (inspectionSubEntity.getValueKind().getId().equals(LimsConstant.ValueType.ENUM)){
            cpOriginalValue.setContent(inspectionSubEntity.getOriginValue());
            cpOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        }
        ceOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
        ceReportedValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
    }
    public String checkValue(String str){
        if (StringUtil.isEmpty(str)){
            return "";
        }
        if (com.supcon.mes.middleware.util.Util.isNumeric(str)){
            if (com.supcon.mes.middleware.util.Util.isContainPoint(str)){
                return com.supcon.mes.middleware.util.Util.removePoint(str);
            }else {
                return str;
            }
        }else {
            return str;
        }

    }
    public void setOriginalValueChangeListener(OriginalValueChangeListener mOriginalValueChangeListener){
        this.mOriginalValueChangeListener = mOriginalValueChangeListener;
    }

    public interface OriginalValueChangeListener{
        void originalValueChange(String value, int position);
    }

    public void setDispValueChangeListener(DispValueChangeListener mDispValueChangeListener){
        this.mDispValueChangeListener = mDispValueChangeListener;
    }

    public interface DispValueChangeListener{
        void dispValueChange(String value, int position);
    }
}
