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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private boolean originalFocus;
    private boolean dispValueFocus;

    private SinglePickController mSinglePickController;


    public SingleProjectAdapter(Context context) {
        super(context);
        mSinglePickController = new SinglePickController((Activity) context);
        mSinglePickController.setCanceledOnTouchOutside(true);
        mSinglePickController.setDividerVisible(true);
    }

    @Override
    protected BaseRecyclerViewHolder<InspectionSubEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<InspectionSubEntity>{
        private ConclusionAdapter conclusionAdapter;
        @BindByTag("ctInspectionProject")
        CustomTextView ctInspectionProject;
        @BindByTag("busiVersionTv")
        CustomTextView busiVersionTv;
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
        @BindByTag("tvRepeatNumber")
        CustomTextView tvRepeatNumber;


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
            cpOriginalValue.setKeyTextColor(Color.parseColor("#666666"));
            ceReportedValue.setKeyTextColor(Color.parseColor("#666666"));

        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();



            RxView.clicks(llQualityStandard)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            getList().get(getAdapterPosition()).setOpen(!getList().get(getAdapterPosition()).isOpen());
                            notifyItemChanged(getAdapterPosition());
                        }
                    });

            RxView.clicks(imageUpDown)
                    .throttleFirst(2000,TimeUnit.MILLISECONDS)
                    .subscribe(o->{
                        onItemChildViewClick(imageUpDown,1);
                    });
            RxView.clicks(imageFileView)
                    .throttleFirst(2000,TimeUnit.MILLISECONDS)
                    .subscribe(o->{
                        onItemChildViewClick(imageUpDown,2);
                    });

//            //原始值数值变化监听
//            RxTextView.textChanges(ceOriginalValue.editText())
//                    .skipInitialValue()
//                    .subscribe(new Consumer<CharSequence>() {
//                        @Override
//                        public void accept(CharSequence charSequence) throws Exception {
//                            originalValue = charSequence.toString();
//                            getList().get(getAdapterPosition()).setOriginValue(originalValue);
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


            ceReportedValue.editText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE){

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

            //枚举原始值改变时的监听
            cpOriginalValue.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    String[] split = getList().get(getAdapterPosition()).getOptionNames().split(",");
                    List<String> list = Arrays.asList(split);
                    mSinglePickController.list(list)
                            .listener(new SinglePicker.OnItemPickListener() {
                                @Override
                                public void onItemPicked(int index, Object item) {
                                    getItem(getAdapterPosition()).setOriginValue(list.get(index));
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
            ceOriginalValue.editText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            ceOriginalValue.editText().setSingleLine();

            ceReportedValue.editText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            ceReportedValue.editText().setSingleLine();
            
            //检验项目
            ctInspectionProject.setValue(data.getSampleTestId()!=null && data.getSampleTestId().getTestId()!=null?data.getSampleTestId().getTestId().getName():"");
            busiVersionTv.setValue(data.getSampleTestId()!=null && data.getSampleTestId().getTestId()!=null?data.getSampleTestId().getTestId().getBusiVersion():"");
            ctInspectionItems.setContent(StringUtil.isEmpty(data.getComName()) ? "--" : data.getComName());
            tvRepeatNumber.setValue(data.getParallelNo()!=null?data.getParallelNo().toString():"");
            //原始值
            if(null != data.getValueKind()){ // 值类型
                if (!StringUtil.isEmpty(data.getValueKind().getValue())){ // 值类型不为空
                    if (data.getValueKind().getValue().equals("枚举")){
                        setVisible(false);
                        cpOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    }else if (data.getValueKind().getValue().equals("计算")){
                        setVisible(true);
                        ceOriginalValue.setEditable(false);
                        ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    }else {
                        setVisible(true);
                        ceOriginalValue.setContent(StringUtil.isEmpty(data.getOriginValue()) ? "" : data.getOriginValue());
                    }
                }else {
                    setVisible(true);
                }
            }else {
                setVisible(true);
            }
            ctRoundOffValue.setContent(StringUtil.isEmpty(data.getRoundValue()) ? "--" : data.getRoundValue()); //修约值
            ceReportedValue.setContent(StringUtil.isEmpty(data.getDispValue()) ? "--" : data.getDispValue()); //报出值

            //判断当前分项中有没有 不合格的结论  有的话 为false
            //HashMap<String, Object> dispMap = data.getDispMap();
            List<ConclusionEntity> conclusionList = data.getConclusionList();
            for (int i = 0; i < conclusionList.size(); i++) {
                if (null != conclusionList.get(i).getFinalResult()){
                    if (conclusionList.get(i).getFinalResult().equals(conclusionList.get(i).getColumnList().get(0).getResult())){
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
                ctInspectionItems.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else {
                ctInspectionItems.setBackgroundColor(Color.parseColor("#B20404"));
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
            if (!TextUtils.isEmpty(data.getFileUploadMultiFileIds())){
                new LimsFileUpLoadController().loadFile(data.getFileUploadMultiFileIds(),data.getFileUploadMultiFileNames()).setFileOnSuccessListener(new OnSuccessListener<File>() {
                    @Override
                    public void onSuccess(File result) {
                        data.setFilePath(result.getPath());
                    }
                });
            }
            rvConclusion.setAdapter(conclusionAdapter);
            conclusionAdapter.setData(data.getConclusionList(), data.getDispMap());
            conclusionAdapter.setList(data.getConclusionList());
            conclusionAdapter.notifyDataSetChanged();
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

    public void setOriginalValueChangeListener(OriginalValueChangeListener mOriginalValueChangeListener){
        this.mOriginalValueChangeListener = mOriginalValueChangeListener;
    }

    public interface OriginalValueChangeListener{
        void originalValueChange( String value, int position);
    }

    public void setDispValueChangeListener(DispValueChangeListener mDispValueChangeListener){
        this.mDispValueChangeListener = mDispValueChangeListener;
    }

    public interface DispValueChangeListener{
        void dispValueChange( String value, int position);
    }
}
